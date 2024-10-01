package year2023.day13;

import static utils.Utils.printMap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(final String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/year2023/day13/input_test_01_01.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/year2023/day13/input_test_01_02.txt";
        //final String filePath = System.getProperty("user.dir") + "/resources/year2023/day13/input_test_02_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day13/input.txt";

        final List<String> inputs = ImportUtils.readAsList(filePath);

        final List<String[][]> inputsAsArray = convertInputInArrays(inputs);

        final List<Reflection> horizontalReflections = new ArrayList<>();
        final List<Reflection> verticalReflections = new ArrayList<>();

        for (final String[][] array : inputsAsArray) {
            printMap(array);
            //  ReflectionTuple tuple = calculatePart1(array, null);
            final ReflectionTuple tuple = calculatePart2(array);

            if (tuple.horizontalReflection() != null) {
                horizontalReflections.add(tuple.horizontalReflection());
            } else if (tuple.verticalReflection() != null) {
                verticalReflections.add(tuple.verticalReflection());
            }

        }

        final int totalHorizontalReflections = horizontalReflections.stream().map(Reflection::line).reduce(Integer::sum).orElse(0);
        final int totalVerticalReflections = verticalReflections.stream().map(Reflection::line).reduce(Integer::sum).orElse(0);

        Utils.log("Total horizontal reflections: " + totalHorizontalReflections);
        Utils.log("Total vertical reflections: " + totalVerticalReflections);

        final int result = (totalHorizontalReflections * 100) + totalVerticalReflections;
        Utils.log("Total result: " + result);

    }

    private static List<String[][]> convertInputInArrays(final List<String> inputs) {
        final List<String[][]> inputsAsArray = new ArrayList<>();

        List<String> tmpArrayList = new ArrayList<>();
        for (final String input : inputs) {

            if (input.isBlank()) {

                if (!tmpArrayList.isEmpty()) {
                    final String[][] array = convertListToArray(tmpArrayList);
                    inputsAsArray.add(array);
                }

                tmpArrayList = new ArrayList<>();
            } else {
                tmpArrayList.add(input);
            }
        }

        return inputsAsArray;
    }

    private static String[][] convertListToArray(final List<String> list) {
        return list.stream()
                .map(element -> element.split(""))
                .toArray(String[][]::new);
    }


    private static ReflectionTuple calculatePart2(
            final String[][] array
    ) {
        final ReflectionTuple ogTuple = calculatePart1(array, null);
        final Reflection ogReflectionToAvoid =
                ogTuple.horizontalReflection() != null ? ogTuple.horizontalReflection() : ogTuple.verticalReflection();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                final String[][] deepCopy = Utils.deepCopy2Array(array);
                if (Objects.equals(array[i][j], "#")) {
                    deepCopy[i][j] = ".";
                    final ReflectionTuple tuple = calculatePart1(deepCopy, ogReflectionToAvoid);

                    // There was no null check in part 1.
                    if ((tuple.horizontalReflection() == null && tuple.verticalReflection() == null)) {
                        continue;
                    }

                    return tuple;
                } else if (Objects.equals(array[i][j], ".")) {
                    deepCopy[i][j] = "#";
                    final ReflectionTuple tuple = calculatePart1(deepCopy, ogReflectionToAvoid);

                    // There was no null check in part 1.
                    if ((tuple.horizontalReflection() == null && tuple.verticalReflection() == null)) {
                        continue;
                    }

                    return tuple;
                }
            }
        }

        throw new IllegalStateException("We should have find a tuple!");
    }

    private static ReflectionTuple calculatePart1(
            final String[][] array,
            final Reflection reflectionToAvoid
    ) {

        //        print(array);

        final Reflection horizontalReflection = calculateReflections(array, reflectionToAvoid, "h");

        final String[][] transposedArray = Utils.transpose(array);
        final Reflection verticalReflection = calculateReflections(transposedArray, reflectionToAvoid, "v");

        //        if (horizontalReflection != null || verticalReflection != null) {
        //            print(array);
        //        }

        return new ReflectionTuple(horizontalReflection, verticalReflection);
    }

    private static Reflection calculateReflections(
            final String[][] array,
            final Reflection reflectionToAvoid,
            final String direction
    ) {
        final Map<Integer, Integer> splitMap = new HashMap<>();
        for (int i = 0; i < array.length - 1; i++) {

            final int splitAfterLine = i;
            int reflectionCount = 0;
            boolean isReflectionPerfect = false;

            for (int j = 0; j < array.length - 1; j++) {

                if (i + j >= array.length) {
                    isReflectionPerfect = true;
                    break;
                }

                if (i + 1 - j < 0) {
                    isReflectionPerfect = true;
                    break;
                }

                if (areArraysEquals(array[i + 1 - j], array[i + j])) {
                    //if (Arrays.equals(array[i + 1 - j], array[i + j])) {
                    reflectionCount++;
                } else {
                    break;
                }

            }

            if (isReflectionPerfect) {
                final boolean doWeWantToAvoidFoundReflection = reflectionToAvoid != null
                        && reflectionToAvoid.line() == splitAfterLine + 1
                        && reflectionToAvoid.reflectionLines() == reflectionCount
                        && Objects.equals(reflectionToAvoid.direction(), direction);

                if (!doWeWantToAvoidFoundReflection) {
                    splitMap.put(splitAfterLine + 1, reflectionCount);
                    return new Reflection(splitAfterLine + 1, reflectionCount, direction);
                }
            }
        }

        return null;
    }

    private static boolean areArraysEquals(
            final String[] array1,
            final String[] array2
    ) {
        boolean areEqual = true;
        for (int i = 0; i < array1.length; i++) {
            if (!array1[i].equals(array2[i])) {
                //        System.out.println("Unterschied bei Index " + i + ": \"" + array1[i] + "\" vs \"" + array2[i] + "\"");
                areEqual = false;
            }
        }
        return areEqual;
    }

    private record ReflectionTuple(Reflection horizontalReflection, Reflection verticalReflection) {

    }

    private record Reflection(int line, int reflectionLines, String direction) {

    }

}
