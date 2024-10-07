package year2023.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import application.Day;
import utils.ArrayUtils;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/11
 */
public class Day11 extends Day {

    private static int TOP_TO_BOTTOM;
    private static int LEFT_TO_RIGHT;

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final String[][] inputArray = utils.ImportUtils.convertListToArray(input);
        return calculateDistance(input, inputArray, 2);
    }

    @Override
    public String part2(final List<String> input) {
        final String[][] inputArray = utils.ImportUtils.convertListToArray(input);
        return calculateDistance(input, inputArray, 1000000);
    }

    private String calculateDistance(
            final List<String> input,
            final String[][] inputArray,
            final int expansionFactorInt
    ) {
        TOP_TO_BOTTOM = inputArray.length;
        LEFT_TO_RIGHT = inputArray[0].length;
        log("TopToBottom: " + TOP_TO_BOTTOM);
        log("LeftToRight: " + LEFT_TO_RIGHT);

        final List<Integer> emptyRows = new ArrayList<>();
        final List<Integer> emptyColumns = new ArrayList<>();

        final String[][] expandedArray = expandGalaxy(input, emptyRows, emptyColumns);

        // Print the expanded array for verification
        for (final String[] row : expandedArray) {
            log(Arrays.toString(row));
        }

        TOP_TO_BOTTOM = expandedArray.length;
        LEFT_TO_RIGHT = expandedArray[0].length;

        log("TopToBottom: " + TOP_TO_BOTTOM);
        log("LeftToRight: " + LEFT_TO_RIGHT);

        int counter = 0;
        final List<Coordinate> foundHashes = new ArrayList<>();
        for (int row = 0; row < TOP_TO_BOTTOM; row++) {
            for (int column = 0; column < LEFT_TO_RIGHT; column++) {
                if (Objects.equals(expandedArray[row][column], "#")) {
                    counter++;
                    foundHashes.add(new Coordinate(row, column, counter));
                }
            }
        }

        log("We found: " + foundHashes.size());

        int pairs = 0;
        for (int i = foundHashes.size() - 1; i > 0; i--) {
            pairs += i;
        }

        log("So we have pairs: " + pairs);

        final List<Long> allDistances = new ArrayList<>();
        for (int i = 0; i < foundHashes.size(); i++) {
            for (int j = i + 1; j < foundHashes.size(); j++) {
                final Coordinate start = foundHashes.get(i);
                final Coordinate target = foundHashes.get(j);
                if (start != target) {
                    //                    int rowDistance = Math.abs(target.row() - start.row());
                    //                    int columnDistance = Math.abs(target.column() - start.column());
                    final long expansionFactor = expansionFactorInt - 1;

                    final long minRow = Math.min(start.row(), target.row());
                    final long maxRow = Math.max(start.row(), target.row());

                    int rowMultiplier = 0;
                    for (final Integer emptyRow : emptyRows) {
                        if (emptyRow > minRow && emptyRow < maxRow) {
                            rowMultiplier++;
                        }
                    }
                    final long rowDistance = maxRow - minRow + rowMultiplier * expansionFactor;

                    final long minColumn = Math.min(start.column(), target.column());
                    final long maxColumn = Math.max(start.column(), target.column());

                    long columnMultiplier = 0;
                    for (final Integer emptyColumn : emptyColumns) {
                        if (emptyColumn > minColumn && emptyColumn < maxColumn) {
                            columnMultiplier++;
                        }
                    }
                    final long columnDistance = maxColumn - minColumn + columnMultiplier * expansionFactor;

                    final long totalDistance = columnDistance + rowDistance;

                    log(start.counter() + " to " + target.counter() + ": " + totalDistance);
                    allDistances.add(totalDistance);
                }
            }
        }

        return ListUtils.sumUpLong(allDistances).toString();
    }

    private record Coordinate(int row, int column, int counter) {

    }

    private static String[][] expandGalaxy(
            final List<String> inputList,
            final List<Integer> emptyRows,
            final List<Integer> emptyColumns
    ) {

        final int expansionFactor = 0;

        final List<String> expandedToOneDimension = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            final String line = inputList.get(i);
            if (!line.contains("#")) {
                emptyRows.add(i);
            }
            expandedToOneDimension.add(line);
        }

        final String[][] tmpArray = listToArray(expandedToOneDimension);
        final String[][] tmpArray2 = ArrayUtils.transpose(tmpArray);
        final List<String> inputList2 = arrayToList(tmpArray2);

        final List<String> expandedToTwoDimensions = new ArrayList<>();
        for (int i = 0; i < inputList2.size(); i++) {
            final String line = inputList2.get(i);
            if (!line.contains("#")) {
                emptyColumns.add(i);
            }
            expandedToTwoDimensions.add(line);
        }

        final String[][] tmpArray3 = listToArray(expandedToTwoDimensions);
        return ArrayUtils.transpose(tmpArray3);
    }

    private static String[][] listToArray(final List<String> stringList) {
        final int numRows = stringList.size();
        final int numCols = stringList.get(0).length(); // Annahme: Alle Zeichenketten haben die gleiche Länge

        final String[][] stringArray = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            stringArray[i] = stringList.get(i).split(""); // Zerlege die Zeichenkette in ein Array von Zeichen
        }

        return stringArray;
    }

    private static List<String> arrayToList(final String[][] stringArray) {
        final List<String> stringList = new ArrayList<>();

        for (final String[] row : stringArray) {
            final StringBuilder stringBuilder = new StringBuilder();
            for (final String cell : row) {
                stringBuilder.append(cell);
            }
            stringList.add(stringBuilder.toString());
        }

        return stringList;
    }

}
