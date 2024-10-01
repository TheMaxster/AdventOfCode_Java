package year2023.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;
import utils.Utils;

public class Main_02 {

    public static void main(final String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day12/input_test_01.txt";
        //   final String filePath = System.getProperty("user.dir") + "/resources/year2023/day12/input.txt";

        final List<String> inputs = ImportUtils.readAsList(filePath);

        //List<SpringConfigs> springConfigs = createSpringConfigsForPart1(inputs);
        final List<SpringConfigs> springConfigs = createSpringConfigsForPart2(inputs);

        final List<Integer> arrangementsPerConfig = new ArrayList<>();

        springConfigs.parallelStream().forEach(sc -> processSpringConfig(sc, arrangementsPerConfig));

        final int sumOfArrangements = arrangementsPerConfig.stream().reduce(Integer::sum).get();
        Utils.log("Total sum " + sumOfArrangements);

    }

    private static void processSpringConfig(
            final SpringConfigs springConfig,
            final List<Integer> arrangementsPerConfig
    ) {
        final String unknownCondition = springConfig.unknownCondition();
        final List<Integer> records = springConfig.records();

        final int depth = unknownCondition.length();
        final String start = "";

        final List<String> allResults = new ArrayList<>();
        //recursiveApproach(start, depth, allResults, unknownCondition.toCharArray(),records);
        //iterativeApproach(springConfig, allResults);
        iterativeApproachWithFor(springConfig, allResults);

        Utils.log(unknownCondition + " result " + allResults.size());
        //        for (String allResult : allResults) {
        //            Utils.log("R: "+allResult);
        //        }

        final List<String> possibleResults = new ArrayList<>();
        allResults.stream().forEach(result -> {

            if (matchesPattern(result, unknownCondition) && checkExactAmountOfHashes(result, records)) {

                final String workingString = String.valueOf(result);

                if (checkForRecords(workingString, records)) {
                    possibleResults.add(result);
                }

            }
        });

        final int arrangementPerUnkownConfig = possibleResults.size();

        Utils.log(springConfig.unknownCondition() + " -> " + arrangementPerUnkownConfig);
        arrangementsPerConfig.add(arrangementPerUnkownConfig);
    }

    private static boolean checkForRecords(
            String workingString,
            final List<Integer> records
    ) {
        for (int i = 0; i < records.size(); i++) {
            final int record = records.get(i);
            final boolean isLast = i == records.size() - 1;

            final String stringToLookup = "#".repeat(record) + (isLast ? "" : ".");

            workingString = getSubstringAfter(workingString, stringToLookup);
            if (workingString == null) {
                break;
            }
        }

        // We check the rest string
        if (workingString != null && !workingString.contains("#")) {
            return true;
        }

        return false;
    }


    private static List<SpringConfigs> createSpringConfigsForPart1(final List<String> inputs) {
        final List<SpringConfigs> springConfigs = new ArrayList<>();
        for (final String input : inputs) {
            final String[] split = input.split(" ");
            final String unknownCondition = split[0];
            final List<Integer> records = Arrays.stream(split[1].split(","))
                    .map(Integer::valueOf)
                    .toList();

            final int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
            springConfigs.add(new SpringConfigs(unknownCondition, exactAmountOfHashes, records));


        }
        return springConfigs;
    }

    private static List<SpringConfigs> createSpringConfigsForPart2(final List<String> inputs) {
        final List<SpringConfigs> springConfigs = new ArrayList<>();
        for (final String input : inputs) {
            final String[] split = input.split(" ");
            final String unknownCondition = split[0];
            final List<Integer> records = Arrays.stream(split[1].split(","))
                    .map(Integer::valueOf)
                    .toList();

            final List<Integer> extendedRecords = new ArrayList<>(records);
            final StringBuilder extendedUnknownCondition = new StringBuilder(unknownCondition);
            for (int i = 0; i < 4; i++) {
                extendedUnknownCondition.append("?").append(unknownCondition);
                extendedRecords.addAll(records);
            }

            //            Utils.log(extendedUnknownCondition.toString()+" "+extendedRecords.stream()
            //                    .map(String::valueOf)
            //                    .collect(Collectors.joining(",")));

            final int exactAmountOfHashes = extendedRecords.stream().reduce(0, Integer::sum);
            springConfigs.add(new SpringConfigs(extendedUnknownCondition.toString(), exactAmountOfHashes, extendedRecords));

        }
        return springConfigs;
    }

