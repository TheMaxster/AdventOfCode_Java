//package main.java.year2023.day06;
//
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main {
//
//    public static void main(final String[] args) {
//        // final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day06/input_test_01.txt";
//        final String filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day06/input.txt";
//
//        final List<String> inputList = ImportUtils.readAsList(filePath);
//
//        //        List<String> timeList = getNumbersForPart1(inputList.get(0));
//        //        List<String> distanceList = getNumbersForPart1(inputList.get(1));
//
//        final List<String> timeList = getNumbersForPart2(inputList.get(0));
//        final List<String> distanceList = getNumbersForPart2(inputList.get(1));
//
//        final List<Tuple> tuples = new ArrayList<>();
//        for (int i = 0; i < timeList.size(); i++) {
//            tuples.add(new Tuple(
//                    new BigInteger(timeList.get(i)),
//                    new BigInteger(distanceList.get(i))
//            ));
//        }
//
//        ArrayUtils.log("Our tuples: " + tuples.toString());
//
//        final List<Integer> solutionsPerTuple = new ArrayList<>();
//        for (final Tuple tuple : tuples) {
//            int solutions = 0;
//            final BigInteger time = tuple.time();
//            final BigInteger distance = tuple.distance();
//
//            for (int speed = 1; speed < time.intValue(); speed++) {
//
//                // Check: (time-speed)*speed > distance
//                final BigInteger timeMinusSpeed = time.subtract(BigInteger.valueOf(speed));
//                if (BigInteger.valueOf(speed).multiply(timeMinusSpeed).compareTo(distance) > 0) {
//                    solutions++;
//                }
//            }
//
//            if (solutions != 0) {
//                solutionsPerTuple.add(solutions);
//                ArrayUtils.log("Solutions per Tuple: " + solutions + " " + tuple);
//            }
//
//        }
//
//        int product = 1;
//        for (final int number : solutionsPerTuple) {
//            product *= number;
//        }
//
//        ArrayUtils.log("Solution: " + product);
//
//
//    }
//
//    private static List<String> getNumbersForPart1(final String input) {
//        final String[] splitLeftRight = input.split(":");
//        final String[] rightSide = splitLeftRight[1].split(" ");
//        return Arrays.stream(rightSide).map(String::trim).filter(s -> s != "").toList();
//    }
//
//    private static List<String> getNumbersForPart2(final String input) {
//        final String[] splitLeftRight = input.split(":");
//        return List.of(splitLeftRight[1].replaceAll(" ", ""));
//    }
//
//    private record Tuple(BigInteger time, BigInteger distance) {
//
//    }
//
//}
