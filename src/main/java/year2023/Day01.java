package year2023;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/1
 */
public class Day01 extends Day {

    //    private static final String FILE_PATH = "resources/main/resources/year2023/day01/input.txt";

    public String part1(final List<String> input) {
        //        final List<String> input = ImportUtils.readAsList(FILE_PATH);

        final long sum = input.stream()
                .map(line -> findFirstNumber(line) + findLastNumber(line))
                .mapToLong(Long::valueOf)
                .sum();

        return String.valueOf(sum);
    }

    private static String findLastNumber(final String text) {
        final String reverseString = new StringBuilder(text).reverse().toString();
        return findFirstNumber(reverseString);
    }

    private static String findFirstNumber(final String text) {
        for (final char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                return String.valueOf(c);
            }
        }

        return "0";
    }

    public String part2(final List<String> input) {

        //        final List<String> input = ImportUtils.readAsList(FILE_PATH);

        final Map<String, String> pointsTable = createPointsTable();

        final Long result = input.stream()
                .map(line -> extractAndCombineNumbers(line, pointsTable))
                .mapToLong(Long::valueOf)
                .sum();

        return result.toString();
    }

    private String extractAndCombineNumbers(
            final String line,
            final Map<String, String> pointsTable
    ) {
        final SortedMap<Integer, String> matches = new TreeMap<>();

        pointsTable.forEach((key, value) -> {
            int index = line.indexOf(key);
            while (index != -1) {
                matches.put(index, value);
                index = line.indexOf(key, index + 1);
            }
        });

        if (matches.isEmpty()) {
            return "0";
        }

        final String firstNumber = matches.get(matches.firstKey());
        final String lastNumber = matches.get(matches.lastKey());
        return firstNumber + lastNumber;
    }

    private Map<String, String> createPointsTable() {
        final Map<String, String> pointsTable = new HashMap<>();
        pointsTable.put("1", "1");
        pointsTable.put("2", "2");
        pointsTable.put("3", "3");
        pointsTable.put("4", "4");
        pointsTable.put("5", "5");
        pointsTable.put("6", "6");
        pointsTable.put("7", "7");
        pointsTable.put("8", "8");
        pointsTable.put("9", "9");
        pointsTable.put("10", "10");
        pointsTable.put("one", "1");
        pointsTable.put("two", "2");
        pointsTable.put("three", "3");
        pointsTable.put("four", "4");
        pointsTable.put("five", "5");
        pointsTable.put("six", "6");
        pointsTable.put("seven", "7");
        pointsTable.put("eight", "8");
        pointsTable.put("nine", "9");
        return pointsTable;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
