package year2024.day09;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day09 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day09/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        log(importInput.toString());

        final List<String> stringRep = parseInput(importInput);
        log(stringRep.toString());

        final List<String> convertedInput = convertInput(stringRep);
        log(convertedInput.toString());

        final long product = multiply(convertedInput);

        return String.valueOf(product);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        log(importInput.toString());

        final List<Block> stringRep = parseInputForPart2(importInput);
        log(stringRep.toString());

        final List<String> convertedInput = convertInputForPart2(stringRep);
        log(convertedInput.toString());

        final long product = multiply(convertedInput);

        return String.valueOf(product);
    }

    private List<String> convertInputForPart2(final List<Block> blockList) {

    }

    private List<Block> parseInputForPart2(final List<String> importInput) {
        final String string = importInput.get(0);

        final char[] array = string.toCharArray();
        final List<Block> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            final int value = Integer.parseInt(String.valueOf(array[i]));
            if (i % 2 == 0) {
                result.add(new Block(String.valueOf(i / 2), value));
            } else {
                for (int j = 0; j < value; j++) {
                    result.add(new Block(".", value));
                }
            }
        }

        return result;
    }

    @Data
    @AllArgsConstructor
    private class Block {

        String number;
        Integer amount;
    }


    private long multiply(final List<String> convertedInput) {
        final List<Long> list = convertedInput.stream()
                .filter(s -> !Objects.equals(s, "."))
                .map(Long::parseLong)
                .toList();

        long productSum = 0L;
        for (int i = 0; i < list.size(); i++) {
            productSum += (i * list.get(i));
        }
        return productSum;
    }

    private List<String> convertInput(final List<String> stringList) {
        boolean remainingAreAllDots = false;
        while (!remainingAreAllDots) {
            final int dotIndex = stringList.indexOf(".");
            final int numberIndex = findLastNumberIndex(stringList);

            stringList.set(dotIndex, stringList.get(numberIndex));
            stringList.set(numberIndex, ".");
            log(stringList.toString());

            final int newDotIndex = stringList.indexOf(".");
            remainingAreAllDots = stringList.subList(newDotIndex, stringList.size()).stream()
                    .allMatch(c -> Objects.equals(c, "."));
        }

        return stringList;
    }

    private int findLastNumberIndex(final List<String> stringList) {
        for (int i = stringList.size() - 1; i >= 0; i--) {
            if (!Objects.equals(stringList.get(i), ".")) {
                return i;
            }
        }
        return stringList.size();
    }


    private List<String> parseInput(final List<String> importInput) {
        final String string = importInput.get(0);

        final char[] array = string.toCharArray();
        final List<String> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            final int value = Integer.parseInt(String.valueOf(array[i]));
            if (i % 2 == 0) {
                for (int j = 0; j < value; j++) {
                    result.add(String.valueOf(i / 2));
                }
            } else {
                for (int j = 0; j < value; j++) {
                    result.add(".");
                }
            }
        }

        return result;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
