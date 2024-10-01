package year2023.day06;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day06/input_06_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day06/input_06.txt";

        List<String> inputList = ImportUtils.readAsList(filePath);

//        List<String> timeList = getNumbersForPart1(inputList.get(0));
//        List<String> distanceList = getNumbersForPart1(inputList.get(1));

        List<String> timeList = getNumbersForPart2(inputList.get(0));
        List<String> distanceList = getNumbersForPart2(inputList.get(1));

        List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            tuples.add(new Tuple(
                    new BigInteger(timeList.get(i)),
                    new BigInteger(distanceList.get(i))
            ));
        }

        Utils.log("Our tuples: " + tuples.toString());

        List<Integer> solutionsPerTuple = new ArrayList<>();
        for (Tuple tuple : tuples) {
            int solutions = 0;
            BigInteger time = tuple.time();
            BigInteger distance = tuple.distance();

            for (int speed = 1; speed < time.intValue(); speed++) {

                // Check: (time-speed)*speed > distance
                BigInteger timeMinusSpeed = time.subtract(BigInteger.valueOf(speed));
                if (BigInteger.valueOf(speed).multiply(timeMinusSpeed).compareTo(distance) > 0) {
                    solutions++;
                }
            }

            if (solutions != 0) {
                solutionsPerTuple.add(solutions);
                Utils.log("Solutions per Tuple: " + solutions + " " + tuple);
            }

        }

        int product = 1;
        for (int number : solutionsPerTuple) {
            product *= number;
        }

        Utils.log("Solution: " + product);


    }

    private static List<String> getNumbersForPart1(String input) {
        String[] splitLeftRight = input.split(":");
        String[] rightSide = splitLeftRight[1].split(" ");
        return Arrays.stream(rightSide).map(String::trim).filter(s -> s != "").toList();
    }

    private static List<String> getNumbersForPart2(String input) {
        String[] splitLeftRight = input.split(":");
        return List.of(splitLeftRight[1].replaceAll(" ", ""));
    }

    private record Tuple(BigInteger time, BigInteger distance) {    }

}