    private static void iterativeApproachWithFor(
            final SpringConfigs configs,
            final List<String> list
    ) {
        final char[] unknownCondition = configs.unknownCondition().toCharArray();
        final List<Integer> records = configs.records();
        final Integer recordsSumOfHashes = records.stream().reduce(0, Integer::sum);

        final int max = Collections.max(configs.records());
        final String maxAmountOfHashes = "#".repeat(max + 1);

        final String start = "";

        List<String> tmpAssignments = new ArrayList<>();
        List<String> tmpAssignments2;
        tmpAssignments.add(start);

        for (int i = 0; i < unknownCondition.length; i++) {

            final char currentChar = unknownCondition[i];
            tmpAssignments2 = new ArrayList<>();
            final boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
            final boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');

            for (final String tmpAssignment : tmpAssignments) {

                if (isDotOrQuestionMark) {
                    final String tmpAssignmentAndDot = tmpAssignment + ".";
                    if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {

                        if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {

                            if (i == unknownCondition.length - 1) {
                                if (matchesPattern(tmpAssignmentAndDot, configs.unknownCondition()) && checkExactAmountOfHashes(
                                        tmpAssignmentAndDot, records)) {
                                    final String workingString = String.valueOf(tmpAssignmentAndDot);
                                    if (checkForRecords(workingString, records)) {
                                        tmpAssignments2.add(tmpAssignmentAndDot);
                                    }
                                }

                            } else {
                                tmpAssignments2.add(tmpAssignmentAndDot);
                            }

                        }

                    }


                }
                if (isHashOrQuestionMark) {
                    final String tmpAssignmentAndHash = tmpAssignment + "#";
                    if (checkMaxAmountOfHashes(tmpAssignmentAndHash, configs, maxAmountOfHashes)) {

                        if (i == unknownCondition.length - 1) {
                            if (matchesPattern(tmpAssignmentAndHash, configs.unknownCondition()) && checkExactAmountOfHashes(
                                    tmpAssignmentAndHash, records)) {
                                final String workingString = String.valueOf(tmpAssignmentAndHash);
                                if (checkForRecords(workingString, records)) {
                                    tmpAssignments2.add(tmpAssignmentAndHash);
                                }
                            }

                        } else {
                            tmpAssignments2.add(tmpAssignmentAndHash);
                        }
                    }

                }


            }

            tmpAssignments = tmpAssignments2;


        }

        list.addAll(tmpAssignments);


    }

    private static class Assignment {

        int amountDots;

        public Assignment(
                final String assignment,
                final int amountHashes,
                final int amountDots
        ) {
            this.amountDots = amountDots;
            this.amountHashes = amountHashes;
            this.assignment = assignment;
        }

        int amountHashes;
        String assignment;

        public int getAmountDots() {
            return amountDots;
        }

        public void setAmountDots(final int amountDots) {
            this.amountDots = amountDots;
        }

        public int getAmountHashes() {
            return amountHashes;
        }

        public void setAmountHashes(final int amountHashes) {
            this.amountHashes = amountHashes;
        }

        public String getAssignment() {
            return assignment;
        }

        public void setAssignment(final String assignment) {
            this.assignment = assignment;
        }


    }

    private static boolean matchesPattern(
            final String result,
            final String unknownCondition
    ) {
        for (int i = 0; i < unknownCondition.length(); i++) {
            final char resultChar = result.charAt(i);
            final char conditionChar = unknownCondition.charAt(i);

            // Check the pattern.
            if ((conditionChar == '#' && resultChar != '#') || (conditionChar == '.' && resultChar != '.')) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkMaxAmountOfHashes(
            final String result,
            final SpringConfigs configs,
            final String maxAmountOfHashes
    ) {
        final int exactAmountOfHashes = configs.amountOfHashes();
        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);

        return (occurrencesInResult <= exactAmountOfHashes) && !result.contains(maxAmountOfHashes);
    }

    private static boolean checkMaxAmountOfDots(
            final String result,
            final int amountOfHashesInRecords,
            final int length
    ) {
        final int exactAmountOfDots = length - amountOfHashesInRecords;
        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('.', 0);

        return occurrencesInResult <= exactAmountOfDots + 1;
    }

    private static boolean checkExactAmountOfHashes(
            final String result,
            final List<Integer> records
    ) {
        final int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);

        return exactAmountOfHashes == occurrencesInResult;
    }

    private static Map<Character, Integer> countLetterFrequency(final String text) {
        final Map<Character, Integer> frequencyMap = new HashMap<>();

        for (final char ch : text.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }

        return frequencyMap;
    }

    private static String getSubstringAfter(
            final String mainString,
            final String subString
    ) {
        final int index = mainString.indexOf(subString);

        if (index == -1 || (index > 0 && mainString.charAt(index - 1) == '#')) {
            return null; // Substring nicht gefunden oder ein '#' davor
        }

        final int afterIndex = index + subString.length();

        if (afterIndex < mainString.length()) {
            return mainString.substring(afterIndex);
        }

        return ""; // Kein Text nach dem subString
    }


    private record SpringConfigs(String unknownCondition, int amountOfHashes, List<Integer> records) {

    }

}
