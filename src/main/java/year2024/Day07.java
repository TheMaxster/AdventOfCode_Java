package year2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Queue;

import org.apache.commons.lang3.StringUtils;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import utils.ArrayUtils;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/7
 */
public class Day07 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day07/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final List<Calculation> calculations = parseInput(input);
        log("Original amount: " + calculations.size());

        final List<Calculation> validSolutions = calculations.stream()
                .map(c -> doCalculations(c, false))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log("Valid solutions: " + validSolutions.size());
        final long sum = validSolutions.stream().mapToLong(Calculation::getResult).sum();
        return Long.toString(sum);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final List<Calculation> calculations = parseInput(input);
        log("Original amount: " + calculations.size());

        final List<Calculation> validSolutions = calculations.stream()
                .map(c -> doCalculations(c, true))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
        log("Valid solutions: " + validSolutions.size());
        final long sum = validSolutions.stream().mapToLong(Calculation::getResult).sum();
        return Long.toString(sum);
    }

    private Optional<Calculation> doCalculations(
            final Calculation calculation,
            final boolean activatePart2Operator
    ) {
        final Queue<QueueElement> queue = initializeQueue(calculation);
        final boolean solveable = processCalculation(queue, activatePart2Operator);
        if (solveable) {
            return Optional.of(calculation);
        } else {
            return Optional.empty();
        }
    }

    private boolean processCalculation(
            final Queue<QueueElement> queue,
            final boolean activatePart2Operator
    ) {
        boolean calculationIsSolvable = false;
        while (!queue.isEmpty()) {
            final QueueElement qel = queue.poll();

            final Long tmpVal = qel.getTmp();
            final Long resultVal = qel.getResult();
            if (tmpVal > resultVal) {
                continue; // We are on the wrong path.
            }

            final Long[] remainingValues = qel.getValues();
            if (tmpVal.equals(resultVal) && (remainingValues == null || remainingValues.length == 0)) {
                log(qel.getCalc());
                calculationIsSolvable = true;
                break; // We found a solution.
            }

            if (remainingValues == null || remainingValues.length == 0) {
                continue; // We are on the wrong path.
            }

            final Long numberForMath = remainingValues[0];
            final Long[] reducedValues = ArrayUtils.removeFirstOccurrence(remainingValues, numberForMath);

            // Doing + operator
            final QueueElement qelPlus = new QueueElement();
            qelPlus.setResult(resultVal);
            qelPlus.setCalc(qel.getCalc() + " + " + numberForMath);
            qelPlus.setTmp(tmpVal + numberForMath);
            qelPlus.setValues(reducedValues);
            queue.add(qelPlus);

            // Doing * operator
            final QueueElement qelMultiply = new QueueElement();
            qelMultiply.setResult(resultVal);
            qelMultiply.setCalc(qel.getCalc() + " * " + numberForMath);
            qelMultiply.setTmp(tmpVal * numberForMath);
            qelMultiply.setValues(reducedValues);
            queue.add(qelMultiply);

            if (activatePart2Operator) {
                // Part 2 operator
                // Doing * operator
                final QueueElement qelConcat = new QueueElement();
                qelConcat.setResult(resultVal);
                qelConcat.setCalc(qel.getCalc() + " || " + numberForMath);
                qelConcat.setTmp(Long.parseLong(tmpVal + "" + numberForMath));
                qelConcat.setValues(reducedValues);
                queue.add(qelConcat);
            }
        }

        return calculationIsSolvable;
    }

    private Queue<QueueElement> initializeQueue(final Calculation calculation) {
        final Long result = calculation.getResult();
        final Long[] values = calculation.getValues();

        final Queue<QueueElement> queue = new ArrayDeque<>();
        final QueueElement qel = new QueueElement();
        qel.setResult(result);
        qel.setTmp(values[0]);
        qel.setValues(ArrayUtils.removeFirstOccurrence(values, values[0]));
        qel.setCalc(values[0].toString());
        queue.add(qel);
        return queue;
    }

    private List<Calculation> parseInput(final List<String> importInput) {
        final List<Calculation> calculations = new ArrayList<>();

        for (final String s : importInput) {
            final String[] split1 = s.split(":");
            final Long result = Long.parseLong(split1[0]);

            final String[] split2 = split1[1].split(" ");
            final Long[] values = Arrays.stream(split2)
                    .filter(StringUtils::isNumeric)
                    .map(String::trim)
                    .map(Long::parseLong)
                    .toArray(Long[]::new);

            calculations.add(new Calculation(values, result));
        }

        return calculations;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Data
    @AllArgsConstructor
    public static class Calculation {

        Long[] values;
        Long result;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class QueueElement {

        Long[] values;
        Long result;
        Long tmp;
        String calc;
    }
}
