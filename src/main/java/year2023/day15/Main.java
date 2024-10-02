//package main.java.year2023.day15;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//import utils.ListUtils;
//
//public class Main {
//
//    public static void main(final String[] args) {
//        // final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day15/input_test_01_01.txt";
//        //final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day15/input_test_01_02.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day15/input.txt";
//
//        final List<String> inputList = ImportUtils.readAsList(filePath);
//        final String input = inputList.get(0);
//
//        final List<String> initializationSteps = Arrays.stream(input.split(",")).toList();
//
//        calculatePart1(initializationSteps);
//        calculatePart2(initializationSteps);
//
//    }
//
//    private static void calculatePart1(final List<String> steps) {
//        final List<Integer> currentValues = new ArrayList<>();
//        for (final String step : steps) {
//            final int currentValue = calculateHash(step);
//            ArrayUtils.log(step + " -> " + currentValue);
//            currentValues.add(currentValue);
//        }
//
//        final int sumOfCurrentValues = ListUtils.sumUpInt(currentValues);
//        ArrayUtils.log("Part 1 Total sum: " + sumOfCurrentValues);
//    }
//
//    private static void calculatePart2(final List<String> initializationSteps) {
//        final Map<Integer, List<Lense>> boxes = new HashMap<>();
//        int stepCount = 1;
//        for (final String step : initializationSteps) {
//            processStep(step, boxes);
//
//            final String boxEntries = formatBoxEntries(boxes);
//            ArrayUtils.log("Step " + stepCount + ": " + boxEntries);
//            stepCount++;
//        }
//
//        // Calculate the focussing power.
//        final int focussingPower = boxes.entrySet().stream().mapToInt(Main::calculatePowerForBox).sum();
//        ArrayUtils.log("Part 2 Total sum: " + focussingPower);
//    }
//
//    private static void processStep(
//            final String step,
//            final Map<Integer, List<Lense>> boxes
//    ) {
//        if (step.contains("=")) {
//            final String[] labelAndFocal = step.split("=");
//            final String label = labelAndFocal[0];
//            final int boxNumber = calculateHash(label);
//            final int focal = Integer.parseInt(labelAndFocal[1]);
//
//            final List<Lense> lenses = boxes.getOrDefault(boxNumber, new ArrayList<>());
//
//            final Optional<Lense> lenseOpt = lenses.stream().filter(lense -> Objects.equals(lense.getLabel(), label)).findFirst();
//            lenseOpt.ifPresentOrElse(
//                    lense -> lense.setFocal(focal),
//                    () -> lenses.add(new Lense(label, focal))
//            );
//
//            boxes.putIfAbsent(boxNumber, lenses);
//
//        } else if (step.contains("-")) {
//            final String label = step.split("-")[0];
//            final int boxNumber = calculateHash(label);
//
//            boxes.getOrDefault(boxNumber, new ArrayList<>()).removeIf(lense -> Objects.equals(lense.getLabel(), label));
//        }
//    }
//
//    private static int calculateHash(final String step) {
//        int currentValue = 0;
//        for (final char c : step.toCharArray()) {
//            currentValue = ((currentValue + (int) c) * 17) % 256;
//        }
//        return currentValue;
//    }
//
//    private static String formatBoxEntries(final Map<Integer, List<Lense>> boxes) {
//        return boxes.entrySet().stream()
//                .map(entry -> "\n\tBox " + entry.getKey() + ": " + formatLenses(entry.getValue()))
//                .collect(Collectors.joining());
//    }
//
//    private static String formatLenses(final List<Lense> lenses) {
//        return lenses.stream()
//                .map(Lense::toString)
//                .collect(Collectors.joining(", "));
//    }
//
//    private static int calculatePowerForBox(final Map.Entry<Integer, List<Lense>> box) {
//        final int firstMultiplier = box.getKey() + 1;
//
//        int boxPower = 0;
//        final List<Lense> lenses = box.getValue();
//        for (int i = 0; i < lenses.size(); i++) {
//            final Lense lense = lenses.get(i);
//            final int lensePower = (i + 1) * lense.getFocal() * firstMultiplier;
//            boxPower += lensePower;
//        }
//        return boxPower;
//    }
//
//    private static class Lense {
//
//        private String label;
//        private int focal;
//
//        public Lense(
//                final String label,
//                final int focal
//        ) {
//            this.label = label;
//            this.focal = focal;
//        }
//
//        public String getLabel() {
//            return label;
//        }
//
//        public void setLabel(final String label) {
//            this.label = label;
//        }
//
//        public int getFocal() {
//            return focal;
//        }
//
//        public void setFocal(final int focal) {
//            this.focal = focal;
//        }
//
//        public String toString() {
//            return label + " " + focal;
//        }
//    }
//
//}
