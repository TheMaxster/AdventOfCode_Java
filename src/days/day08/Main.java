package days.day08;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {


    public static void main(String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_01.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_02.txt";
      //   final String filePath = System.getProperty("user.dir") + "/resources/days/day08/input_08_test_03.txt";
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

        // Part 2: Define our starting keys.
        List<String> startingKeys = new ArrayList<>();
        for (Map.Entry<String, LeftRightTuple> entry : navigationMap.entrySet()) {
            if (entry.getKey().endsWith("A")) {
                startingKeys.add(entry.getKey());
            }
        }

        //        BigInteger steps = recursiveApproachForPart2(pathInstructionArray, navigationMap, startingKeys.toArray(new String[0]),
        //                BigInteger.ZERO);
        BigInteger steps = iterativeApproachForPart2(pathInstructionArray, navigationMap, startingKeys);

        Utils.log("Solution Part 2: We reached **Z in steps: " + steps.toString());

    }

    private static BigInteger iterativeApproachForPart2(
            final String[] pathInstructionArray,
            final HashMap<String, LeftRightTuple> navigationMap,
            List<String> startingKeys
    ) {

        int pathsThatEndWithZ;
        int numberOfPaths = startingKeys.size();

        BigInteger steps = BigInteger.ZERO;

        do {
            List<String> newNextPathKeys = new ArrayList<>();

            for (int i = 0; i < pathInstructionArray.length; i++) {
                final String nextDirection = pathInstructionArray[i];

                if (!newNextPathKeys.isEmpty()) {
                    startingKeys = newNextPathKeys;
                    // Utils.log("New Next Path Keys: " + newNextPathKeys);
                    newNextPathKeys = new ArrayList<>();
                }

                if (Objects.equals(nextDirection, "L")) {
                    newNextPathKeys.addAll(startingKeys.parallelStream()
                            .map(navigationMap::get)
                            .map(LeftRightTuple::left)
                            .toList()
                    );

                } else if (Objects.equals(nextDirection, "R")) {
                    newNextPathKeys.addAll(startingKeys.parallelStream()
                            .map(navigationMap::get)
                            .map(LeftRightTuple::right)
                            .toList()
                    );
                }
                steps = steps.add(BigInteger.ONE);
            }

            pathsThatEndWithZ = (int) newNextPathKeys.stream()
                    .filter(newNextPathKey -> newNextPathKey.endsWith("Z"))
                    .count();

            startingKeys = newNextPathKeys;
        } while (numberOfPaths != pathsThatEndWithZ);

        return steps;
    }

    private static BigInteger recursiveApproachForPart2(
            final String[] pathInstructionArray,
            final HashMap<String, LeftRightTuple> navigationMap,
            String[] nextPathKeys,
            BigInteger steps
    ) {

        List<String> newNextPathKeys = new ArrayList<>();

        int pathsThatEndWithZ = 0;

        for (int i = 0; i < pathInstructionArray.length; i++) {
            pathsThatEndWithZ = 0;
            final String nextDirection = pathInstructionArray[i];

            if (!newNextPathKeys.isEmpty()) {
                nextPathKeys = newNextPathKeys.toArray(new String[0]);
                Utils.log("New Next Path Keys: " + newNextPathKeys);
                newNextPathKeys = new ArrayList<>();
            }

            for (final String nextPathKey : nextPathKeys) {

                LeftRightTuple currenctLocation = navigationMap.get(nextPathKey);

                String newNextPathKey = null;
                if (Objects.equals(nextDirection, "L")) {
                    newNextPathKey = currenctLocation.left();
                } else if (Objects.equals(nextDirection, "R")) {
                    newNextPathKey = currenctLocation.right();
                }

                newNextPathKeys.add(newNextPathKey);
                if (newNextPathKey.endsWith("Z")) {
                    pathsThatEndWithZ++;
                }
            }
            steps = steps.add(BigInteger.ONE);
        }

        if (newNextPathKeys.size() != pathsThatEndWithZ) {
            steps = recursiveApproachForPart2(pathInstructionArray, navigationMap, newNextPathKeys.toArray(new String[0]), steps);
        } else {
            // If we are here, we reached the goal.
        }

        return steps;

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