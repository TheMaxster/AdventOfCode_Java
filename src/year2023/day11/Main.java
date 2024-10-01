package year2023.day11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    private static int TOP_TO_BOTTOM;
    private static int LEFT_TO_RIGHT;

    public static void main(String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day11/input_11_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day11/input_11.txt";

        String[][] inputArray = ImportUtils.readAsArray(filePath);
        TOP_TO_BOTTOM = inputArray.length;
        LEFT_TO_RIGHT = inputArray[0].length;
        Utils.log("TopToBottom: " + TOP_TO_BOTTOM);
        Utils.log("LeftToRight: " + LEFT_TO_RIGHT);

        List<String> inputList = ImportUtils.readAsList(filePath);

        //
        //      for(int i = TOP_TO_BOTTOM )
        //
        //    }

        List<Integer> emptyRows = new ArrayList<>();
        List<Integer> emptyColumns = new ArrayList<>();

        String[][] expandedArray = expandGalaxy(inputList, emptyRows, emptyColumns);

        // Print the expanded array for verification
        for (String[] row : expandedArray) {
            System.out.println(Arrays.toString(row));
        }

        TOP_TO_BOTTOM = expandedArray.length;
        LEFT_TO_RIGHT = expandedArray[0].length;

        Utils.log("TopToBottom: " + TOP_TO_BOTTOM);
        Utils.log("LeftToRight: " + LEFT_TO_RIGHT);

        int counter = 0;
        List<Coordinate> foundHashes = new ArrayList<>();
        for (int row = 0; row < TOP_TO_BOTTOM; row++) {
            for (int column = 0; column < LEFT_TO_RIGHT; column++) {
                if (Objects.equals(expandedArray[row][column], "#")) {
                    counter++;
                    foundHashes.add(new Coordinate(row, column, counter));
                }
            }
        }

        Utils.log("We found: " + foundHashes.size());

        int pairs = 0;
        for (int i = foundHashes.size() - 1; i > 0; i--) {
            pairs += i;
        }

        Utils.log("So we have pairs: " + pairs);

        List<Long> allDistances = new ArrayList<>();
        for (int i = 0; i < foundHashes.size(); i++) {
            for (int j = i + 1; j < foundHashes.size(); j++) {
                Coordinate start = foundHashes.get(i);
                Coordinate target = foundHashes.get(j);
                if (start != target) {
                    //                    int rowDistance = Math.abs(target.row() - start.row());
                    //                    int columnDistance = Math.abs(target.column() - start.column());
                    long expansionFactor = 1000000-1;

                    long minRow = Math.min(start.row(), target.row());
                    long maxRow = Math.max(start.row(), target.row());

                    int rowMultiplier = 0;
                    for (Integer emptyRow : emptyRows) {
                        if (emptyRow > minRow && emptyRow < maxRow) {
                            rowMultiplier++;
                        }
                    }
                    long rowDistance = maxRow - minRow + rowMultiplier * expansionFactor;

                    long minColumn = Math.min(start.column(), target.column());
                    long maxColumn = Math.max(start.column(), target.column());

                    long columnMultiplier = 0;
                    for (Integer emptyColumn : emptyColumns) {
                        if (emptyColumn > minColumn && emptyColumn < maxColumn) {
                            columnMultiplier++;
                        }
                    }
                    long columnDistance = maxColumn - minColumn + columnMultiplier * expansionFactor;

                    long totalDistance = columnDistance + rowDistance;

                    Utils.log(start.counter() + " to " + target.counter() + ": " + totalDistance);
                    allDistances.add(totalDistance);
                }
            }
        }

        long sumAllDistances = allDistances.stream().reduce((a, b) -> (a + b)).get();
        Utils.log("Sum of all distances: " + sumAllDistances);


    }

    private record Coordinate(int row, int column, int counter) {

    }

    private static String[][] expandGalaxy(
            final List<String> inputList,
            List<Integer> emptyRows,
            List<Integer> emptyColumns
    ) {

        int expansionFactor = 0;

        List<String> expandedToOneDimension = new ArrayList<>();
        for (int i = 0; i < inputList.size(); i++) {
            String line = inputList.get(i);
            if (!line.contains("#")) {
                emptyRows.add(i);
                for (int exp = 0; exp < expansionFactor; exp++) {
                    expandedToOneDimension.add(line);
                }
            }
            {
                expandedToOneDimension.add(line);
            }
        }

        String[][] tmpArray = listToArray(expandedToOneDimension);
        String[][] tmpArray2 = transpose(tmpArray);
        List<String> inputList2 = arrayToList(tmpArray2);

        List<String> expandedToTwoDimensions = new ArrayList<>();
        for (int i = 0; i < inputList2.size(); i++) {
            String line = inputList2.get(i);
            if (!line.contains("#")) {
                emptyColumns.add(i);
                for (int exp = 0; exp < expansionFactor; exp++) {
                    expandedToTwoDimensions.add(line);
                }
            }
            {
                expandedToTwoDimensions.add(line);
            }
        }

        String[][] tmpArray3 = listToArray(expandedToTwoDimensions);
        return transpose(tmpArray3);
    }

    private static String[][] listToArray(List<String> stringList) {
        int numRows = stringList.size();
        int numCols = stringList.get(0).length(); // Annahme: Alle Zeichenketten haben die gleiche Länge

        String[][] stringArray = new String[numRows][numCols];

        for (int i = 0; i < numRows; i++) {
            stringArray[i] = stringList.get(i).split(""); // Zerlege die Zeichenkette in ein Array von Zeichen
        }

        return stringArray;
    }

    private static List<String> arrayToList(String[][] stringArray) {
        List<String> stringList = new ArrayList<>();

        for (String[] row : stringArray) {
            StringBuilder stringBuilder = new StringBuilder();
            for (String cell : row) {
                stringBuilder.append(cell);
            }
            stringList.add(stringBuilder.toString());
        }

        return stringList;
    }

    private static String[][] transpose(String[][] originalArray) {
        int rows = originalArray.length;
        int columns = originalArray[0].length;

        String[][] transposedArray = new String[columns][rows];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                transposedArray[j][i] = originalArray[i][j];
            }
        }

        return transposedArray;
    }

}
