package year2023.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12_test_01.txt";
      //   final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12.txt";

        List<String> inputs = ImportUtils.readAsList(filePath);

     //  List<SpringConfigs> springConfigs = createSpringConfigsForPart1(inputs);
        List<SpringConfigs> springConfigs = createSpringConfigsForPart2(inputs);

        List<Integer> arrangementsPerConfig = new ArrayList<>();

        springConfigs.parallelStream().forEach(sc -> processSpringConfig(sc, arrangementsPerConfig));

        int sumOfArrangements = arrangementsPerConfig.stream().reduce(Integer::sum).get();
        Utils.log("Total sum " + sumOfArrangements);

    }

    private static void processSpringConfig(
            final SpringConfigs springConfig,
            final List<Integer> arrangementsPerConfig
    ) {
        String unknownCondition = springConfig.unknownCondition();
        List<Integer> records = springConfig.records();

        int depth = unknownCondition.length();
        String start = "";

        List<String> allResults = new ArrayList<>();
        //recursiveApproach(start, depth, allResults, unknownCondition.toCharArray(),records);
        iterativeApproach(springConfig, allResults);

        Utils.log(unknownCondition+" result "+allResults.size());
        //            for (String allResult : allResults) {
        //                Utils.log(allResult);
        //            }

        List<String> possibleResults = new ArrayList<>();
        allResults.stream().forEach(result -> {

     //       if (matchesPattern(result, unknownCondition) && checkExactAmountOfHashes(result, records)) {

                String workingString = String.valueOf(result);

                for (int i = 0; i < records.size(); i++) {
                    int record = records.get(i);
                    boolean isLast = i == records.size() - 1;

                    String stringToLookup = "#".repeat(record) + (isLast ? "" : ".");

                    workingString = getSubstringAfter(workingString, stringToLookup);
                    if (workingString == null) {
                        break;
                    }
                }

                // We check the rest string
                if (workingString != null && !workingString.contains("#")) {
                    possibleResults.add(result);
                    // Utils.log(result);
                }

  //          }
        });

        int arrangementPerUnkownConfig = possibleResults.size();

        Utils.log(springConfig.unknownCondition() + " -> " + arrangementPerUnkownConfig);
        arrangementsPerConfig.add(arrangementPerUnkownConfig);
    }


    private static List<SpringConfigs> createSpringConfigsForPart1(List<String> inputs) {
        List<SpringConfigs> springConfigs = new ArrayList<>();
        for (String input : inputs) {
            final String[] split = input.split(" ");
            String unknownCondition = split[0];
            List<Integer> records = Arrays.stream(split[1].split(","))
                    .map(Integer::valueOf)
                    .toList();

            int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
            springConfigs.add(new SpringConfigs(unknownCondition, exactAmountOfHashes, records));


        }
        return springConfigs;
    }

    private static List<SpringConfigs> createSpringConfigsForPart2(List<String> inputs) {
        List<SpringConfigs> springConfigs = new ArrayList<>();
        for (String input : inputs) {
            final String[] split = input.split(" ");
            String unknownCondition = split[0];
            List<Integer> records = Arrays.stream(split[1].split(","))
                    .map(Integer::valueOf)
                    .toList();

            List<Integer> extendedRecords = new ArrayList<>(records);
            StringBuilder extendedUnknownCondition = new StringBuilder(unknownCondition);
            for (int i = 0; i < 4; i++) {
                extendedUnknownCondition.append("?").append(unknownCondition);
                extendedRecords.addAll(records);
            }

            //            Utils.log(extendedUnknownCondition.toString()+" "+extendedRecords.stream()
            //                    .map(String::valueOf)
            //                    .collect(Collectors.joining(",")));

            int exactAmountOfHashes = extendedRecords.stream().reduce(0, Integer::sum);
            springConfigs.add(new SpringConfigs(extendedUnknownCondition.toString(), exactAmountOfHashes, extendedRecords));

        }
        return springConfigs;
    }


    private static void iterativeApproach(
            final SpringConfigs configs,
            final List<String> list
    ) {
        char[]  unknownCondition = configs.unknownCondition().toCharArray();
        List<Integer> records = configs.records();

        Queue<Node> queue = new LinkedList<>();
        queue.add(new Node("", unknownCondition.length));

        while (!queue.isEmpty()) {
            Node currentNode = queue.poll();
            String start = currentNode.start;
            int depth = currentNode.depth;

            if (depth == 0) {
                String startDot = start + ".";
                if(checkMaxAmountOfDots(startDot,records, unknownCondition.length)) {
                    list.add(start + ".");
                }

                String startHash = start + "#";
                if (checkMaxAmountOfHashes(startHash, configs)) {
                    list.add(startHash);
                }

                continue;
            }

            final char currentChar = unknownCondition[unknownCondition.length - depth];
            boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
            boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');

            if (isDotOrQuestionMark) {
                String startDot = start + ".";
                if(checkMaxAmountOfDots(startDot,records, unknownCondition.length)) {
                    queue.add(new Node(startDot, depth - 1));
                }
            }
            if (isHashOrQuestionMark) {
                String startHash = start + "#";
                if (checkMaxAmountOfHashes(startHash, configs)) {
                    queue.add(new Node(startHash, depth - 1));
                }

            }
        }
    }

    private static class Node {

        String start;
        int depth;

        public Node(
                String start,
                int depth
        ) {
            this.start = start;
            this.depth = depth;
        }
    }

