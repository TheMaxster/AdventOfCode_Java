package year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/25
 */
public class Day25 extends Day {


    private static final String FILE_PATH = "src/main/resources/year2024/day25/input_test_01.txt";

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int result = countValidPairs(parseInput(input));
        return String.valueOf(result);
    }

    public String part2(final List<String> input) {
        return "N/A";
    }

    private Map<String, List<int[]>> parseInput(final List<String> input) {
        final List<String[][]> locks = new ArrayList<>();
        final List<String[][]> keys = new ArrayList<>();

        for (int i = 0; i < input.size(); i = i + 8) {
            log("Line: " + i);
            final String line = input.get(i);

            if (line.isEmpty()) {
                continue;
            }

            final List<String> object = new ArrayList<>();

            for (int j = 0; j < 7; j++) {
                object.add(input.get(i + j));
            }
            final String[][] objectArray = ImportUtils.convertListToArray(object);

            if (line.equals("#####")) {
                keys.add(objectArray);
            } else if (line.equals(".....")) {
                locks.add(objectArray);
            }
        }

        final Map<String, List<int[]>> result = new HashMap<>();

        final List<int[]> lockHeights = locks.stream().map(Day25::convertToHeights).toList();
        final List<int[]> keyHeights = keys.stream().map(Day25::convertToHeights).toList();

        result.put("locks", lockHeights);
        result.put("keys", keyHeights);
        return result;
    }

    private static int[] convertToHeights(final String[][] schematic) {
        final int width = schematic[0].length;
        final int[] heights = new int[width];
        Arrays.fill(heights, 0);

        for (int col = 0; col < width; col++) {
            for (int row = 0; row < schematic.length; row++) {
                final String ch = schematic[row][col];
                if (Objects.equals(ch, "#")) {
                    heights[col] = heights[col] + 1;
                }
            }
        }
        return heights;
    }

    private int countValidPairs(final Map<String, List<int[]>> schematics) {
        final List<int[]> locks = schematics.get("locks");
        final List<int[]> keys = schematics.get("keys");

        return (int) locks.stream()
                .flatMap(lock -> keys.stream().filter(key -> isValidPair(lock, key)))
                .count();
    }

    private static boolean isValidPair(
            final int[] lock,
            final int[] key
    ) {
        for (int i = 0; i < lock.length; i++) {
            if (lock[i] + key[i] > 7) { // Exceeds the available space
                return false;
            }
        }
        return true;
    }

}
