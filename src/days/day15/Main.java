package days.day15;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day15/input_15_test_01_01.txt";
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day15/input_15_test_01_02.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day15/input_15.txt";

        List<String> inputList = ImportUtils.readAsList(filePath);
        String input = inputList.get(0);

        List<String> initializationSteps = Arrays.stream(input.split(",")).toList();

        calculatePart1(initializationSteps);
        calculatePart2(initializationSteps);

    }

    private static void calculatePart1(final List<String> steps) {
        List<Integer> currentValues = new ArrayList<>();
        for (String step : steps) {
            final int currentValue = calculateHash(step);
            Utils.log(step + " -> " + currentValue);
            currentValues.add(currentValue);
        }

        int sumOfCurrentValues = Utils.sumUp(currentValues);
        Utils.log("Part 1 Total sum: " + sumOfCurrentValues);
    }

    private static void calculatePart2(final List<String> initializationSteps) {
        Map<Integer, List<Lense>> boxes = new HashMap<>();
        int stepCount = 1;
        for (String step : initializationSteps) {
            processStep(step, boxes);

            final String boxEntries = formatBoxEntries(boxes);
            Utils.log("Step " + stepCount + ": " + boxEntries);
            stepCount++;
        }

        // Calculate the focussing power.
        int focussingPower = boxes.entrySet().stream().mapToInt(Main::calculatePowerForBox).sum();
        Utils.log("Part 2 Total sum: " + focussingPower);
    }

    private static void processStep(
            final String step,
            final Map<Integer, List<Lense>> boxes
    ) {
        if (step.contains("=")) {
            String[] labelAndFocal = step.split("=");
            String label = labelAndFocal[0];
            int boxNumber = calculateHash(label);
            int focal = Integer.parseInt(labelAndFocal[1]);

            List<Lense> lenses = boxes.getOrDefault(boxNumber, new ArrayList<>());

            Optional<Lense> lenseOpt = lenses.stream().filter(lense -> Objects.equals(lense.getLabel(), label)).findFirst();
            lenseOpt.ifPresentOrElse(
                    lense -> lense.setFocal(focal),
                    () -> lenses.add(new Lense(label, focal))
            );

            boxes.putIfAbsent(boxNumber, lenses);

        } else if (step.contains("-")) {
            String label = step.split("-")[0];
            int boxNumber = calculateHash(label);

            boxes.getOrDefault(boxNumber, new ArrayList<>()).removeIf(lense -> Objects.equals(lense.getLabel(), label));
        }
    }

    private static int calculateHash(final String step) {
        int currentValue = 0;
        for (char c : step.toCharArray()) {
            currentValue = ((currentValue + (int) c) * 17) % 256;
        }
        return currentValue;
    }

    private static String formatBoxEntries(Map<Integer, List<Lense>> boxes) {
        return boxes.entrySet().stream()
                .map(entry -> "\n\tBox " + entry.getKey() + ": " + formatLenses(entry.getValue()))
                .collect(Collectors.joining());
    }

    private static String formatLenses(List<Lense> lenses) {
        return lenses.stream()
                .map(Lense::toString)
                .collect(Collectors.joining(", "));
    }

    private static int calculatePowerForBox(final Map.Entry<Integer, List<Lense>> box) {
        int firstMultiplier = box.getKey() + 1;

        int boxPower = 0;
        List<Lense> lenses = box.getValue();
        for (int i = 0; i < lenses.size(); i++) {
            Lense lense = lenses.get(i);
            int lensePower = (i + 1) * lense.getFocal() * firstMultiplier;
            boxPower += lensePower;
        }
        return boxPower;
    }

    private static class Lense {

        private String label;
        private int focal;

        public Lense(
                final String label,
                final int focal
        ) {
            this.label = label;
            this.focal = focal;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(final String label) {
            this.label = label;
        }

        public int getFocal() {
            return focal;
        }

        public void setFocal(final int focal) {
            this.focal = focal;
        }

        public String toString() {
            return label + " " + focal;
        }
    }

}