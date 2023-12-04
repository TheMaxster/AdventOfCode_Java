package days.day01;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        part1();
        part2();
    }

    private static void part1() {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day01/input_01.txt";

        int result = 0;

        final List<BigDecimal> bdNumbers = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

                // Solution for problem 1:
                final String firstNumber = findFirstNumber(line);
                final String lastNumber = findLastNumber(line);

                final String number = firstNumber + lastNumber;

                System.out.println(line + " => " + number);

                bdNumbers.add(new BigDecimal(number));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Our result is :" + globalResult);
    }

    private static String findLastNumber(final String text) {
        String reverseString = new StringBuilder(text).reverse().toString();
        return findFirstNumber(reverseString);
    }

    private static String findFirstNumber(final String text) {
        for (char c : text.toCharArray()) {
            if (Character.isDigit(c)) {
                return String.valueOf(c);
            }
        }

        return "0";
    }

    private static void part2() {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day01/input_01.txt";

        final List<BigDecimal> bdNumbers = new ArrayList<>();

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

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {

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

                final Integer min = Collections.min(new ArrayList<>(tmpMap.keySet()));
                final String firstNumber2 = tmpMap.get(min);

                final Integer max = Collections.max(new ArrayList<>(tmpMap.keySet()));
                final String lastNumber2 = tmpMap.get(max);

                final String number = firstNumber2 + lastNumber2;

                System.out.println(line + " => " + number);

                bdNumbers.add(new BigDecimal(number));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String globalResult = bdNumbers.stream().reduce(BigDecimal.ZERO, BigDecimal::add).toString();

        System.out.println("Our result is :" + globalResult);
    }

}