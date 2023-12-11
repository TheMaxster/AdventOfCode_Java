package days.day08;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {


    public static void main(String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_01.txt";
        //   final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_02.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_03.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08.txt";

        List<String> importList = ImportUtils.readAsList(filePath);

        String pathInstruction = importList.get(0);

        HashMap<String, LeftRightTuple> navigationMap = new HashMap<>();

        for (int i = 2; i < importList.size(); i++) {
            String[] equationSplit = importList.get(i).split("=");
            String key = equationSplit[0].trim();
            String[] leftRightPair = equationSplit[1].split(",");
            String left = leftRightPair[0].replace("(", "").trim();
            String right = leftRightPair[1].replace(")", "").trim();
            LeftRightTuple leftRightTuple = new LeftRightTuple(left, right);
            navigationMap.put(key, leftRightTuple);
        }

        String[] pathInstructionArray = pathInstruction.split("");
        Utils.log("Path: " + pathInstruction);

        //        // Part 1: Define our starting key.
        //        String startingKey = "AAA";
        //        BigInteger steps = recursiveApproachForPart1(pathInstructionArray, navigationMap, startingKey, BigInteger.ZERO);
        //
        //        Utils.log("Solution Part 1: We reached ZZZ in steps: " + steps);

        // ---------------------------------------------------------------------------------------------------

        // Part 2: Define our starting keys.
        List<String> startingKeys = new ArrayList<>();
        for (Map.Entry<String, LeftRightTuple> entry : navigationMap.entrySet()) {
            if (entry.getKey().endsWith("A")) {
                startingKeys.add(entry.getKey());
            }
        }

        // IMPORTANT: This approach is brute force, but more correct than the approach with least common multiple. The lcm approach only works with special test data.

        Map<String, String> newNavigationMap = createNavigationMapForFullPath(pathInstructionArray, navigationMap);
        long multiplier = pathInstructionArray.length;
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

        Utils.log("Solution Part 2: We reached **Z in steps: " + (result * multiplier));

    }

    private static void iterativeLeastCommonMultipleApproachForPart2(
            final Map<String, String> newNavigationMap,
            List<String> startingKeys,
            Map<String, Long> paths
    ) {

        for (int i = 0; i < startingKeys.size(); i++) {
            paths.put(startingKeys.get(i), 0L);
        }

        for (int i = 0; i < startingKeys.size(); i++) {
            long steps = 0L;

            String startingKey = startingKeys.get(i);

            String tmpNode = startingKey;
            while (!tmpNode.endsWith("Z")) {
                tmpNode = newNavigationMap.get(tmpNode);
                steps++;
            }

            paths.put(startingKey, steps);
        }
    }

    private static long leastCommonMultiple(
            long number1,
            long number2
    ) {
        long absNumber1 = Math.abs(number1);
        long absNumber2 = Math.abs(number2);
        long absHigherNumber = Math.max(absNumber1, absNumber2);
        long absLowerNumber = Math.min(absNumber1, absNumber2);
        long lcm = absHigherNumber;
        while (lcm % absLowerNumber != 0) {
            lcm += absHigherNumber;
        }
        return lcm;
    }


    private static long iterativeBruteForceApproachForPart2(
            final Map<String, String> newNavigationMap,
            List<String> startingKeys
    ) {

        long pathsThatEndWithZ = 0;
        long numberOfPaths = startingKeys.size();

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
            Utils.log(String.valueOf(steps));
        }

        return steps;
    }

    private static HashMap<String, String> createNavigationMapForFullPath(
            final String[] pathInstructionArray,
            final HashMap<String, LeftRightTuple> navigationMap
    ) {

        HashMap<String, String> newNavigationMap = new HashMap<>();
        for (Map.Entry<String, LeftRightTuple> entry : navigationMap.entrySet()) {

            String start = entry.getKey();
            String nextPathKey = entry.getKey();

            for (int i = 0; i < pathInstructionArray.length; i++) {
                final String nextDirection = pathInstructionArray[i];

                LeftRightTuple currenctLocation = navigationMap.get(nextPathKey);
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

            String nextDirection = pathInstructionArray[i];
            LeftRightTuple currenctLocation = navigationMap.get(nextPathKey);
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