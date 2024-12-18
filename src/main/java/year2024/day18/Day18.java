package year2024.day18;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day18 extends Day {

    // Directions: North, East, South, West
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // North
            {0, 1},  // East
            {1, 0},  // South
            {0, -1}  // West
    };

    private static final String FILE_PATH = "src/main/resources/year2024/day18/input_test_01.txt";
    private static final int TEST_WIDTH = 7;
    private static final int TEST_HEIGHT = 7;

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int widthToUse = 71; // TEST_WIDTH;
        final int heightToUse = 71; // TEST_HEIGHT;
        final int bytesToFall = 1024; // 12
        final List<String> importToUse = input;

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final int bytesToFall = 12;
        //        final List<String> importToUse = importInput;

        final List<Coordinate> bytes = parseInput(importToUse);
        final Coordinate start = new Coordinate(0, 0);
        final Coordinate end = new Coordinate(widthToUse - 1, heightToUse - 1);

        final String[][] map = createMap(widthToUse, heightToUse, bytesToFall, bytes);

        final int steps = findShortestPath(map, start, end);

        return String.valueOf(steps);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int widthToUse = 71; // TEST_WIDTH;
        final int heightToUse = 71; // TEST_HEIGHT;
        final int bytesToFall = 1024; // 12
        final List<String> importToUse = input;

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final int bytesToFall = 12;
        //        final List<String> importToUse = importInput;

        final List<Coordinate> bytes = parseInput(importToUse);
        final Coordinate start = new Coordinate(0, 0);
        final Coordinate end = new Coordinate(widthToUse - 1, heightToUse - 1);

        Coordinate result = null;
        for (int i = 1; i < bytes.size(); i++) {
            final String[][] map = createMap(widthToUse, heightToUse, i, bytes);

            final int steps = findShortestPath(map, start, end);
            if (steps == -1) {
                result = bytes.get(i - 1);
                break;
            }

        }

        return result.y + "," + result.x;
    }

    private static String[][] createMap(
            final int widthToUse,
            final int heightToUse,
            final int bytesToFall,
            final List<Coordinate> bytes
    ) {
        final String[][] map = new String[widthToUse][heightToUse];
        for (final String[] strings : map) {
            Arrays.fill(strings, ".");
        }

        for (int i = 0; i < bytesToFall; i++) {
            final Coordinate aByte = bytes.get(i);
            map[aByte.getX()][aByte.getY()] = "#";
        }
        return map;
    }

    private List<Coordinate> parseInput(final List<String> importInput) {
        final Pattern pattern = Pattern.compile("(\\d+),(\\d+)");
        final List<Coordinate> coordinates = new ArrayList<>();
        for (final String s : importInput) {
            final Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                final int x = Integer.parseInt(matcher.group(1));
                final int y = Integer.parseInt(matcher.group(2));
                coordinates.add(new Coordinate(y, x));
            }
        }
        return coordinates;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    public static int findShortestPath(
            final String[][] map,
            final Coordinate start,
            final Coordinate end
    ) {
        final int rows = map.length;
        final int cols = map[0].length;

        // Queue for BFS: stores [x, y, distance]
        final Queue<State> queue = new LinkedList<>();
        queue.add(new State(start, 0)); // Start point with distance 0

        // Visited array
        final boolean[][] visited = new boolean[rows][cols];
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            final State current = queue.poll();

            // Check if we reached the end
            if (current.getCoordinate().x == end.x && current.getCoordinate().y == end.y) {
                return current.getScore();
            }

            // Explore neighbors in all 4 directions
            for (final int[] direction : DIRECTIONS) {
                final int nx = current.getCoordinate().x + direction[0];
                final int ny = current.getCoordinate().y + direction[1];

                // Check if the next position is within bounds, not visited, and not blocked
                if (ArrayUtils.isWithinBounds(map, nx, ny) && !visited[nx][ny] && !map[nx][ny].equals("#")) {
                    visited[nx][ny] = true; // Mark as visited
                    queue.add(new State(new Coordinate(nx, ny), current.getScore() + 1)); // Add to queue with updated distance
                }
            }
        }

        return -1; // No path found
    }

    @Data
    @AllArgsConstructor
    private static class State {

        private final Coordinate coordinate;
        private final int score;
    }
}
