package year2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import application.Day;
import utils.ImportUtils;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/3
 */
public class Day03 extends Day {

    private static final List<String> VALID_NUMBERS = List.of("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");
    private static final String ASTERISK = "*";

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final String[][] inputArray = ImportUtils.convertListToArray(input);

        final List<FoundNumber> foundValidNumberDatas = determineValidNumberDatas(inputArray);

        final List<Integer> foundValidNumbers = foundValidNumberDatas.stream().map(FoundNumber::number).toList();
        log("FoundValidNumbers: " + foundValidNumbers);
        return ListUtils.sumUpInt(foundValidNumbers).toString();
    }

    @Override
    public String part2(final List<String> input) {
        final String[][] inputArray = ImportUtils.convertListToArray(input);
        final Map<String, FoundAsterisk> asteriskMap = new HashMap<>();

        final List<FoundNumber> foundValidNumberDatas = determineValidNumberDatas(inputArray);
        for (final FoundNumber foundValidNumberData : foundValidNumberDatas) {
            checkSurroundingAsterisk(inputArray, foundValidNumberData, asteriskMap);
        }

        final List<Integer> asteriskSummands = new ArrayList<>();
        // log("AsteriskMap:" + asteriskMap);
        for (final Map.Entry<String, FoundAsterisk> stringFoundAsteriskEntry : asteriskMap.entrySet()) {

            // log("AsteriskList:" + stringFoundAsteriskEntry.getValue().getNumbers());
            final List<Integer> numbers = stringFoundAsteriskEntry.getValue().getNumbers();
            if (numbers.size() == 2) {
                asteriskSummands.add(numbers.get(0) * numbers.get(1));
            }
        }

        return ListUtils.sumUpInt(asteriskSummands).toString();
    }

    private static List<FoundNumber> determineValidNumberDatas(final String[][] inputArray) {
        final List<FoundNumber> foundValidNumberDatas = new ArrayList<>();

        for (int lineIndex = 0; lineIndex < inputArray.length; lineIndex++) {
            for (int columnIndex = 0; columnIndex < inputArray[lineIndex].length; columnIndex++) {

                final String foundNumberAsString = lookForNumber(inputArray, lineIndex, columnIndex, new StringBuilder());
                if (foundNumberAsString != "") {

                    final int foundNumber = Integer.parseInt(foundNumberAsString);

                    final int indexStart = columnIndex;
                    final int indexEnd = indexStart + foundNumberAsString.length() - 1;

                    if (!checkIfNumberHasOtherNumberNeighbours(inputArray, indexStart, indexEnd, lineIndex)) {

                        final boolean isValid = checkSurrounding(inputArray, indexStart, indexEnd, lineIndex);

                        if (isValid) {
                            final FoundNumber foundNumberData = new FoundNumber(indexStart, indexEnd, lineIndex, foundNumber);
                            foundValidNumberDatas.add(foundNumberData);
                        }

                    }

                }
            }

        }
        return foundValidNumberDatas;
    }

    private static String lookForNumber(
            final String[][] inputArray,
            final int lineIndex,
            final int columnIndex,
            final StringBuilder numberBuilder
    ) {
        try {
            final String potentialNumber = inputArray[lineIndex][columnIndex];
            if (VALID_NUMBERS.contains(potentialNumber)) {
                //  log("Found number: " + potentialNumber);
                numberBuilder.append(inputArray[lineIndex][columnIndex]);
                return lookForNumber(inputArray, lineIndex, columnIndex + 1, numberBuilder);
            }
            return numberBuilder.toString();
        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return numberBuilder.toString();
        }
    }

    private static boolean checkIfNumberHasOtherNumberNeighbours(
            final String[][] array,
            final int indexStart,
            final int indexEnd,
            final int line
    ) {

        // Check character to the left via parsing.
        try {
            final String charToAnalyze = array[line][indexStart - 1];
            final Integer leftNumber = Integer.parseInt(charToAnalyze);
            return true;

        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            //...
        }

        // Check character to the right via parsing.
        try {
            final String charToAnalyze = array[line][indexEnd + 1];
            final Integer rightNumber = Integer.parseInt(charToAnalyze);
            return true;

        } catch (final NumberFormatException | ArrayIndexOutOfBoundsException e) {
            //...
        }

        return false;
    }

    private static boolean checkSurrounding(
            final String[][] array,
            final int indexStart,
            final int indexEnd,
            final int line
    ) {

        final List<Integer> rangeList = List.of(-1, 0, 1);

        for (int additional = 0; additional <= indexEnd - indexStart; additional++) {
            for (final Integer rangeI : rangeList) {
                for (final Integer rangeJ : rangeList) {
                    final int i = line + rangeJ;
                    final int j = indexStart + rangeI + additional;

                    try {
                        final String charToAnalyze = array[i][j];

                        // log("Check: " + i + " " + j);
                        if (!VALID_NUMBERS.contains(charToAnalyze) && !Objects.equals(charToAnalyze, ".")) {
                            // log("ValidChar: " + charToAnalyze + "(Coords: " + i + " " + j + ")");
                            return true;
                        }
                    } catch (final Exception e) {
                        //...
                    }
                }
            }
        }

        return false;
    }

    private static boolean checkSurroundingAsterisk(
            final String[][] array,
            final FoundNumber foundValidNumberData,
            final Map<String, FoundAsterisk> asteriskMap
    ) {
        // log("Check for number: " + foundValidNumberData.getNumber());

        final Integer indexStart = foundValidNumberData.indexStart();
        final Integer indexEnd = foundValidNumberData.indexEnd();
        final Integer line = foundValidNumberData.line();

        final List<Integer> rangeList = List.of(-1, 0, 1);

        // Check the surrounding around the whole string.
        for (int additional = 0; additional <= indexEnd - indexStart; additional++) {
            for (final Integer rangeI : rangeList) {
                for (final Integer rangeJ : rangeList) {
                    final int i = line + rangeJ;
                    final int j = indexStart + rangeI + additional;

                    try {
                        final String charToAnalyze = array[i][j];

                        //  log("Check: " + i + " " + j + " : " + charToAnalyze);
                        if (Objects.equals(charToAnalyze, ASTERISK)) {
                            //    log("Valid Asterisk: " + charToAnalyze + "(Coords: " + i + " " + j + ")");

                            final String key = i + "-" + j;
                            asteriskMap.computeIfAbsent(key, k -> new FoundAsterisk(i, j));
                            final FoundAsterisk asterisk = asteriskMap.get(key);
                            if (!asterisk.getFoundByNumber().contains(foundValidNumberData)) {
                                asterisk.getNumbers().add(foundValidNumberData.number());
                                asterisk.getFoundByNumber().add(foundValidNumberData);
                            }

                        }
                    } catch (final Exception e) {
                        //...
                    }
                }
            }
        }

        return false;
    }

    /**
     * Contains all information about the found asterisk.
     */
    private static class FoundAsterisk {

        private final Set<FoundNumber> foundByNumber;
        private final int line;
        private final int column;
        private final List<Integer> numbers;

        public FoundAsterisk(
                final int line,
                final int column
        ) {
            this.line = line;
            this.column = column;
            this.numbers = new ArrayList<>();
            this.foundByNumber = new HashSet<>();
        }

        public Set<FoundNumber> getFoundByNumber() {
            return foundByNumber;
        }

        public List<Integer> getNumbers() {
            return numbers;
        }
    }

    /**
     * Contains all information about the found number.
     */
    public record FoundNumber(int indexStart, int indexEnd, int line, int number) {

    }

}
