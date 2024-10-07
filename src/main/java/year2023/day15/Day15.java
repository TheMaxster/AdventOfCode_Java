package year2023.day15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/15
 */
public class Day15 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final List<String> initializationSteps = Arrays.stream(input.get(0).split(",")).toList();
        return calculatePart1(initializationSteps);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> initializationSteps = Arrays.stream(input.get(0).split(",")).toList();
        return calculatePart2(initializationSteps);
    }

    private String calculatePart1(final List<String> steps) {
        final List<Integer> currentValues = new ArrayList<>();
        for (final String step : steps) {
            final int currentValue = calculateHash(step);
            log(step + " -> " + currentValue);
            currentValues.add(currentValue);
        }

        return String.valueOf(ListUtils.sumUpInt(currentValues));
    }

    private String calculatePart2(final List<String> initializationSteps) {
        final Map<Integer, List<Lense>> boxes = new HashMap<>();
        int stepCount = 1;
        for (final String step : initializationSteps) {
            processStep(step, boxes);

            final String boxEntries = formatBoxEntries(boxes);
            log("Step " + stepCount + ": " + boxEntries);
            stepCount++;
        }

        // Calculate the focussing power.
        final int focussingPower = boxes.entrySet().stream().mapToInt(Day15::calculatePowerForBox).sum();
        return String.valueOf(focussingPower);
    }

    private static void processStep(
            final String step,
            final Map<Integer, List<Lense>> boxes
    ) {
        if (step.contains("=")) {
            final String[] labelAndFocal = step.split("=");
            final String label = labelAndFocal[0];
            final int boxNumber = calculateHash(label);
            final int focal = Integer.parseInt(labelAndFocal[1]);

            final List<Lense> lenses = boxes.getOrDefault(boxNumber, new ArrayList<>());

            final Optional<Lense> lenseOpt = lenses.stream().filter(lense -> Objects.equals(lense.getLabel(), label)).findFirst();
            lenseOpt.ifPresentOrElse(
                    lense -> lense.setFocal(focal),
                    () -> lenses.add(new Lense(label, focal))
            );

            boxes.putIfAbsent(boxNumber, lenses);

        } else if (step.contains("-")) {
            final String label = step.split("-")[0];
            final int boxNumber = calculateHash(label);

            boxes.getOrDefault(boxNumber, new ArrayList<>()).removeIf(lense -> Objects.equals(lense.getLabel(), label));
        }
    }

    private static int calculateHash(final String step) {
        int currentValue = 0;
        for (final char c : step.toCharArray()) {
            currentValue = ((currentValue + (int) c) * 17) % 256;
        }
        return currentValue;
    }

    private static String formatBoxEntries(final Map<Integer, List<Lense>> boxes) {
        return boxes.entrySet().stream()
                .map(entry -> "\n\tBox " + entry.getKey() + ": " + formatLenses(entry.getValue()))
                .collect(Collectors.joining());
    }

    private static String formatLenses(final List<Lense> lenses) {
        return lenses.stream()
                .map(Lense::toString)
                .collect(Collectors.joining(", "));
    }

    private static int calculatePowerForBox(final Map.Entry<Integer, List<Lense>> box) {
        final int firstMultiplier = box.getKey() + 1;

        int boxPower = 0;
        final List<Lense> lenses = box.getValue();
        for (int i = 0; i < lenses.size(); i++) {
            final Lense lense = lenses.get(i);
            final int lensePower = (i + 1) * lense.getFocal() * firstMultiplier;
            boxPower += lensePower;
        }
        return boxPower;
    }

    @Data
    @AllArgsConstructor
    private static class Lense {

        private String label;
        private int focal;

        public String toString() {
            return label + " " + focal;
        }
    }

}
