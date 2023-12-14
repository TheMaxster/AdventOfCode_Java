package days.day12;

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

public class Main_02 {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12_test_01.txt";
       //   final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12.txt";

        List<String> inputs = ImportUtils.readAsList(filePath);

        //List<SpringConfigs> springConfigs = createSpringConfigsForPart1(inputs);
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
        //iterativeApproach(springConfig, allResults);
        iterativeApproachWithFor(springConfig, allResults);

        Utils.log(unknownCondition + " result " + allResults.size());
        //        for (String allResult : allResults) {
        //            Utils.log("R: "+allResult);
        //        }

        List<String> possibleResults = new ArrayList<>();
        allResults.stream().forEach(result -> {

            if (matchesPattern(result, unknownCondition) && checkExactAmountOfHashes(result, records)) {

                String workingString = String.valueOf(result);

                if(checkForRecords(workingString, records)){
                    possibleResults.add(result);
                }

            }
        });

        int arrangementPerUnkownConfig = possibleResults.size();

        Utils.log(springConfig.unknownCondition() + " -> " + arrangementPerUnkownConfig);
        arrangementsPerConfig.add(arrangementPerUnkownConfig);
    }

    private static boolean checkForRecords(
            String workingString,
            final List<Integer> records
    ) {
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
            return true;
        }

        return false;
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

    private static void iterativeApproachWithFor(
            final SpringConfigs configs,
            final List<String> list
    ) {
        char[] unknownCondition = configs.unknownCondition().toCharArray();
        List<Integer> records = configs.records();
        Integer recordsSumOfHashes =  records.stream().reduce(0, Integer::sum);

        int max = Collections.max(configs.records());
        String maxAmountOfHashes = "#".repeat(max + 1);

        String start = "";

        List<String> tmpAssignments = new ArrayList<>();
        List<String> tmpAssignments2;
        tmpAssignments.add(start);

        for (int i = 0; i < unknownCondition.length; i++) {

            char currentChar = unknownCondition[i];
            tmpAssignments2 = new ArrayList<>();
            boolean isDotOrQuestionMark = (currentChar == '.' || currentChar == '?');
            boolean isHashOrQuestionMark = (currentChar == '#' || currentChar == '?');

            for (String tmpAssignment : tmpAssignments) {

                if (isDotOrQuestionMark) {
                    String tmpAssignmentAndDot = tmpAssignment + ".";
                    if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {

                        if (checkMaxAmountOfDots(tmpAssignmentAndDot, configs.amountOfHashes(), unknownCondition.length)) {

                            if(i == unknownCondition.length-1){
                                if (matchesPattern(tmpAssignmentAndDot, configs.unknownCondition()) && checkExactAmountOfHashes(tmpAssignmentAndDot, records)) {
                                    String workingString = String.valueOf(tmpAssignmentAndDot);
                                    if(checkForRecords(workingString, records)){
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
                    String tmpAssignmentAndHash = tmpAssignment+ "#";
                    if (checkMaxAmountOfHashes(tmpAssignmentAndHash, configs, maxAmountOfHashes)) {

                        if(i == unknownCondition.length-1){
                            if (matchesPattern(tmpAssignmentAndHash, configs.unknownCondition()) && checkExactAmountOfHashes(tmpAssignmentAndHash, records)) {
                                String workingString = String.valueOf(tmpAssignmentAndHash);
                                if(checkForRecords(workingString, records)){
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
            final SpringConfigs configs,
            String maxAmountOfHashes
    ) {
        int exactAmountOfHashes = configs.amountOfHashes();
         int occurrencesInResult = countLetterFrequency(result).getOrDefault('#', 0);

        return (occurrencesInResult <= exactAmountOfHashes) && !result.contains(maxAmountOfHashes);
    }

    private static boolean checkMaxAmountOfDots(
            final String result,
            final int amountOfHashesInRecords,
            int length
    ) {
        int exactAmountOfDots = length - amountOfHashesInRecords;
         int occurrencesInResult = countLetterFrequency(result).getOrDefault('.', 0);

        return occurrencesInResult <= exactAmountOfDots + 1;
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

        return ""; // Kein Text nach dem subString
    }


    private record SpringConfigs(String unknownCondition, int amountOfHashes, List<Integer> records) {

    }

}