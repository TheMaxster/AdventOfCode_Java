package year2024.day08;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/8
 */
public class Day08 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day08/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] map = ImportUtils.convertListToArray(input);
        logMap(map);

        final Result result = createModifiedMap(map, 1, false);
        logMap(result.getNewMap());

        return String.valueOf(result.getNewCoords().size());
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] map = ImportUtils.convertListToArray(input);
        logMap(map);

        final Result result = createModifiedMap(map, 100, true); // We assume that 100 is enough to traverse the map.
        logMap(result.getNewMap());

        return String.valueOf(result.getNewCoords().size());
    }


    private Result createModifiedMap(
            final String[][] map,
            final int multiplierMaxSize,
            final boolean includeAntennas
    ) {
        final Set<Coordinate> newCoords = new HashSet<>();
        final Map<String, List<Coordinate>> overview = parseInput(map);
        final String[][] modifiedMap = ArrayUtils.deepCopy2Array(map);

        for (final Map.Entry<String, List<Coordinate>> entry : overview.entrySet()) {
            final Set<Coordinate> newCoordsForLetter = new HashSet<>();
            final List<Coordinate> coordinates = entry.getValue();

            for (final Coordinate coord1 : coordinates) {
                for (final Coordinate coord2 : coordinates) {
                    for (int multiplier = 1; multiplier <= multiplierMaxSize; multiplier++) {
                        if (coord1 == coord2) {
                            continue;
                        }
                        final List<Coordinate> newCoordsTmp = calculatePotentialCoords(coord1, coord2, multiplier);

                        for (final Coordinate newCoord : newCoordsTmp) {
                            if (includeAntennas || (!coord1.equals(newCoord) && !coord2.equals(newCoord))) {
                                if (ArrayUtils.isWithinBounds(map, newCoord)) {
                                    newCoordsForLetter.add(newCoord);
                                    modifiedMap[newCoord.getX()][newCoord.getY()] = "#";
                                }
                            }
                        }
                    }
                }

            }
            log(entry.getKey() + ": " + newCoordsForLetter);
            newCoords.addAll(newCoordsForLetter);
        }
        return new Result(modifiedMap, newCoords);
    }

    private List<Coordinate> calculatePotentialCoords(
            final Coordinate coord1,
            final Coordinate coord2,
            final int multiplier
    ) {
        final int distX = multiplier * (coord1.getX() - coord2.getX());
        final int distY = multiplier * (coord1.getY() - coord2.getY());

        return List.of(
                new Coordinate(coord1.getX() + distX, coord1.getY() + distY),
                new Coordinate(coord2.getX() + distX, coord2.getY() + distY),
                new Coordinate(coord1.getX() - distX, coord1.getY() - distY),
                new Coordinate(coord2.getX() - distX, coord2.getY() - distY)
        );
    }


    private Map<String, List<Coordinate>> parseInput(final String[][] map) {
        return Arrays.stream(map)
                .flatMap(Arrays::stream)
                .filter(string -> !string.equals("."))
                .distinct()
                .collect(Collectors.toMap(
                        letter -> letter,
                        letter -> ArrayUtils.findAllOccurences(map, letter)
                ));
    }


    @Data
    @AllArgsConstructor
    public static class Result {

        String[][] newMap;
        Set<Coordinate> newCoords;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
