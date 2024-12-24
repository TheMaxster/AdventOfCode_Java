package year2024;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/13
 */
public class Day13 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day13/input_test_01.txt";
    private static final int COST_A = 3;
    private static final int COST_B = 1;
    private static final Pattern PATTERN_BUTTON_A = Pattern.compile("Button A: X\\+(\\d+), Y\\+(\\d+)");
    private static final Pattern PATTERN_BUTTON_B = Pattern.compile("Button B: X\\+(\\d+), Y\\+(\\d+)");
    private static final Pattern PATTERN_PRIZE = Pattern.compile("Prize: X=(\\d+), Y=(\\d+)");

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final List<InputData> inputs = parseInput(input);

        final long offset = 0L;
        final long limit = 100;

        final List<Pair<Long, Long>> results = inputs.stream()
                .map(data -> solveEquations(data, offset, limit))
                .flatMap(Optional::stream)
                .toList();

        final long totalCostA = COST_A * results.stream().mapToLong(Pair::getLeft).sum();
        final long totalCostB = COST_B * results.stream().mapToLong(Pair::getRight).sum();

        return String.valueOf(totalCostA + totalCostB);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final List<InputData> inputs = parseInput(input);

        final long offset = 10000000000000L;
        final long limit = Long.MAX_VALUE;

        final List<Pair<Long, Long>> results = inputs.stream()
                .map(data -> solveEquations(data, offset, limit))
                .flatMap(Optional::stream)
                .toList();

        final long totalCostA = COST_A * results.stream().mapToLong(Pair::getLeft).sum();
        final long totalCostB = COST_B * results.stream().mapToLong(Pair::getRight).sum();

        return String.valueOf(totalCostA + totalCostB);
    }

    private Optional<Pair<Long, Long>> solveEquations(
            final InputData inputData,
            final long offset,
            final long limit
    ) {
        // We have to solve two equations of kind: a1*x+b1*y=c1, where a and b and c are known.
        // We can set them equal so that we only have to solve x.

        final long Ax = inputData.getButtonA().getLeft();
        final long Ay = inputData.getButtonA().getRight();
        final long Bx = inputData.getButtonB().getLeft();
        final long By = inputData.getButtonB().getRight();
        final long prizeX = inputData.getPrize().getLeft() + offset;
        final long prizeY = inputData.getPrize().getRight() + offset;

        final long xMul = ((prizeX * By) - (prizeY * Bx)) / ((Ax * By) - (Ay * Bx));
        log("x: " + xMul);

        if (!isLong(xMul) || xMul > limit || xMul <= 0) {
            return Optional.empty();
        }

        final long yMul = (prizeX - Ax * xMul) / Bx;
        log("y: " + yMul);

        if (!isLong(yMul) || yMul > limit || yMul <= 0) {
            return Optional.empty();
        }

        // Double check
        final boolean firstCheck = xMul * Ax + yMul * Bx == prizeX;
        final boolean secondCheck = xMul * Ay + yMul * By == prizeY;
        if (!firstCheck || !secondCheck) {
            return Optional.empty();
        }

        return Optional.of(Pair.of(xMul, yMul));
    }

    private static boolean isLong(final double number) {
        return number == Math.floor(number);
    }

    private List<InputData> parseInput(final List<String> input) {
        final List<InputData> result = new ArrayList<>();

        for (int i = 0; i < input.size(); i += 4) {
            final String lineA = input.get(i);
            final String lineB = input.get(i + 1);
            final String linePrize = input.get(i + 2);

            final Pair<Long, Long> buttonA = parseCoordinate(lineA, PATTERN_BUTTON_A);
            final Pair<Long, Long> buttonB = parseCoordinate(lineB, PATTERN_BUTTON_B);
            final Pair<Long, Long> prize = parseCoordinate(linePrize, PATTERN_PRIZE);
            result.add(new InputData(buttonA, buttonB, prize));
        }

        return result;
    }

    private static Pair<Long, Long> parseCoordinate(
            final String line,
            final Pattern pattern
    ) {
        final Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            final long x = Long.parseLong(matcher.group(1));
            final long y = Long.parseLong(matcher.group(2));
            return Pair.of(x, y);
        }
        throw new IllegalArgumentException("Invalid line format: " + line);
    }

    @Data
    @AllArgsConstructor
    private class InputData {

        Pair<Long, Long> buttonA;
        Pair<Long, Long> buttonB;
        Pair<Long, Long> prize;

        @Override
        public String toString() {
            return "Button A: " + buttonA + ", Button B: " + buttonB + ", Prize: " + prize;
        }
    }


}
