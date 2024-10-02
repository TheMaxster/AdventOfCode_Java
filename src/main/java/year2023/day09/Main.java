//package main.java.year2023.day09;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main {
//
//
//    public static void main(final String[] args) {
//        //  final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day09/input_test_01.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day09/input.txt";
//
//        final List<String> inputLines = ImportUtils.readAsList(filePath);
//
//        final List<Integer> allMissingNumbers = new ArrayList<>();
//
//        for (final String inputLine : inputLines) {
//
//            final List<Integer> sequence = Arrays.stream(inputLine.split(" ")).filter(a -> a.trim() != "").map(Integer::valueOf).toList();
//
//            // Comment in for part 2 solution.
//            // sequence = getReverseSequenceForPart2(sequence);
//
//            final int increment = recursiveCalculation(sequence);
//            final int missingNumber = sequence.get(sequence.size() - 1) + increment;
//
//            ArrayUtils.log(sequence + " -> " + missingNumber);
//            allMissingNumbers.add(missingNumber);
//
//        }
//
//        final int sumOfMissingNumbers = allMissingNumbers.stream().reduce(0, Integer::sum);
//
//        ArrayUtils.log("Solution: " + sumOfMissingNumbers);
//
//    }
//
//    private static List<Integer> getReverseSequenceForPart2(final List<Integer> sequence) {
//        final List<Integer> reversedSequence = new ArrayList<>();
//        for (int i = sequence.size() - 1; i >= 0; i--) {
//            reversedSequence.add(sequence.get(i));
//        }
//        return reversedSequence;
//    }
//
//    private static int recursiveCalculation(final List<Integer> sequence) {
//        final List<Integer> nextSequence = new ArrayList<>();
//        for (int i = 0; i < sequence.size() - 1; i++) {
//            final Integer difference = sequence.get(i + 1) - sequence.get(i);
//            nextSequence.add(difference);
//        }
//
//        //    Utils.log(nextSequence.toString());
//
//        int newSummand = 0;
//        final long amountZeros = nextSequence.stream().filter(a -> a == 0).count();
//        if (amountZeros == nextSequence.size()) {
//            newSummand = 0;
//        } else {
//            newSummand = recursiveCalculation(nextSequence);
//        }
//
//        return newSummand + nextSequence.get(nextSequence.size() - 1);
//    }
//
//
//}
