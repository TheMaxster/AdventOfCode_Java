//package main.java.year2023.day12;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedList;
//import java.util.List;
//import java.util.Map;
//import java.util.Queue;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main {
//
//    public static void main(final String[] args) {
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day12/input_test_01.txt";
//        //   final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day12/input.txt";
//
//        final List<String> inputs = ImportUtils.readAsList(filePath);
//
//        //  List<SpringConfigs> springConfigs = createSpringConfigsForPart1(inputs);
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
//        final List<String> allResults = new ArrayList<>();
//        //recursiveApproach(start, depth, allResults, unknownCondition.toCharArray(),records);
//        iterativeApproach(springConfig, allResults);
//
//        ArrayUtils.log(unknownCondition + " result " + allResults.size());
//        //            for (String allResult : allResults) {
//        //                Utils.log(allResult);
//        //            }
//
//        final List<String> possibleResults = new ArrayList<>();
//        allResults.stream().forEach(result -> {
//
//            //       if (matchesPattern(result, unknownCondition) && checkExactAmountOfHashes(result, records)) {
//
//            String workingString = String.valueOf(result);
//
//            for (int i = 0; i < records.size(); i++) {
//                final int record = records.get(i);
//                final boolean isLast = i == records.size() - 1;
//
//                final String stringToLookup = "#".repeat(record) + (isLast ? "" : ".");
//
//                workingString = getSubstringAfter(workingString, stringToLookup);
//                if (workingString == null) {
//                    break;
//                }
//            }
//
//            // We check the rest string
//            if (workingString != null && !workingString.contains("#")) {
//                possibleResults.add(result);
//                // Utils.log(result);
//            }
//
//            //          }
//        });
//
//        final int arrangementPerUnkownConfig = possibleResults.size();
//
//        ArrayUtils.log(springConfig.unknownCondition() + " -> " + arrangementPerUnkownConfig);
//        arrangementsPerConfig.add(arrangementPerUnkownConfig);
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
//
//    private static void iterativeApproach(
//            final SpringConfigs configs,
//            final List<String> list
//    ) {
//        final char[] unknownCondition = configs.unknownCondition().toCharArray();
//        final List<Integer> records = configs.records();
//
//        final Queue<Node> queue = new LinkedList<>();
//        queue.add(new Node("", unknownCondition.length));
//
//        while (!queue.isEmpty()) {
//            final Node currentNode = queue.poll();
//            final String start = currentNode.start;
//            final int depth = currentNode.depth;
//
//            if (depth == 0) {
//                final String startDot = start + ".";
//                if (checkMaxAmountOfDots(startDot, records, unknownCondition.length)) {
//                    list.add(start + ".");
//                }
//
//                final String startHash = start + "#";
//                if (checkMaxAmountOfHashes(startHash, configs)) {
//                    list.add(startHash);
//                }
//
//                continue;
//            }
//
//            final char currentChar = unknownCondition[unknownCondition.length - depth];
//            final boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
//            final boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');
//
//            if (isDotOrQuestionMark) {
//                final String startDot = start + ".";
//                if (checkMaxAmountOfDots(startDot, records, unknownCondition.length)) {
//                    queue.add(new Node(startDot, depth - 1));
//                }
//            }
//            if (isHashOrQuestionMark) {
//                final String startHash = start + "#";
//                if (checkMaxAmountOfHashes(startHash, configs)) {
//                    queue.add(new Node(startHash, depth - 1));
//                }
//
//            }
//        }
//    }
//
//    private static class Node {
//
//        String start;
//        int depth;
//
//        public Node(
//                final String start,
//                final int depth
//        ) {
//            this.start = start;
//            this.depth = depth;
//        }
//    }
//
//    //    private static void recursiveApproach(
//    //            final String start,
//    //            final int depth,
//    //            final List<String> list,
//    //            final char[] unknownCondition,
//    //            final List<Integer> records
//    //    ) {
//    //        if (depth == 0) {
//    //            list.add(start + ".");
//    //            list.add(start + "#");
//    //            return;
//    //        }
//    //
//    //        final char currentChar = unknownCondition[unknownCondition.length - depth];
//    //        boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
//    //        boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');
//    //
//    //        if (isDotOrQuestionMark) {
//    //            recursiveApproach(start + ".", depth - 1, list, unknownCondition, records);
//    //        }
//    //        if (isHashOrQuestionMark) {
//    //            String startHash = start + "#";
//    //            if (checkMaxAmountOfHashes(startHash, records)) {
//    //                recursiveApproach(startHash, depth - 1, list, unknownCondition, records);
//    //            }
//    //        }
//    //    }
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
//            final SpringConfigs configs
//    ) {
//        final int exactAmountOfHashes = configs.amountOfHashes();
//        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);
//
//        final int max = Collections.max(configs.records());
//        final String moreThanMaxHashes = "#".repeat(max + 1);
//
//        return (occurrencesInResult <= exactAmountOfHashes) && !result.contains(moreThanMaxHashes);
//    }
//
//    private static boolean checkMaxAmountOfDots(
//            final String result,
//            final List<Integer> records,
//            final int length
//    ) {
//        final int exactAmountOfDots = length - records.stream().reduce(0, Integer::sum);
//        final int occurrencesInResult = countLetterFrequency(result).getOrDefault('.', 0);
//
//        return occurrencesInResult <= exactAmountOfDots + 1;
//    }
//
//    private static boolean checkExactAmountOfHashes(
//            final String result,
//            final List<Integer> records
//    ) {
//        final int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
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
//        return null; // Kein Text nach dem subString
//    }
//
//
//    private record SpringConfigs(String unknownCondition, int amountOfHashes, List<Integer> records) {
//
//    }
//
//}
