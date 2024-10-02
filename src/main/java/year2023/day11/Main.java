//package main.java.year2023.day11;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main {
//
//    private static int TOP_TO_BOTTOM;
//    private static int LEFT_TO_RIGHT;
//
//    public static void main(final String[] args) {
//        // final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day11/input_test_01.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day11/input.txt";
//
//        final String[][] inputArray = ImportUtils.readAsArray(filePath);
//        TOP_TO_BOTTOM = inputArray.length;
//        LEFT_TO_RIGHT = inputArray[0].length;
//        ArrayUtils.log("TopToBottom: " + TOP_TO_BOTTOM);
//        ArrayUtils.log("LeftToRight: " + LEFT_TO_RIGHT);
//
//        final List<String> inputList = ImportUtils.readAsList(filePath);
//
//        //
//        //      for(int i = TOP_TO_BOTTOM )
//        //
//        //    }
//
//        final List<Integer> emptyRows = new ArrayList<>();
//        final List<Integer> emptyColumns = new ArrayList<>();
//
//        final String[][] expandedArray = expandGalaxy(inputList, emptyRows, emptyColumns);
//
//        // Print the expanded array for verification
//        for (final String[] row : expandedArray) {
//            System.out.println(Arrays.toString(row));
//        }
//
//        TOP_TO_BOTTOM = expandedArray.length;
//        LEFT_TO_RIGHT = expandedArray[0].length;
//
//        ArrayUtils.log("TopToBottom: " + TOP_TO_BOTTOM);
//        ArrayUtils.log("LeftToRight: " + LEFT_TO_RIGHT);
//
//        int counter = 0;
//        final List<Coordinate> foundHashes = new ArrayList<>();
//        for (int row = 0; row < TOP_TO_BOTTOM; row++) {
//            for (int column = 0; column < LEFT_TO_RIGHT; column++) {
//                if (Objects.equals(expandedArray[row][column], "#")) {
//                    counter++;
//                    foundHashes.add(new Coordinate(row, column, counter));
//                }
//            }
//        }
//
//        ArrayUtils.log("We found: " + foundHashes.size());
//
//        int pairs = 0;
//        for (int i = foundHashes.size() - 1; i > 0; i--) {
//            pairs += i;
//        }
//
//        ArrayUtils.log("So we have pairs: " + pairs);
//
//        final List<Long> allDistances = new ArrayList<>();
//        for (int i = 0; i < foundHashes.size(); i++) {
//            for (int j = i + 1; j < foundHashes.size(); j++) {
//                final Coordinate start = foundHashes.get(i);
//                final Coordinate target = foundHashes.get(j);
//                if (start != target) {
//                    //                    int rowDistance = Math.abs(target.row() - start.row());
//                    //                    int columnDistance = Math.abs(target.column() - start.column());
//                    final long expansionFactor = 1000000 - 1;
//
//                    final long minRow = Math.min(start.row(), target.row());
//                    final long maxRow = Math.max(start.row(), target.row());
//
//                    int rowMultiplier = 0;
//                    for (final Integer emptyRow : emptyRows) {
//                        if (emptyRow > minRow && emptyRow < maxRow) {
//                            rowMultiplier++;
//                        }
//                    }
//                    final long rowDistance = maxRow - minRow + rowMultiplier * expansionFactor;
//
//                    final long minColumn = Math.min(start.column(), target.column());
//                    final long maxColumn = Math.max(start.column(), target.column());
//
//                    long columnMultiplier = 0;
//                    for (final Integer emptyColumn : emptyColumns) {
//                        if (emptyColumn > minColumn && emptyColumn < maxColumn) {
//                            columnMultiplier++;
//                        }
//                    }
//                    final long columnDistance = maxColumn - minColumn + columnMultiplier * expansionFactor;
//
//                    final long totalDistance = columnDistance + rowDistance;
//
//                    ArrayUtils.log(start.counter() + " to " + target.counter() + ": " + totalDistance);
//                    allDistances.add(totalDistance);
//                }
//            }
//        }
//
//        final long sumAllDistances = allDistances.stream().reduce((a, b) -> (a + b)).get();
//        ArrayUtils.log("Sum of all distances: " + sumAllDistances);
//
//
//    }
//
//    private record Coordinate(int row, int column, int counter) {
//
//    }
//
//    private static String[][] expandGalaxy(
//            final List<String> inputList,
//            final List<Integer> emptyRows,
//            final List<Integer> emptyColumns
//    ) {
//
//        final int expansionFactor = 0;
//
//        final List<String> expandedToOneDimension = new ArrayList<>();
//        for (int i = 0; i < inputList.size(); i++) {
//            final String line = inputList.get(i);
//            if (!line.contains("#")) {
//                emptyRows.add(i);
//                for (int exp = 0; exp < expansionFactor; exp++) {
//                    expandedToOneDimension.add(line);
//                }
//            }
//            {
//                expandedToOneDimension.add(line);
//            }
//        }
//
//        final String[][] tmpArray = listToArray(expandedToOneDimension);
//        final String[][] tmpArray2 = transpose(tmpArray);
//        final List<String> inputList2 = arrayToList(tmpArray2);
//
//        final List<String> expandedToTwoDimensions = new ArrayList<>();
//        for (int i = 0; i < inputList2.size(); i++) {
//            final String line = inputList2.get(i);
//            if (!line.contains("#")) {
//                emptyColumns.add(i);
//                for (int exp = 0; exp < expansionFactor; exp++) {
//                    expandedToTwoDimensions.add(line);
//                }
//            }
//            {
//                expandedToTwoDimensions.add(line);
//            }
//        }
//
//        final String[][] tmpArray3 = listToArray(expandedToTwoDimensions);
//        return transpose(tmpArray3);
//    }
//
//    private static String[][] listToArray(final List<String> stringList) {
//        final int numRows = stringList.size();
//        final int numCols = stringList.get(0).length(); // Annahme: Alle Zeichenketten haben die gleiche Länge
//
//        final String[][] stringArray = new String[numRows][numCols];
//
//        for (int i = 0; i < numRows; i++) {
//            stringArray[i] = stringList.get(i).split(""); // Zerlege die Zeichenkette in ein Array von Zeichen
//        }
//
//        return stringArray;
//    }
//
//    private static List<String> arrayToList(final String[][] stringArray) {
//        final List<String> stringList = new ArrayList<>();
//
//        for (final String[] row : stringArray) {
//            final StringBuilder stringBuilder = new StringBuilder();
//            for (final String cell : row) {
//                stringBuilder.append(cell);
//            }
//            stringList.add(stringBuilder.toString());
//        }
//
//        return stringList;
//    }
//
//    private static String[][] transpose(final String[][] originalArray) {
//        final int rows = originalArray.length;
//        final int columns = originalArray[0].length;
//
//        final String[][] transposedArray = new String[columns][rows];
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < columns; j++) {
//                transposedArray[j][i] = originalArray[i][j];
//            }
//        }
//
//        return transposedArray;
//    }
//
//}
