package year2024.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day11 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day11/input_test_01.txt";

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final List<Long> parsedInput = parseInput(input);
        log("Parsed Input: " + parsedInput);

        final int numberOfBlinks = 25;

        final long answer = calculate(parsedInput, numberOfBlinks);
        return String.valueOf(answer);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final List<Long> parsedInput = parseInput(input);
        log("Parsed Input: " + parsedInput);

        final int numberOfBlinks = 75;

        final long answer = calculate(parsedInput, numberOfBlinks);
        return String.valueOf(answer);
    }

    private List<Long> parseInput(final List<String> input) {
        final String[] arrayInput = input.get(0).split(" ");
        return Arrays.stream(arrayInput).map(Long::parseLong).toList();
    }

    private long calculate(
            final List<Long> initialStones,
            final int blinks
    ) {
        List<Long> nextStones = new ArrayList<>(initialStones);
        Map<Long, Long> occurences = new HashMap<>();

        // Initialise the map of occurences.
        for (final Long stone : nextStones) {
            occurences.put(stone, occurences.getOrDefault(stone, 0L) + 1);
        }

        // Now do the blinks.
        for (int i = 0; i < blinks; i++) {
            final Map<Long, Long> nextOccurences = new HashMap<>();
            for (final Map.Entry<Long, Long> e : occurences.entrySet()) {
                nextStones = applyStoneRules(e.getKey());
                for (final Long st : nextStones) {
                    final long occurenceOfNextStone = nextOccurences.getOrDefault(st, 0L) + e.getValue();
                    nextOccurences.put(st, occurenceOfNextStone);
                }
            }
            occurences = nextOccurences;
        }

        log(occurences.toString());

        return occurences.values().stream().mapToLong(l -> l).sum();
    }

    private List<Long> applyStoneRules(final Long l) {
        if (l == 0L) {
            return List.of(1L);
        }

        final List<Long> possiblySplit = splitIfEven(l);
        if (!possiblySplit.isEmpty()) {
            return possiblySplit;
        }

        return List.of(l * 2024L);
    }

    private List<Long> splitIfEven(final Long l) {
        final List<Long> result = new ArrayList<>();
        final String s = String.valueOf(l);
        if (s.length() % 2 == 0) {
            final int halfLength = s.length() / 2;
            result.add(Long.parseLong(s.substring(0, halfLength)));
            result.add(Long.parseLong(s.substring(halfLength)));
        }
        return result;
    }
}
