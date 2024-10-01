package year2023.day09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ImportUtils;
import utils.Utils;

public class Main {


    public static void main(String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day09/input_09_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day09/input_09.txt";

        List<String> inputLines = ImportUtils.readAsList(filePath);

        List<Integer> allMissingNumbers = new ArrayList<>();

        for (String inputLine : inputLines) {

            List<Integer> sequence = Arrays.stream(inputLine.split(" ")).filter(a -> a.trim() != "").map(Integer::valueOf).toList();

            // Comment in for part 2 solution.
            // sequence = getReverseSequenceForPart2(sequence);

            int increment = recursiveCalculation(sequence);
            int missingNumber = sequence.get(sequence.size() - 1) + increment;

            Utils.log(sequence + " -> " + missingNumber);
            allMissingNumbers.add(missingNumber);

        }

        int sumOfMissingNumbers = allMissingNumbers.stream().reduce(0, Integer::sum);

        Utils.log("Solution: " + sumOfMissingNumbers);

    }

    private static List<Integer> getReverseSequenceForPart2(final List<Integer> sequence) {
        List<Integer> reversedSequence = new ArrayList<>();
        for (int i = sequence.size() - 1; i >= 0; i--) {
            reversedSequence.add(sequence.get(i));
        }
        return reversedSequence;
    }

    private static int recursiveCalculation(final List<Integer> sequence) {
        List<Integer> nextSequence = new ArrayList<>();
        for (int i = 0; i < sequence.size() - 1; i++) {
            Integer difference = sequence.get(i + 1) - sequence.get(i);
            nextSequence.add(difference);
        }

        //    Utils.log(nextSequence.toString());

        int newSummand = 0;
        long amountZeros = nextSequence.stream().filter(a -> a == 0).count();
        if (amountZeros == nextSequence.size()) {
            newSummand = 0;
        } else {
            newSummand = recursiveCalculation(nextSequence);
        }

        return newSummand + nextSequence.get(nextSequence.size() - 1);
    }


}
