package year2024.day20;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/20
 */
public class Day20 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    private static final String FILE_PATH = "src/main/resources/year2024/day20/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] map = ImportUtils.convertListToArray(input);
        final Coordinate start = ArrayUtils.findAllOccurences(map, "S").get(0);
        final Coordinate end = ArrayUtils.findAllOccurences(map, "E").get(0);

        final List<Coordinate> ogShortestPath = ArrayUtils.findShortestPath(map, start, end);
        final int expectedTimeSaving = 100;
        final int cheatDistance = 2;

        final int count = getCount(ogShortestPath, cheatDistance, expectedTimeSaving);
        return String.valueOf(count);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] map = ImportUtils.convertListToArray(input);
        final Coordinate start = ArrayUtils.findAllOccurences(map, "S").get(0);
        final Coordinate end = ArrayUtils.findAllOccurences(map, "E").get(0);

        final List<Coordinate> ogShortestPath = ArrayUtils.findShortestPath(map, start, end);
        final int expectedTimeSaving = 100;
        final int cheatDistance = 20;

        final int count = getCount(ogShortestPath, cheatDistance, expectedTimeSaving);
        return String.valueOf(count);
    }

    private static int getCount(
            final List<Coordinate> ogShortestPath,
            final int cheatDistance,
            final int expectedTimeSaving
    ) {
        final int pathLength = ogShortestPath.size();
        int count = 0;
        for (int i = 0; i < pathLength - 1; i++) {
            for (int j = i + 1; j < pathLength; j++) {
                final Coordinate p1 = ogShortestPath.get(i);
                final Coordinate p2 = ogShortestPath.get(j);
                final int manDist = p1.manhattanDistance(p2);
                if (manDist > cheatDistance) {
                    continue;
                }
                final int pathDist = j - i;
                final int timeSaved = pathDist - manDist;
                if (timeSaved >= expectedTimeSaving) {
                    count++;
                }
            }
        }
        return count;
    }

}
