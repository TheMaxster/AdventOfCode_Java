package days.day12;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class Main_04 {

    private record SpringConfig(String condition, List<Integer> records) {

    }

    public static void main(String[] args) {
       // final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day12/input_12.txt";

        List<String> inputs = ImportUtils.readAsList(filePath);

        // int result = calculatePart1(inputs);
        long result = calculatePart2(inputs);

        Utils.log("Total sum: " + result);


    }

    private static long calculatePart1(final List<String> inputs) {
        return calculateSum(inputs, 1);
    }

    private static long calculatePart2(final List<String> inputs) {
        return calculateSum(inputs, 5);
    }

    private static long calculateSum(
            final List<String> inputs,
            final int foldingFactor
    ) {
        long sum = 0;
        HashMap<String, Long> cache = new HashMap<>();
        for (String input : inputs) {
            SpringConfig config = processInput(input, foldingFactor);
            sum += arrange(config.condition(), config.records(), -1, cache);


        }
        return sum;
    }


    // processInput parses a single input row with the given folding factor.
    // The folding factor defines how many times the input string and the spring condition should be repeated
    private static SpringConfig processInput(
            final String input,
            final int foldingFactor
    ) {
        final String[] split = input.split(" ");
        final String condition = split[0];
        final List<Integer> records = Arrays.stream(split[1].split(","))
                .map(Integer::valueOf)
                .toList();

        List<Integer> extendedRecords = new ArrayList<>(records);
        StringBuilder extendedCondition = new StringBuilder(condition);
        for (int i = 1; i < foldingFactor; i++) {
            extendedCondition.append("?").append(condition);
            extendedRecords.addAll(records);
        }

        return new SpringConfig(extendedCondition.toString(), extendedRecords);
    }

    private static long arrange(
            final String row,
            List<Integer> groups,
            int currentGroup,
            final HashMap<String, Long> cache
    ) {
        String key = generateKey(row, groups, currentGroup);
        Long m = cache.getOrDefault(key, null);

        if (m != null) {
            return m;
        }

        if (Objects.equals(row, "") && groups.isEmpty() && currentGroup <= 0) {
            return 1;
        } else if (row.length() == 0) {
            return 0;
        }

        long d = 0;

        List<Integer> groupsSublist = groups;
        if (row.length() > 0) {
            switch (row.charAt(0)) {
                case '#' -> {
                    if (currentGroup == 0 || (currentGroup == -1 && groupsSublist.isEmpty())) {
                        return 0;
                    } else if (currentGroup == -1) {
                        currentGroup = groupsSublist.get(0);
                        groupsSublist = groups.subList(1, groups.size());
                    }
                    d = arrange(getSubstring(row), groupsSublist, currentGroup - 1, cache);
                }
                case '.' -> {
                    if (currentGroup <= 0) {
                        d = arrange(getSubstring(row), groupsSublist, -1, cache);
                    }
                }
                case '?' -> {
                    d = arrange("#" + getSubstring(row), groupsSublist, currentGroup, cache) + arrange("." + getSubstring(row),
                            groupsSublist, currentGroup, cache);
                }
            }
        }
        cache.put(generateKey(row, groupsSublist, currentGroup), d);
        return d;
    }

    private static String generateKey(
            final String row,
            final List<Integer> groups,
            final int currentGroups
    ) {
        return row + "->" + groups.stream().map(String::valueOf).collect(Collectors.joining(",")) + "->" + currentGroups;
    }

    private static String getSubstring(String string) {
        if (string.length() - 1 < 1) {
            return "";
        } else {
            return string.substring(1);
        }
    }


}