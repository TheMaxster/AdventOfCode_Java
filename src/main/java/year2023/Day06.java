package year2023;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/6
 */
public class Day06 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final List<String> timeList = getNumbersForPart1(input.get(0));
        final List<String> distanceList = getNumbersForPart1(input.get(1));
        return calculateSolution(timeList, distanceList);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> timeList = getNumbersForPart2(input.get(0));
        final List<String> distanceList = getNumbersForPart2(input.get(1));
        return calculateSolution(timeList, distanceList);
    }

    private String calculateSolution(
            final List<String> timeList,
            final List<String> distanceList
    ) {
        final List<Tuple> tuples = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            tuples.add(new Tuple(
                    new BigInteger(timeList.get(i)),
                    new BigInteger(distanceList.get(i))
            ));
        }

        log("Our tuples: " + tuples.toString());

        final List<Integer> solutionsPerTuple = new ArrayList<>();
        for (final Tuple tuple : tuples) {
            int solutions = 0;
            final BigInteger time = tuple.getTime();
            final BigInteger distance = tuple.getDistance();

            for (int speed = 1; speed < time.intValue(); speed++) {

                // Check: (time-speed)*speed > distance
                final BigInteger timeMinusSpeed = time.subtract(BigInteger.valueOf(speed));
                if (BigInteger.valueOf(speed).multiply(timeMinusSpeed).compareTo(distance) > 0) {
                    solutions++;
                }
            }

            if (solutions != 0) {
                solutionsPerTuple.add(solutions);
                log("Solutions per Tuple: " + solutions + " " + tuple);
            }

        }

        return ListUtils.multiplyUpInt(solutionsPerTuple).toString();
    }

    private static List<String> getNumbersForPart1(final String input) {
        final String[] splitLeftRight = input.split(":");
        final String[] rightSide = splitLeftRight[1].split(" ");
        return Arrays.stream(rightSide).map(String::trim).filter(s -> s != "").toList();
    }

    private static List<String> getNumbersForPart2(final String input) {
        final String[] splitLeftRight = input.split(":");
        return List.of(splitLeftRight[1].replaceAll(" ", ""));
    }

    @Data
    @AllArgsConstructor
    private class Tuple {

        BigInteger time;
        BigInteger distance;
    }

}
