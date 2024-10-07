package year2023.day09;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Day;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/9
 */
public class Day09 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final List<Integer> allMissingNumbers = new ArrayList<>();

        for (final String inputLine : input) {

            final List<Integer> sequence = Arrays.stream(inputLine.split(" ")).filter(a -> a.trim() != "").map(Integer::valueOf).toList();

            // Comment in for part 2 solution.
            // sequence = getReverseSequenceForPart2(sequence);

            final int increment = recursiveCalculation(sequence);
            final int missingNumber = sequence.get(sequence.size() - 1) + increment;

            log(sequence + " -> " + missingNumber);
            allMissingNumbers.add(missingNumber);

        }

        return ListUtils.sumUpInt(allMissingNumbers).toString();
    }

    @Override
    public String part2(final List<String> input) {
        final List<Integer> allMissingNumbers = new ArrayList<>();

        for (final String inputLine : input) {

            final List<Integer> sequence = Arrays.stream(inputLine.split(" ")).filter(a -> a.trim() != "").map(Integer::valueOf).toList();

            // Comment in for part 2 solution.
            final List<Integer> reversedSequence = getReverseSequenceForPart2(sequence);

            final int increment = recursiveCalculation(reversedSequence);
            final int missingNumber = reversedSequence.get(reversedSequence.size() - 1) + increment;

            log(reversedSequence + " -> " + missingNumber);
            allMissingNumbers.add(missingNumber);

        }

        return ListUtils.sumUpInt(allMissingNumbers).toString();
    }

    private static List<Integer> getReverseSequenceForPart2(final List<Integer> sequence) {
        final List<Integer> reversedSequence = new ArrayList<>();
        for (int i = sequence.size() - 1; i >= 0; i--) {
            reversedSequence.add(sequence.get(i));
        }
        return reversedSequence;
    }

    private static int recursiveCalculation(final List<Integer> sequence) {
        final List<Integer> nextSequence = new ArrayList<>();
        for (int i = 0; i < sequence.size() - 1; i++) {
            final Integer difference = sequence.get(i + 1) - sequence.get(i);
            nextSequence.add(difference);
        }

        int newSummand = 0;
        final long amountZeros = nextSequence.stream().filter(a -> a == 0).count();
        if (amountZeros == nextSequence.size()) {
            newSummand = 0;
        } else {
            newSummand = recursiveCalculation(nextSequence);
        }

        return newSummand + nextSequence.get(nextSequence.size() - 1);
    }


}
