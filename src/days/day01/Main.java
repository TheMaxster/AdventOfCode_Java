package days.day01;

import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;

public class Main {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        String filePath = Path.of("resources/days/day01/input_01.txt").toString();
        List<String> input = ImportUtils.readAsList(filePath);

        int result = 0;

        List<BigDecimal> bdNumbers = new ArrayList<>();
        for (String line : input) {
            // Solution for problem 1:
            String firstNumber = findFirstNumber(line);
            String lastNumber = findLastNumber(line);

            String number = firstNumber + lastNumber;

            // System.out.println(line + " => " + number);

            bdNumbers.add(new BigDecimal(number));
        }

        String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Solution Part 1:" + globalResult);
    }

    private static String findLastNumber(String text) {
        String reverseString = new StringBuilder(text).reverse().toString();
        return findFirstNumber(reverseString);
    }

    private static String findFirstNumber(String text) {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                return String.valueOf(c);
            }
        }

        return "0";
    }

    private static void part2() {
        String filePath = System.getProperty("user.dir") + "/resources/days/day01/input_01.txt";

        Map<String, String> pointsTable = new HashMap<>();
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

        List<String> input = ImportUtils.readAsList(filePath);

        List<BigDecimal> bdNumbers = new ArrayList<>();

        for (String line : input) {
            // Solution for problem 2:

            Map<Integer, String> tmpMap = new HashMap<>();

            for (String key : pointsTable.keySet()) {
                int index = -1;
                do {
                    index += 1;
                    index = line.indexOf(key, index);
                    if (index != -1) {
                        tmpMap.put(index, pointsTable.get(key));
                    }
                } while (index != -1);
            }

            Integer min = Collections.min(tmpMap.keySet());
            String firstNumber2 = tmpMap.get(min);

            Integer max = Collections.max(tmpMap.keySet());
            String lastNumber2 = tmpMap.get(max);

            String number = firstNumber2 + lastNumber2;

      //      System.out.println(line + " => " + number);

            bdNumbers.add(new BigDecimal(number));
        }

        String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Solution Part 2:" + globalResult);
    }

}