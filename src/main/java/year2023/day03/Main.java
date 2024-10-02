//package main.java.year2023.day03;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Set;
//
//import main.java.utils.ImportUtils;
//
//public class Main {
//
//    private static final List VALID_NUMBERS = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
//    private static final String ASTERISK = "*";
//
//    public static void main(final String[] args) {
//        //  final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day03/input_test_01.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day03/input.txt";
//
//        final String[][] inputArray = ImportUtils.readAsArray(filePath);
//
//        // Part 1
//        final List<Integer> foundValidNumbers = new ArrayList<>();
//        final List<FoundNumber> foundValidNumberDatas = new ArrayList<>();
//
//        for (int lineIndex = 0; lineIndex < inputArray.length; lineIndex++) {
//            for (int columnIndex = 0; columnIndex < inputArray[lineIndex].length; columnIndex++) {
//
//                final String foundNumberAsString = lookForNumber(inputArray, lineIndex, columnIndex, new StringBuilder());
//                if (foundNumberAsString != "") {
//
//                    final int foundNumber = Integer.parseInt(foundNumberAsString);
//
//                    final int indexStart = columnIndex;
//                    final int indexEnd = indexStart + foundNumberAsString.length() - 1;
//
//                    if (!checkIfNumberHasOtherNumberNeighbours(inputArray, indexStart, indexEnd, lineIndex)) {
//
//                        final boolean isValid = checkSurrounding(inputArray, indexStart, indexEnd, lineIndex);
//
//                        if (isValid) {
//                            foundValidNumbers.add(foundNumber);
//                            final FoundNumber foundNumberData = new FoundNumber(indexStart, indexEnd, lineIndex, foundNumber);
//                            foundValidNumberDatas.add(foundNumberData);
//                        }
//
//                    }
//
//                }
//            }
//
//        }
//        System.out.println("FoundValidNumbers: " + foundValidNumbers);
//        final int result = foundValidNumbers.stream().mapToInt(Integer::intValue).sum();
//        System.out.println("Solution Part 1: " + result);
//
//        // Part 2
//        final Map<String, FoundAsterisk> asteriskMap = new HashMap<>();
//
//        for (final FoundNumber foundValidNumberData : foundValidNumberDatas) {
//            checkSurroundingAsterisk(inputArray, foundValidNumberData, asteriskMap);
//        }
//
//        final List<Integer> asteriskSummands = new ArrayList<>();
//        // System.out.println("AsteriskMap:" + asteriskMap);
//        for (final Map.Entry<String, FoundAsterisk> stringFoundAsteriskEntry : asteriskMap.entrySet()) {
//
//            // System.out.println("AsteriskList:" + stringFoundAsteriskEntry.getValue().getNumbers());
//            final List<Integer> numbers = stringFoundAsteriskEntry.getValue().getNumbers();
//            if (numbers.size() == 2) {
//                asteriskSummands.add(numbers.get(0) * numbers.get(1));
//            }
//        }
//
//        final int product = asteriskSummands.stream().mapToInt(Integer::intValue).sum();
//        System.out.println("Solution Part 2: " + product);
//
//    }
//
//    private static String lookForNumber(
//            final String[][] inputArray,
//            final int lineIndex,
//            final int columnIndex,
//            final StringBuilder numberBuilder
//
//    ) {
//        try {
//            final String potentialNumber = inputArray[lineIndex][columnIndex];
//            if (VALID_NUMBERS.contains(potentialNumber)) {
//                //  System.out.println("Found number: " + potentialNumber);
//                numberBuilder.append(inputArray[lineIndex][columnIndex]);
//                return lookForNumber(inputArray, lineIndex, columnIndex + 1, numberBuilder);
//            }
//            return numberBuilder.toString();
//        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
//            return numberBuilder.toString();
//        }
//
//    }
//
//    private static boolean checkIfNumberHasOtherNumberNeighbours(
//            final String[][] array,
//            final int indexStart,
//            final int indexEnd,
//            final int line
//    ) {
//
//        // Check character to the left via parsing.
//        try {
//            final String charToAnalyze = array[line][indexStart - 1];
//            final Integer leftNumber = Integer.parseInt(charToAnalyze);
//            return true;
//
//        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
//            //...
//        }
//
//        // Check character to the right via parsing.
//        try {
//            final String charToAnalyze = array[line][indexEnd + 1];
//            final Integer rightNumber = Integer.parseInt(charToAnalyze);
//            return true;
//
//        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
//            //...
//        }
//
//        return false;
//    }
//
//    private static boolean checkSurrounding(
//            final String[][] array,
//            final int indexStart,
//            final int indexEnd,
//            final int line
//    ) {
//
//        final List<Integer> rangeList = List.of(-1, 0, 1);
//
//        for (int additional = 0; additional <= indexEnd - indexStart; additional++) {
//            for (final Integer rangeI : rangeList) {
//                for (final Integer rangeJ : rangeList) {
//                    final int i = line + rangeJ;
//                    final int j = indexStart + rangeI + additional;
//
//                    try {
//                        final String charToAnalyze = array[i][j];
//
//                        // System.out.println("Check: " + i + " " + j);
//                        if (!VALID_NUMBERS.contains(charToAnalyze) && !Objects.equals(charToAnalyze, ".")) {
//                            // System.out.println("ValidChar: " + charToAnalyze + "(Coords: " + i + " " + j + ")");
//                            return true;
//                        }
//                    } catch (final Exception e) {
//                        //...
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    private static boolean checkSurroundingAsterisk(
//            final String[][] array,
//            final FoundNumber foundValidNumberData,
//            final Map<String, FoundAsterisk> asteriskMap
//    ) {
//        // System.out.println("Check for number: " + foundValidNumberData.getNumber());
//
//        final Integer indexStart = foundValidNumberData.indexStart();
//        final Integer indexEnd = foundValidNumberData.indexEnd();
//        final Integer line = foundValidNumberData.line();
//
//        final List<Integer> rangeList = List.of(-1, 0, 1);
//
//        // Check the surrounding around the whole string.
//        for (int additional = 0; additional <= indexEnd - indexStart; additional++) {
//            for (final Integer rangeI : rangeList) {
//                for (final Integer rangeJ : rangeList) {
//                    final int i = line + rangeJ;
//                    final int j = indexStart + rangeI + additional;
//
//                    try {
//                        final String charToAnalyze = array[i][j];
//
//                        //  System.out.println("Check: " + i + " " + j + " : " + charToAnalyze);
//                        if (Objects.equals(charToAnalyze, ASTERISK)) {
//                            //    System.out.println("Valid Asterisk: " + charToAnalyze + "(Coords: " + i + " " + j + ")");
//
//                            final String key = i + "-" + j;
//                            asteriskMap.computeIfAbsent(key, k -> new FoundAsterisk(i, j));
//                            final FoundAsterisk asterisk = asteriskMap.get(key);
//                            if (!asterisk.getFoundByNumber().contains(foundValidNumberData)) {
//                                asterisk.getNumbers().add(foundValidNumberData.number());
//                                asterisk.getFoundByNumber().add(foundValidNumberData);
//                            }
//
//                        }
//                    } catch (final Exception e) {
//                        //...
//                    }
//                }
//            }
//        }
//
//        return false;
//    }
//
//    /**
//     * Contains all information about the found asterisk.
//     */
//    private static class FoundAsterisk {
//
//        private Set<FoundNumber> foundByNumber;
//        private int line;
//        private int column;
//        private List<Integer> numbers;
//
//        public FoundAsterisk(
//                final int line,
//                final int column
//        ) {
//            this.line = line;
//            this.column = column;
//            this.numbers = new ArrayList<>();
//            this.foundByNumber = new HashSet<>();
//        }
//
//        public Set<FoundNumber> getFoundByNumber() {
//            return foundByNumber;
//        }
//
//        public List<Integer> getNumbers() {
//            return numbers;
//        }
//    }
//
//    /**
//     * Contains all information about the found number.
//     */
//    public record FoundNumber(int indexStart, int indexEnd, int line, int number) {
//
//    }
//
//}
