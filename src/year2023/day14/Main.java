package year2023.day14;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(final String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/year2023/day14/input_test_01.txt";
        // final String filePath = System.getProperty("user.dir") + "/resources/year2023/day14/input_test_01_02.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day14/input.txt";

        final String[][] importArray = ImportUtils.readAsArray(filePath);
        final String[][] transposedImportArray = Utils.transpose(importArray);

        // Part 1
        final String[][] resultPart1 = calculatePart1(transposedImportArray);
        final String[][] retransposedImportArrayForPart1 = Utils.transpose(resultPart1);
        Utils.printMap(retransposedImportArrayForPart1);
        final int totalPointsForAllRowsPart1 = calculateTotalPointsForMap(retransposedImportArrayForPart1);
        Utils.log("Part 1: Total points: " + totalPointsForAllRowsPart1);

        // Part 2
        final Map<String, Long> cacheMap = new HashMap<>();
        final long turnsToMake = 1000000000;

        String[][] tmpArray = transposedImportArray;

        long leftOverTurnsToMake = 0;
        for (long i = 0; i < turnsToMake; i++) {

            final String hash = Utils.generateHash(tmpArray);
            final Long foundIndex = cacheMap.getOrDefault(hash, null);
            if (foundIndex != null) {
                final long cycleLength = i - foundIndex;
                final long remainingCycles = (turnsToMake - i + 1) % cycleLength;
                leftOverTurnsToMake = remainingCycles - 1;
                break;
            }

            cacheMap.put(hash, i);
            tmpArray = turnOneCycle(tmpArray);
        }

        // Do the leftover cycles.
        for (long i = 0; i < leftOverTurnsToMake; i++) {
            tmpArray = turnOneCycle(tmpArray);
        }

        final String[][] retransposedImportArray = Utils.transpose(tmpArray);
        Utils.printMap(retransposedImportArray);

        final int totalPointsForAllRowsPart2 = calculateTotalPointsForMap(retransposedImportArray);
        Utils.log("Part 2: Total points: " + totalPointsForAllRowsPart2);

    }

    private static Integer calculateTotalPointsForMap(final String[][] map) {
        int totalPoints = 0;
        int points = map.length;
        for (final String[] column : map) {
            for (int i = column.length - 1; i >= 0; i--) {
                if (Objects.equals(column[i], "O")) {
                    totalPoints += points;
                }
            }
            points--;
        }
        return totalPoints;
    }

    private static String[][] turnOneCycle(final String[][] transposedImportArray) {
        final String[][] tiltedNorth = tiltArrayNorth(transposedImportArray);
        //     Utils.printMap(Utils.transpose(tiltedNorth.northTiltedArray()));
        final String[][] tiltedWest = tiltArrayNorth(Utils.rotateLeft(tiltedNorth));
        //   Utils.printMap(Utils.transpose(Utils.rotateRight(tiltedWest.northTiltedArray())));
        final String[][] tiltedSouth = tiltArrayNorth(Utils.rotateLeft(tiltedWest));
        // Utils.printMap(Utils.transpose(Utils.rotateRight(Utils.rotateLeft(tiltedSouth.northTiltedArray()))));
        final String[][] tiltedEast = tiltArrayNorth(Utils.rotateLeft(tiltedSouth));
        //  Utils.printMap(Utils.transpose(Utils.rotateRight(Utils.rotateRight(Utils.rotateRight(tiltedEast.northTiltedArray())))));

        return Utils.rotateLeft(tiltedEast); // Get back in north position.
    }

    private static String[][] calculatePart1(final String[][] transposedImportArray) {
        return tiltArrayNorth(transposedImportArray);
    }

    private static String[][] tiltArrayNorth(final String[][] transposedImportArray) {
        final String[][] northTiltedArray = new String[transposedImportArray.length][transposedImportArray[0].length];
        for (int j = 0; j < transposedImportArray.length; j++) {
            final String[] column = transposedImportArray[j];
            final List<Integer> pointsForRow = calculateNewPointsForColumn(column);
            final String[] newColumn = createNewColumn(column, pointsForRow);
            northTiltedArray[j] = newColumn;
        }
        return northTiltedArray;
    }

    private static List<Integer> calculateNewPointsForColumn(final String[] column) {
        final List<Integer> newPointsForColumn = new ArrayList<>();

        int maxPoints = column.length;

        for (int i = 0; i < column.length; i++) {
            if (Objects.equals(column[i], ".")) {
                // do nothing
            } else if (Objects.equals(column[i], "O")) {
                newPointsForColumn.add(maxPoints);
                maxPoints--;
            } else if (Objects.equals(column[i], "#")) {
                maxPoints = column.length - i - 1;
            }
        }
        return newPointsForColumn;
    }

    private static String[] createNewColumn(
            final String[] column,
            final List<Integer> pointsForRow
    ) {
        final String[] newColumn = Arrays.copyOf(column, column.length);

        for (int i = column.length - 1; i >= 0; i--) {
            if (Objects.equals(newColumn[i], "O")) {
                newColumn[i] = ".";
            }
        }
        for (int i = column.length; i >= 0; i--) {
            if (pointsForRow.contains(i)) {
                newColumn[column.length - i] = "O";
            }
        }

        return newColumn;
    }

}
