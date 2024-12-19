package year2024.day19;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/19
 */
public class Day19 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day19/input_test_01.txt";

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Input parsedInput = parseInput(input);
        final List<String> towelPatterns = parsedInput.getTowelPatterns();
        final List<String> desiredPatterns = parsedInput.getDesiredPatterns();

        final Map<String, Long> cache = new HashMap<>();
        final long possibilities = desiredPatterns.stream()
                .map(d -> countPossible(d, towelPatterns, cache))
                .filter(count -> count > 0)
                .count();

        return String.valueOf(possibilities);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Input parsedInput = parseInput(input);
        final List<String> towelPatterns = parsedInput.getTowelPatterns();
        final List<String> desiredPatterns = parsedInput.getDesiredPatterns();

        final Map<String, Long> cache = new HashMap<>();
        final long count = desiredPatterns.stream().mapToLong(
                d -> countPossible(d, towelPatterns, cache)
        ).sum();

        return String.valueOf(count);
    }

    private long countPossible(
            final String design,
            final List<String> towels,
            final Map<String, Long> cache
    ) {
        if (cache.containsKey(design)) {
            return cache.get(design);
        }
        long count = 0L;
        for (final String towel : towels) {
            if (design.equals(towel)) {
                count += 1;
            } else if (design.startsWith(towel)) {
                count += countPossible(design.substring(towel.length()), towels, cache);
            }
        }
        cache.put(design, count);
        return count;
    }

    private Input parseInput(final List<String> input) {
        final List<String> towelPatterns = new ArrayList<>(Arrays.stream(input.get(0).split(",")).map(String::trim).toList());

        final List<String> desiredPatterns = new ArrayList<>();
        for (int i = 2; i < input.size(); i++) {
            desiredPatterns.add(input.get(i));
        }

        return new Input(towelPatterns, desiredPatterns);
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Data
    @AllArgsConstructor
    public class Input {

        List<String> towelPatterns;
        List<String> desiredPatterns;
    }
}
