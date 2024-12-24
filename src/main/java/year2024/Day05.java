package year2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.apache.commons.collections4.ListUtils;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * See https://adventofcode.com/2024/day/5
 */
public class Day05 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day05/input_test_01.txt";

    public String part1(final List<String> input) {
        // final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Map<Integer, List<Integer>> rules = new HashMap<>();
        final List<List<Integer>> updates = new ArrayList<>();
        parseInput(input, rules, updates);

        final List<List<Integer>> validUpdates = updates.stream()
                .filter(update -> isUpdateValid(update, rules))
                .toList();

        final int sum = validUpdates.stream().mapToInt(validUpdate -> validUpdate.get(validUpdate.size() / 2)).sum();
        return String.valueOf(sum);
    }

    public String part2(final List<String> input) {
        // final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Map<Integer, List<Integer>> rules = new HashMap<>();
        final List<List<Integer>> updates = new ArrayList<>();
        parseInput(input, rules, updates);

        final List<List<Integer>> invalidUpdates = updates.stream()
                .filter(update -> !isUpdateValid(update, rules))
                .toList();

        final List<List<Integer>> validUpdates = sortInvalidUpdates(invalidUpdates, rules);

        final int sum = validUpdates.stream().mapToInt(validUpdate -> validUpdate.get(validUpdate.size() / 2)).sum();
        return String.valueOf(sum);
    }


    private boolean isUpdateValid(
            final List<Integer> update,
            final Map<Integer, List<Integer>> rules
    ) {
        for (int i = 0; i < update.size() - 1; i++) {
            final List<Integer> ruleSet = ListUtils.emptyIfNull(rules.get(update.get(i)));
            if (!ruleSet.contains(update.get(i + 1))) {
                return false;
            }
        }
        return true;
    }

    private void parseInput(
            final List<String> input,
            final Map<Integer, List<Integer>> rules,
            final List<List<Integer>> updates
    ) {

        for (final String line : input) {
            if (line.contains("|")) {
                final String[] rule = line.split("\\|");
                rules.computeIfAbsent(Integer.parseInt(rule[0]), k -> new ArrayList<>()).add(Integer.parseInt(rule[1]));
            } else if (line.contains(",")) {
                updates.add(Arrays.stream(line.split(",")).map(Integer::parseInt).toList());
            }
        }

        log("Rules: " + rules);
        log("Updates: " + updates);
    }


    private List<List<Integer>> sortInvalidUpdates(
            final List<List<Integer>> invalidUpdates,
            final Map<Integer, List<Integer>> rules
    ) {
        return invalidUpdates.stream().map(u -> backtrackAsQueue(u, rules)).toList();
    }

    private List<Integer> backtrackAsQueue(
            final List<Integer> invalidUpdate,
            final Map<Integer, List<Integer>> rules

    ) {
        final Queue<Node> queue = new ArrayDeque<>();
        for (int i = 0; i < invalidUpdate.size(); i++) {
            queue.add(new Node(List.of(i), List.of(invalidUpdate.get(i))));
        }

        while (!queue.isEmpty()) {

            final Node node = queue.poll();
            final List<Integer> visited = node.getVisited();
            final List<Integer> temp = node.getTemp();

            if (visited.size() == invalidUpdate.size()) {
                log("Solution: " + temp.toString());
                return temp; // We got a solution.
            }

            for (int i = 0; i < invalidUpdate.size(); i++) {
                // We find no rules, or we already visited the node -> we skip to the next
                final List<Integer> nextNodes = ListUtils.emptyIfNull(rules.get(temp.get(temp.size() - 1)));
                if (visited.contains(i) || nextNodes.isEmpty() || !nextNodes.contains(invalidUpdate.get(i))) {
                    continue;
                }

                final List<Integer> newVisited = new ArrayList<>(visited);
                newVisited.add(i);
                final List<Integer> newTemp = new ArrayList<>(temp);
                newTemp.add(invalidUpdate.get(i));

                queue.add(new Node(newVisited, newTemp));
            }
        }

        return Collections.emptyList();
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Data
    @AllArgsConstructor
    private static class Node {

        private List<Integer> visited;
        private List<Integer> temp;
    }

}
