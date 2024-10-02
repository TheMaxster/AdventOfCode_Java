//package main.java.year2023.day12;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main_03 {
//
//    public static void main(final String[] args) {
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day12/input_test_01.txt";
//        //   final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day12/input.txt";
//
//        final List<String> inputs = ImportUtils.readAsList(filePath);
//
//        // List<SpringConfigs> springConfigs = createSpringConfigsForPart1(inputs);
//        final List<SpringConfigs> springConfigs = createSpringConfigsForPart2(inputs);
//
//        final List<Integer> arrangementsPerConfig = new ArrayList<>();
//
//        springConfigs.parallelStream().forEach(sc -> processSpringConfig(sc, arrangementsPerConfig));
//
//        final int sumOfArrangements = arrangementsPerConfig.stream().reduce(Integer::sum).get();
//        ArrayUtils.log("Total sum " + sumOfArrangements);
//
//    }
//
//    private static void processSpringConfig(
//            final SpringConfigs springConfig,
//            final List<Integer> arrangementsPerConfig
//    ) {
//        final String unknownCondition = springConfig.unknownCondition();
//        final List<Integer> records = springConfig.records();
//
//        final int depth = unknownCondition.length();
//        final String start = "";
//
//        final int numberOfResults = iterativeApproachWithFor(springConfig);
//
//        ArrayUtils.log(springConfig.unknownCondition() + " -> " + numberOfResults);
//        arrangementsPerConfig.add(numberOfResults);
//    }
//
//    private static boolean checkForRecords(
//            String workingString,
//            final List<Integer> records
//    ) {
//        for (int i = 0; i < records.size(); i++) {
//            final int record = records.get(i);
//            final boolean isLast = i == records.size() - 1;
//
//            final String stringToLookup = "#".repeat(record) + (isLast ? "" : ".");
//
//            workingString = getSubstringAfter(workingString, stringToLookup);
//            if (workingString == null) {
//                break;
//            }
//        }
//
//        // We check the rest string
//        if (workingString != null && !workingString.contains("#")) {
//            return true;
//        }
//
//        return false;
//    }
//
//
//    private static List<SpringConfigs> createSpringConfigsForPart1(final List<String> inputs) {
//        final List<SpringConfigs> springConfigs = new ArrayList<>();
//        for (final String input : inputs) {
//            final String[] split = input.split(" ");
//            final String unknownCondition = split[0];
//            final List<Integer> records = Arrays.stream(split[1].split(","))
//                    .map(Integer::valueOf)
//                    .toList();
//
//            final int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
//            springConfigs.add(new SpringConfigs(unknownCondition, exactAmountOfHashes, records));
//
//
//        }
//        return springConfigs;
//    }
//
//    private static List<SpringConfigs> createSpringConfigsForPart2(final List<String> inputs) {
//        final List<SpringConfigs> springConfigs = new ArrayList<>();
//        for (final String input : inputs) {
//            final String[] split = input.split(" ");
//            final String unknownCondition = split[0];
//            final List<Integer> records = Arrays.stream(split[1].split(","))
//                    .map(Integer::valueOf)
//                    .toList();
//
//            final List<Integer> extendedRecords = new ArrayList<>(records);
//            final StringBuilder extendedUnknownCondition = new StringBuilder(unknownCondition);
//            for (int i = 0; i < 4; i++) {
//                extendedUnknownCondition.append("?").append(unknownCondition);
//                extendedRecords.addAll(records);
//            }
//
//            //            Utils.log(extendedUnknownCondition.toString()+" "+extendedRecords.stream()
//            //                    .map(String::valueOf)
//            //                    .collect(Collectors.joining(",")));
//
//            final int exactAmountOfHashes = extendedRecords.stream().reduce(0, Integer::sum);
//            springConfigs.add(new SpringConfigs(extendedUnknownCondition.toString(), exactAmountOfHashes, extendedRecords));
//
//        }
//        return springConfigs;
//    }
//
//    private static int iterativeApproachWithFor(
//            final SpringConfigs configs
//    ) {
//        final char[] unknownCondition = configs.unknownCondition().toCharArray();
//        final List<Integer> records = configs.records();
//        final Integer recordsSumOfHashes = records.stream().reduce(0, Integer::sum);
//
//        final int max = Collections.max(configs.records());
//        final String maxAmountOfHashes = "#".repeat(max + 1);
//
//        final String start = "";
//
//        List<String> tmpAssignments = new ArrayList<>();
//        List<String> tmpAssignments2;
//        tmpAssignments.add(start);
//
//        for (int i = 0; i < unknownCondition.length; i++) {
//
//            final char currentChar = unknownCondition[i];
//            tmpAssignments2 = new ArrayList<>();
//            final boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
//            final boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');
//
//            for (final String tmpAssignment : tmpAssignments) {
//
//                if (isDotOrQuestionMark) {
//                    final String tmpAssignmentAndDot = tmpAssignment + ".";
//                    if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {
//
//                        if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {
//
//                            if (i == unknownCondition.length - 1) {
//                                if (matchesPattern(tmpAssignmentAndDot, configs.unknownCondition()) && checkExactAmountOfHashes(
//                                        tmpAssignmentAndDot, recordsSumOfHashes)) {
//                                    final String workingString = String.valueOf(tmpAssignmentAndDot);
//                                    if (checkForRecords(workingString, records)) {
//                                        tmpAssignments2.add(tmpAssignmentAndDot);
//                                    }
//                                }
//
//                            } else {
//                                tmpAssignments2.add(tmpAssignmentAndDot);
//                            }
//
//                        }
//
//                    }
//
//
//                }
//                if (isHashOrQuestionMark) {
//                    final String tmpAssignmentAndHash = tmpAssignment + "#";
//                    if (checkMaxAmountOfHashes(tmpAssignmentAndHash, configs, maxAmountOfHashes)) {
//
//                        if (i == unknownCondition.length - 1) {
//                            if (matchesPattern(tmpAssignmentAndHash, configs.unknownCondition()) && checkExactAmountOfHashes(
//                                    tmpAssignmentAndHash, recordsSumOfHashes)) {
//                                final String workingString = String.valueOf(tmpAssignmentAndHash);
//                                if (checkForRecords(workingString, records)) {
//                                    tmpAssignments2.add(tmpAssignmentAndHash);
//                                }
//                            }
//
//                        } else {
//                            tmpAssignments2.add(tmpAssignmentAndHash);
//                        }
//                    }
//
//                }
//
//
//            }
//
//            tmpAssignments = tmpAssignments2;
//
//
//        }
//
//        return tmpAssignments.size();
//
//
//    }
//
//    private static boolean matchesPattern(
//            final String result,
//            final String unknownCondition
//    ) {
//        for (int i = 0; i < unknownCondition.length(); i++) {
//            final char resultChar = result.charAt(i);
//            final char conditionChar = unknownCondition.charAt(i);
//
//            // Check the pattern.
//            if ((conditionChar == '#' && resultChar != '#') || (conditionChar == '.' && resultChar != '.')) {
//                return false;
//            }
//        }
//
//        return true;
//    }
//
//    private static boolean checkMaxAmountOfHashes(
//            final String result,
//            final SpringConfigs configs,
//            final String maxAmountOfHashes
//    ) {
//        final int exactAmountOfHashes = configs.amountOfHashes();
//        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);
//
//        return (occurrencesInResult <= exactAmountOfHashes) && !result.contains(maxAmountOfHashes);
//    }
//
//    private static boolean checkMaxAmountOfDots(
//            final String result,
//            final int amountOfHashesInRecords,
//            final int length
//    ) {
//        final int exactAmountOfDots = length - amountOfHashesInRecords;
//        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('.', 0);
//
//        return occurrencesInResult <= exactAmountOfDots + 1;
//    }
//
//    private static boolean checkExactAmountOfHashes(
//            final String result,
//            final int exactAmountOfHashes
//    ) {
//        //   int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
//        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);
//
//        return exactAmountOfHashes == occurrencesInResult;
//    }
//
//    private static Map<Character, Integer> countLetterFrequency(final String text) {
//        final Map<Character, Integer> frequencyMap = new HashMap<>();
//
//        for (final char ch : text.toCharArray()) {
//            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
//        }
//
//        return frequencyMap;
//    }
//
//    private static String getSubstringAfter(
//            final String mainString,
//            final String subString
//    ) {
//        final int index = mainString.indexOf(subString);
//
//        if (index == -1 || (index > 0 && mainString.charAt(index - 1) == '#')) {
//            return null; // Substring nicht gefunden oder ein '#' davor
//        }
//
//        final int afterIndex = index + subString.length();
//
//        if (afterIndex < mainString.length()) {
//            return mainString.substring(afterIndex);
//        }
//
//        return ""; // Kein Text nach dem subString
//    }
//
//
//    private record SpringConfigs(String unknownCondition, int amountOfHashes, List<Integer> records) {
//
//    }
//
//}
