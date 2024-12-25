package year2024;

import java.util.Arrays;
import java.util.List;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/2
 */
public class Day02 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day02/input_test_01.txt";

    public String part1(final List<String> input) {
        //   final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final Integer[][] importArray = ImportUtils.convertListToIntArray(input, " ");

        final long countValidLevels = Arrays.stream(importArray)
                .filter(this::isLevelValid)
                .count();

        return String.valueOf(countValidLevels);
    }

    private boolean isLevelValid(final Integer[] levels) {
        boolean levelValid = true;
        final boolean ascending = levels[1] - levels[0] > 0;

        if (ascending) {
            for (int i = 0; i < levels.length - 1; i++) {
                final int diff = levels[i + 1] - levels[i];
                if (!(diff > 0 && diff <= 3)) {
                    levelValid = false;
                    break;
                }
            }
        } else { // descending
            for (int i = 0; i < levels.length - 1; i++) {
                final int diff = levels[i + 1] - levels[i];
                if (!(diff < 0 && diff >= -3)) {
                    levelValid = false;
                    break;
                }
            }
        }

        log(Arrays.stream(levels).toList() + " " + levelValid);
        return levelValid;
    }

    public String part2(final List<String> input) {
        // final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final Integer[][] importArray = ImportUtils.convertListToIntArray(input, " ");

        final long countValidLevels = Arrays.stream(importArray)
                .filter(this::isLevelValidWithSkip)
                .count();

        return String.valueOf(countValidLevels);
    }

    private boolean isLevelValidWithSkip(final Integer[] levels) {
        boolean levelValid = isLevelValid(levels);

        if (!levelValid) {
            for (int skipIndex = 0; skipIndex < levels.length; skipIndex++) {
                final Integer[] subArray = createSubArray(levels, skipIndex);

                levelValid = isLevelValid(subArray);
                if (levelValid) {
                    break;
                }
            }
        }
        return levelValid;
    }

    private static Integer[] createSubArray(
            final Integer[] array,
            final int skipIndex
    ) {
        final Integer[] subArray = new Integer[array.length - 1];
        int subIndex = 0;
        for (int i = 0; i < array.length; i++) {
            if (i == skipIndex) {
                continue;
            }
            subArray[subIndex++] = array[i];
        }
        return subArray;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
