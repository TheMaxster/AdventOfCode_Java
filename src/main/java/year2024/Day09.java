package year2024;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day09 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day09/input_test_01.txt";

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        log(importInput.toString());

        final List<Block> parsedInput = parseInputForPart1(input);
        log(createLoggableBlocklist(parsedInput));

        final List<Block> convertedInput = convertInput(parsedInput);
        log(createLoggableBlocklist(convertedInput));

        final long product = multiplyBlocks(convertedInput);

        return String.valueOf(product);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        log(input.toString());

        final List<Block> parsedInput = parseInputForPart2(input);
        log(createLoggableBlocklist(parsedInput));

        final List<Block> convertedInput = convertInput(parsedInput);
        log(createLoggableBlocklist(convertedInput));

        final long product = multiplyBlocks(convertedInput);

        return String.valueOf(product);
    }

    private List<Block> parseInputForPart1(final List<String> importInput) {
        final String string = importInput.get(0);

        final char[] array = string.toCharArray();
        final List<Block> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            final int value = Integer.parseInt(String.valueOf(array[i]));
            if (i % 2 == 0) {
                for (int j = 0; j < value; j++) {
                    result.add(new Block(String.valueOf(i / 2), 1, false));
                }
            } else {
                for (int j = 0; j < value; j++) {
                    result.add(new Block(".", 1, false));
                }
            }
        }

        return result;
    }

    private List<Block> parseInputForPart2(final List<String> importInput) {
        final String string = importInput.get(0);

        final char[] array = string.toCharArray();
        final List<Block> result = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            final int value = Integer.parseInt(String.valueOf(array[i]));
            if (i % 2 == 0) {
                result.add(new Block(String.valueOf(i / 2), value, false));
            } else {
                result.add(new Block(".", value, false));
            }
        }

        return result;
    }

    private List<Block> convertInput(final List<Block> blockList) {
        log(createLoggableBlocklist(blockList));
        while (true) {

            final int numberIndex = findUnvisitedLastNumberIndexForBlocks(blockList);
            if (numberIndex == -1) {
                break;
            }

            final Block numberBlock = blockList.get(numberIndex);
            numberBlock.setVisited(true);
            final int dotIndex = findDotIndexForBlock(blockList, numberBlock.getAmount());

            if (dotIndex > numberIndex || dotIndex == -1) {
                continue;
            }

            final Block dotBlock = blockList.get(dotIndex);
            final int newDots = dotBlock.getAmount() - numberBlock.getAmount();
            dotBlock.setAmount(newDots);
            blockList.add(dotIndex, numberBlock);
            blockList.remove(numberIndex + 1);
            blockList.add(numberIndex, new Block(".", numberBlock.getAmount(), false));
            log(createLoggableBlocklist(blockList));
        }

        return blockList;
    }

    private String createLoggableBlocklist(final List<Block> blockList) {
        final StringBuilder s = new StringBuilder();
        for (final Block block : blockList) {
            s.append((block.getNumber()).repeat(block.getAmount()));
        }
        return s.toString();
    }

    private int findDotIndexForBlock(
            final List<Block> blockList,
            final int blockAmount
    ) {
        for (int i = 0; i < blockList.size(); i++) {
            final Block dotBlock = blockList.get(i);
            if (StringUtils.equals(dotBlock.getNumber(), ".") && blockAmount <= dotBlock.getAmount()) {
                return i;
            }
        }
        return -1;
    }


    private int findUnvisitedLastNumberIndexForBlocks(final List<Block> stringList) {
        for (int i = stringList.size() - 1; i >= 0; i--) {
            final Block block = stringList.get(i);
            if (!StringUtils.equals(block.getNumber(), ".") && !block.isVisited()) {
                return i;
            }
        }
        return -1;
    }

    private long multiplyBlocks(final List<Block> convertedInput) {
        final List<Long> list = new ArrayList<>();
        for (final Block block : convertedInput) {
            for (int i = 0; i < block.getAmount(); i++) {
                list.add(block.getNumber().equals(".")
                        ? 0L
                        : Long.parseLong(block.getNumber())
                );
            }
        }

        long productSum = 0L;
        for (int i = 0; i < list.size(); i++) {
            productSum += (i * list.get(i));
        }
        return productSum;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Data
    @AllArgsConstructor
    private static class Block {

        String number;
        Integer amount;
        boolean visited;
    }
}
