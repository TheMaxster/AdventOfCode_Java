package year2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import application.Day;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day10 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day10/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(importInput);
        logMap(map);

        final Set<List<Coordinate>> completeWays = calculateAllCompleteWays(map);

        final long uniquePaths = completeWays.stream()
                .map(path -> path.get(0).toString() + path.get(path.size() - 1).toString())
                .distinct()
                .count();

        return String.valueOf(uniquePaths);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(importInput);
        logMap(map);

        final Set<List<Coordinate>> completeWays = calculateAllCompleteWays(map);
        return String.valueOf(completeWays.size());
    }

    private Set<List<Coordinate>> calculateAllCompleteWays(final String[][] map) {
        final Queue<List<Coordinate>> allWays = new ArrayDeque<>();

        // Initialize queue with all "0" coordinates
        for (final Coordinate zero : ArrayUtils.findAllOccurences(map, "0")) {
            allWays.add(List.of(zero));
        }

        final Set<List<Coordinate>> completeWays = new HashSet<>();

        while (!allWays.isEmpty()) {
            final List<Coordinate> way = allWays.poll();
            final Coordinate last = way.get(way.size() - 1);

            // Get next height and surrounding coordinates
            final String nextHeight = getNextHeight(map, last);
            final List<Coordinate> nextHeights = ArrayUtils.filterSurroundingCoordinates(map, last, nextHeight);

            for (final Coordinate next : nextHeights) {
                final List<Coordinate> newPath = new ArrayList<>(way);
                newPath.add(next);

                if ("9".equals(nextHeight)) {
                    completeWays.add(newPath);
                } else {
                    allWays.add(newPath);
                }
            }
        }
        return completeWays;
    }

    private String getNextHeight(
            final String[][] map,
            final Coordinate last
    ) {
        final String currentHeight = map[last.x][last.y];
        return String.valueOf(Integer.parseInt(currentHeight) + 1);
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
