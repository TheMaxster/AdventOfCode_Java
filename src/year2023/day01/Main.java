package year2023.day01;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;

public class Main {

    private static final String FILE_PATH = "resources/year2023/day01/input.txt";

    public static void main(final String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        final List<String> input = ImportUtils.readAsList(FILE_PATH);

        final int result = 0;

        final List<BigDecimal> bdNumbers = new ArrayList<>();
        for (final String line : input) {
            // Solution for problem 1:
            final String firstNumber = findFirstNumber(line);
            final String lastNumber = findLastNumber(line);

            final String number = firstNumber + lastNumber;

            // System.out.println(line + " => " + number);

            bdNumbers.add(new BigDecimal(number));
        }

        final String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Solution Part 1:" + globalResult);
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

    private static void part2() {

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

        final List<String> input = ImportUtils.readAsList(FILE_PATH);

        final List<BigDecimal> bdNumbers = new ArrayList<>();

        for (final String line : input) {
            // Solution for problem 2:

            final Map<Integer, String> tmpMap = new HashMap<>();

            for (final String key : pointsTable.keySet()) {
                int index = -1;
                do {
                    index += 1;
                    index = line.indexOf(key, index);
                    if (index != -1) {
                        tmpMap.put(index, pointsTable.get(key));
                    }
                } while (index != -1);
            }

            final Integer min = Collections.min(tmpMap.keySet());
            final String firstNumber2 = tmpMap.get(min);

            final Integer max = Collections.max(tmpMap.keySet());
            final String lastNumber2 = tmpMap.get(max);

            final String number = firstNumber2 + lastNumber2;

            //      System.out.println(line + " => " + number);

            bdNumbers.add(new BigDecimal(number));
        }

        final String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Solution Part 2:" + globalResult);
    }

}
