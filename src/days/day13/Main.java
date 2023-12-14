package days.day13;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13_test_01_01.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13_test_01_02.txt";
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13_test_02_01.txt";
            final String filePath = System.getProperty("user.dir") + "/resources/days/day13/input_13.txt";

        List<String> inputs = ImportUtils.readAsList(filePath);

        List<String[][]> inputsAsArray = convertInputInArrays(inputs);

        List<Reflection> horizontalReflections = new ArrayList<>();
        List<Reflection> verticalReflections = new ArrayList<>();

        for (String[][] array : inputsAsArray) {
            print(array);
           //       ReflectionTuple tuple = calculatePart1(array, null);
            ReflectionTuple tuple = calculatePart2(array);

            if (tuple.horizontalReflection() != null) {
                horizontalReflections.add(tuple.horizontalReflection());
            } else if (tuple.verticalReflection() != null) {
                verticalReflections.add(tuple.verticalReflection());
            }

        }

        int totalHorizontalReflections = horizontalReflections.stream().map(Reflection::line).reduce((a, b) -> a + b).orElse(0);
        int totalVerticalReflections = verticalReflections.stream().map(Reflection::line).reduce((a, b) -> a + b).orElse(0);

        Utils.log("Total horizontal reflections: " + totalHorizontalReflections);
        Utils.log("Total vertical reflections: " + totalVerticalReflections);

        int result = (totalHorizontalReflections * 100) + totalVerticalReflections;
        Utils.log("Total result: " + result);

    }

    private record ReflectionTuple(Reflection horizontalReflection, Reflection verticalReflection) {

    }

    private static ReflectionTuple calculatePart2(
            final String[][] array
    ) {
        ReflectionTuple ogTuple = calculatePart1(array, null);
        Reflection ogReflectionToAvoid =
                ogTuple.horizontalReflection() != null ? ogTuple.horizontalReflection() : ogTuple.verticalReflection();

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                String[][] deepCopy = deepCopy2Array(array);
                if (Objects.equals(array[i][j], "#")) {
                    deepCopy[i][j] = ".";
                    ReflectionTuple tuple = calculatePart1(deepCopy, ogReflectionToAvoid);

                    // There was no null check in part 1.
                    if ((tuple.horizontalReflection() == null && tuple.verticalReflection() == null)) {
                        continue;
                    }

               //     if (!areReflectionTuplesEqual(ogTuple, tuple)) {
                        return tuple;
                 //   }
                } else if (Objects.equals(array[i][j], ".")) {
                    deepCopy[i][j] = "#";
                    ReflectionTuple tuple = calculatePart1(deepCopy, ogReflectionToAvoid);

                    // There was no null check in part 1.
                    if ((tuple.horizontalReflection() == null && tuple.verticalReflection() == null)) {
                        continue;
                    }

                 //   if (!areReflectionTuplesEqual(ogTuple, tuple)) {
                        return tuple;
                 //   }
                }
            }
        }
        //return new ReflectionTuple(null,null);
        throw new IllegalStateException("We should have find a tuple!");
    }

    private static boolean areReflectionTuplesEqual(
            ReflectionTuple rt1,
            ReflectionTuple rt2
    ) {
        //        if(rt1.horizontalReflection() == null && rt2.horizontalReflection() != null
        //         || rt1.horizontalReflection() != null && rt2.horizontalReflection() == null
        //        ){
        //            return false;
        //        }
        //
        //        if(rt1.verticalReflection() == null && rt2.verticalReflection() != null
        //        || rt1.verticalReflection() != null && rt2.verticalReflection() == null
        //
        //        ){
        //            return false;
        //        }

        return areReflectionsEquals(rt1.horizontalReflection(), rt2.horizontalReflection())
                && areReflectionsEquals(rt1.verticalReflection(), rt2.verticalReflection());
    }

    private static boolean areReflectionsEquals(
            Reflection ref1,
            Reflection ref2
    ) {
        if ((ref1 == null && ref2 != null) || (ref1 != null && ref2 == null)) {
            return false;
        }

        if (ref1 == null && ref2 == null) {
            return true;
        }

        return ref1.reflectionLines() == ref2.reflectionLines() && ref1.line() == ref2.line() && ref1.direction() == ref2.direction();
    }

    public static String[][] deepCopy2Array(String[][] original) {
        String[][] copy = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new String[original[i].length];
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    private static ReflectionTuple calculatePart1(
            final String[][] array,
            Reflection reflectionToAvoid
    ) {

        //        print(array);

        Reflection horizontalReflection = calculateReflections(array, reflectionToAvoid, "h");

        String[][] transposedArray = transpose(array);
        Reflection verticalReflection = calculateReflections(transposedArray, reflectionToAvoid, "v");

        //        if (horizontalReflection != null || verticalReflection != null) {
        //            print(array);
        //        }

        return new ReflectionTuple(horizontalReflection, verticalReflection);
    }

    public static void print(String[][] array) {
        for (String[] row : array) {
            for (String element : row) {
                System.out.print(element);
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public static String[][] transpose(String[][] original) {
        int numRows = original.length;
        int numCols = original[0].length;

        String[][] transposed = new String[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposed[j][i] = original[i][j];
            }
        }

        return transposed;
    }

    private static Reflection calculateReflections(
            final String[][] array,
            Reflection reflectionToAvoid,
            String direction
    ) {
        Map<Integer, Integer> splitMap = new HashMap<>();
        for (int i = 0; i < array.length - 1; i++) {

            int splitAfterLine = i;
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
                boolean doWeWantToAvoidFoundReflection = reflectionToAvoid != null
                        && reflectionToAvoid.line() == splitAfterLine + 1
                        && reflectionToAvoid.reflectionLines() == reflectionCount
                        && Objects.equals(reflectionToAvoid.direction(), direction);

                if (!doWeWantToAvoidFoundReflection){
                    splitMap.put(splitAfterLine + 1, reflectionCount);
                    return new Reflection(splitAfterLine + 1, reflectionCount, direction);
                }
            }
        }

        //        if (splitMap.isEmpty()) {
        //            return null;
        //        } else {
        //            Map.Entry<Integer, Integer> maxEntry = Collections.max(splitMap.entrySet(), Map.Entry.comparingByKey());
        //            return new Reflection(maxEntry.getKey(), maxEntry.getValue());
        //        }
        return null;
    }

    private static boolean areArraysEquals(
            String[] array1,
            String[] array2
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

    //    boolean areEqual = true;
    //        for (int i = 0; i < array1.length; i++) {
    //        if (!array1[i].equals(array2[i])) {
    //            System.out.println("Unterschied bei Index " + i + ": \"" + array1[i] + "\" vs \"" + array2[i] + "\"");
    //            areEqual = false;
    //        }
    //    }

    private record Reflection(int line, int reflectionLines, String direction) {

    }

    private static List<String[][]> convertInputInArrays(final List<String> inputs) {
        List<String[][]> inputsAsArray = new ArrayList<>();

        List<String> tmpArrayList = new ArrayList<>();
        for (String input : inputs) {

            if (input.isBlank()) {

                if (!tmpArrayList.isEmpty()) {
                    String[][] array = convertListToArray(tmpArrayList);
                    inputsAsArray.add(array);
                }

                tmpArrayList = new ArrayList<>();
            } else {
                tmpArrayList.add(input);
            }
        }

        return inputsAsArray;
    }


    private static String[][] convertListToArray(List<String> list) {
        return list.stream()
                .map(element -> element.split(""))
                .toArray(String[][]::new);
    }

}