//    private static void recursiveApproach(
//            final String start,
//            final int depth,
//            final List<String> list,
//            final char[] unknownCondition,
//            final List<Integer> records
//    ) {
//        if (depth == 0) {
//            list.add(start + ".");
//            list.add(start + "#");
//            return;
//        }
//
//        final char currentChar = unknownCondition[unknownCondition.length - depth];
//        boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
//        boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');
//
//        if (isDotOrQuestionMark) {
//            recursiveApproach(start + ".", depth - 1, list, unknownCondition, records);
//        }
//        if (isHashOrQuestionMark) {
//            String startHash = start + "#";
//            if (checkMaxAmountOfHashes(startHash, records)) {
//                recursiveApproach(startHash, depth - 1, list, unknownCondition, records);
//            }
//        }
//    }

    private static boolean matchesPattern(
            final String result,
            final String unknownCondition
    ) {
        for (int i = 0; i < unknownCondition.length(); i++) {
            char resultChar = result.charAt(i);
            char conditionChar = unknownCondition.charAt(i);

            // Check the pattern.
            if ((conditionChar == '#' && resultChar != '#') || (conditionChar == '.' && resultChar != '.')) {
                return false;
            }
        }

        return true;
    }

    private static boolean checkMaxAmountOfHashes(
            final String result,
            final SpringConfigs configs
    ) {
        int exactAmountOfHashes = configs.amountOfHashes();
        int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);

        int max = Collections.max(configs.records());
        String moreThanMaxHashes = "#".repeat(max+1);

        return (occurrencesInResult <= exactAmountOfHashes) && !result.contains(moreThanMaxHashes);
    }

    private static boolean checkMaxAmountOfDots(
            final String result,
            final List<Integer> records,
            int length
    ) {
        int exactAmountOfDots = length- records.stream().reduce(0, Integer::sum);
        int occurrencesInResult = countLetterFrequency(result).getOrDefault('.', 0);

        return occurrencesInResult <= exactAmountOfDots+1;
    }

    private static boolean checkExactAmountOfHashes(
            final String result,
            final List<Integer> records
    ) {
        int exactAmountOfHashes = records.stream().reduce(0, Integer::sum);
        int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);

        return exactAmountOfHashes == occurrencesInResult;
    }

    private static Map<Character, Integer> countLetterFrequency(String text) {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for (char ch : text.toCharArray()) {
            frequencyMap.put(ch, frequencyMap.getOrDefault(ch, 0) + 1);
        }

        return frequencyMap;
    }

    private static String getSubstringAfter(
            String mainString,
            String subString
    ) {
        int index = mainString.indexOf(subString);

        if (index == -1 || (index > 0 && mainString.charAt(index - 1) == '#')) {
            return null; // Substring nicht gefunden oder ein '#' davor
        }

        int afterIndex = index + subString.length();

        if (afterIndex < mainString.length()) {
            return mainString.substring(afterIndex);
        }

        return null; // Kein Text nach dem subString
    }


    private record SpringConfigs(String unknownCondition, int amountOfHashes, List<Integer> records) {

    }

}
