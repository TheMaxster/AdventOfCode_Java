package year2023.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/12
 */
public class Day12 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        return String.valueOf(calculateSum(input, 1));
    }

    @Override
    public String part2(final List<String> input) {
        return String.valueOf(calculateSum(input, 5));
    }

    private static long calculateSum(
            final List<String> inputs,
            final int foldingFactor
    ) {
        long sum = 0;
        final HashMap<String, Long> cache = new HashMap<>();
        for (final String input : inputs) {
            final SpringConfig config = processInput(input, foldingFactor);
            sum += arrange(config.condition(), config.records(), -1, cache);
        }
        return sum;
    }

    // processInput parses a single input row with the given folding factor.
    // The folding factor defines how many times the input string and the spring condition should be repeated
    private static SpringConfig processInput(
            final String input,
            final int foldingFactor
    ) {
        final String[] split = input.split(" ");
        final String condition = split[0];
        final List<Integer> records = Arrays.stream(split[1].split(","))
                .map(Integer::valueOf)
                .toList();

        final List<Integer> extendedRecords = new ArrayList<>(records);
        final StringBuilder extendedCondition = new StringBuilder(condition);
        for (int i = 1; i < foldingFactor; i++) {
            extendedCondition.append("?").append(condition);
            extendedRecords.addAll(records);
        }

        return new SpringConfig(extendedCondition.toString(), extendedRecords);
    }

    private static long arrange(
            final String row,
            final List<Integer> groups,
            int currentGroup,
            final HashMap<String, Long> cache
    ) {
        final String key = generateKey(row, groups, currentGroup);
        final Long m = cache.getOrDefault(key, null);

        if (m != null) {
            return m;
        }

        if (Objects.equals(row, "") && groups.isEmpty() && currentGroup <= 0) {
            return 1;
        } else if (row.length() == 0) {
            return 0;
        }

        long d = 0;

        List<Integer> groupsSublist = groups;
        if (row.length() > 0) {
            switch (row.charAt(0)) {
                case '#' -> {
                    if (currentGroup == 0 || (currentGroup == -1 && groupsSublist.isEmpty())) {
                        return 0;
                    } else if (currentGroup == -1) {
                        currentGroup = groupsSublist.get(0);
                        groupsSublist = groups.subList(1, groups.size());
                    }
                    d = arrange(getSubstring(row), groupsSublist, currentGroup - 1, cache);
                }
                case '.' -> {
                    if (currentGroup <= 0) {
                        d = arrange(getSubstring(row), groupsSublist, -1, cache);
                    }
                }
                case '?' -> {
                    d = arrange("#" + getSubstring(row), groupsSublist, currentGroup, cache) + arrange("." + getSubstring(row),
                            groupsSublist, currentGroup, cache);
                }
            }
        }
        cache.put(generateKey(row, groupsSublist, currentGroup), d);
        return d;
    }

    private static String generateKey(
            final String row,
            final List<Integer> groups,
            final int currentGroups
    ) {
        return row + "->" + groups.stream().map(String::valueOf).collect(Collectors.joining(",")) + "->" + currentGroups;
    }

    private static String getSubstring(final String string) {
        if (string.length() - 1 < 1) {
            return "";
        } else {
            return string.substring(1);
        }
    }

    private record SpringConfig(String condition, List<Integer> records) {

    }


}
