package year2023.day08;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/8
 */
public class Day08 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final String pathInstruction = input.get(0);
        final HashMap<String, LeftRightTuple> navigationMap = createNavigationMap(input);

        final String[] pathInstructionArray = pathInstruction.split("");
        log("Path: " + pathInstruction);

        // Define our starting key.
        final String startingKey = "AAA";
        final BigInteger steps = recursiveApproachForPart1(pathInstructionArray, navigationMap, startingKey, BigInteger.ZERO);
        return steps.toString();
    }

    @Override
    public String part2(final List<String> input) {
        final String pathInstruction = input.get(0);
        final HashMap<String, LeftRightTuple> navigationMap = createNavigationMap(input);

        final String[] pathInstructionArray = pathInstruction.split("");
        log("Path: " + pathInstruction);

        // Define our starting keys.
        final List<String> startingKeys = new ArrayList<>();
        for (final Map.Entry<String, LeftRightTuple> entry : navigationMap.entrySet()) {
            if (entry.getKey().endsWith("A")) {
                startingKeys.add(entry.getKey());
            }
        }

        // IMPORTANT: This approach is brute force, but more correct than the approach with least common multiple. The lcm approach only works with special test data.
        final Map<String, String> newNavigationMap = createNavigationMapForFullPath(pathInstructionArray, navigationMap);
        final long multiplier = pathInstructionArray.length;
        //        long steps = iterativeBruteForceApproachForPart2(newNavigationMap, startingKeys);
        //        long result = steps * multiplier;
        //        Utils.log("Solution Part 2: We reached **Z in steps: " + result);

        // ---------------------------------------------------------------------------------------------------

        final Map<String, Long> paths = new HashMap<>();

        iterativeLeastCommonMultipleApproachForPart2(newNavigationMap, startingKeys, paths);

        long result = 1;
        for (final long val : paths.values()) {
            result = leastCommonMultiple(result, val);
        }

        return String.valueOf(result * multiplier);
    }

    private static HashMap<String, LeftRightTuple> createNavigationMap(final List<String> importList) {
        final HashMap<String, LeftRightTuple> navigationMap = new HashMap<>();

        for (int i = 2; i < importList.size(); i++) {
            final String[] equationSplit = importList.get(i).split("=");
            final String key = equationSplit[0].trim();
            final String[] leftRightPair = equationSplit[1].split(",");
            final String left = leftRightPair[0].replace("(", "").trim();
            final String right = leftRightPair[1].replace(")", "").trim();
            final LeftRightTuple leftRightTuple = new LeftRightTuple(left, right);
            navigationMap.put(key, leftRightTuple);
        }
        return navigationMap;
    }

    private static void iterativeLeastCommonMultipleApproachForPart2(
            final Map<String, String> newNavigationMap,
            final List<String> startingKeys,
            final Map<String, Long> paths
    ) {

        for (int i = 0; i < startingKeys.size(); i++) {
            paths.put(startingKeys.get(i), 0L);
        }

        for (int i = 0; i < startingKeys.size(); i++) {
            long steps = 0L;

            final String startingKey = startingKeys.get(i);

            String tmpNode = startingKey;
            while (!tmpNode.endsWith("Z")) {
                tmpNode = newNavigationMap.get(tmpNode);
                steps++;
            }

            paths.put(startingKey, steps);
        }
    }

    private static long leastCommonMultiple(
            final long number1,
            final long number2
    ) {
        final long absNumber1 = Math.abs(number1);
        final long absNumber2 = Math.abs(number2);
        final long absHigherNumber = Math.max(absNumber1, absNumber2);
        final long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }


    private long iterativeBruteForceApproachForPart2(
            final Map<String, String> newNavigationMap,
            List<String> startingKeys
    ) {

        long pathsThatEndWithZ = 0;
        final long numberOfPaths = startingKeys.size();

        long steps = 0L;

        List<String> newNextPathKeys;
        while (numberOfPaths != pathsThatEndWithZ) {

            newNextPathKeys = (startingKeys.stream()
                    .map(newNavigationMap::get)
                    .toList());

            steps += 281L;

            pathsThatEndWithZ = newNextPathKeys.stream()
                    .filter(newNextPathKey -> newNextPathKey.endsWith("Z"))
                    .count();

            startingKeys = newNextPathKeys;

            // Utils.log("newNextPathKeys: "+newNextPathKeys.size());
            //Utils.log("Paths that end with Z: "+numberOfPaths+" "+pathsThatEndWithZ);
            //  Utils.log(String.valueOf(steps * 281L));
            log(String.valueOf(steps));
        }

        return steps;
    }

    private HashMap<String, String> createNavigationMapForFullPath(
            final String[] pathInstructionArray,
            final HashMap<String, LeftRightTuple> navigationMap
    ) {

        final HashMap<String, String> newNavigationMap = new HashMap<>();
        for (final Map.Entry<String, LeftRightTuple> entry : navigationMap.entrySet()) {

            final String start = entry.getKey();
            String nextPathKey = entry.getKey();

            for (int i = 0; i < pathInstructionArray.length; i++) {
                final String nextDirection = pathInstructionArray[i];

                final LeftRightTuple currenctLocation = navigationMap.get(nextPathKey);
                if (Objects.equals(nextDirection, "L")) {
                    nextPathKey = currenctLocation.left();
                } else if (Objects.equals(nextDirection, "R")) {
                    nextPathKey = currenctLocation.right();
                }
            }
            newNavigationMap.put(start, nextPathKey);
        }

        return newNavigationMap;
    }

    private static BigInteger recursiveApproachForPart1(
            final String[] pathInstructionArray,
            final HashMap<String, LeftRightTuple> navigationMap,
            String nextPathKey,
            BigInteger steps
    ) {
        for (int i = 0; i < pathInstructionArray.length; i++) {

            final String nextDirection = pathInstructionArray[i];
            final LeftRightTuple currenctLocation = navigationMap.get(nextPathKey);
            if (Objects.equals(nextDirection, "L")) {
                nextPathKey = currenctLocation.left();
            } else if (Objects.equals(nextDirection, "R")) {
                nextPathKey = currenctLocation.right();
            }

            steps = steps.add(BigInteger.ONE);
        }

        if (!Objects.equals(nextPathKey, "ZZZ")) {
            steps = recursiveApproachForPart1(pathInstructionArray, navigationMap, nextPathKey, steps);
        } else {
            // If we are here, we reached the goal.
        }
        return steps;
    }

    private record LeftRightTuple(String left, String right) {

    }


}
