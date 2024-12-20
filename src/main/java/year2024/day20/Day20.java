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

    // Directions: North, East, South, West
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // North
            {0, 1},  // East
            {1, 0},  // South
            {0, -1}  // West
    };

    private static final String FILE_PATH = "src/main/resources/year2024/day20/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] map = ImportUtils.convertListToArray(input);
        final Coordinate start = ArrayUtils.findAllOccurences(map, "S").get(0);
        final Coordinate end = ArrayUtils.findAllOccurences(map, "E").get(0);

        final List<Coordinate> ogShortestPath = findShortestPath(map, start, end);
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

        final List<Coordinate> ogShortestPath = findShortestPath(map, start, end);
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

    public static List<Coordinate> findShortestPath(
            final String[][] map,
            final Coordinate start,
            final Coordinate end
    ) {
        final int rows = map.length;
        final int cols = map[0].length;

        // Queue for BFS: stores [x, y, distance]
        final Queue<State> queue = new LinkedList<>();
        final List<Coordinate> path = new ArrayList<>();
        path.add(start);
        queue.add(new State(start, 0, path)); // Start point with distance 0

        // Visited array
        final boolean[][] visited = new boolean[rows][cols];
        visited[start.x][start.y] = true;

        while (!queue.isEmpty()) {
            final State current = queue.poll();

            // Check if we reached the end
            if (current.getCoordinate().x == end.x && current.getCoordinate().y == end.y) {
                return current.getPath();
            }

            // Explore neighbors in all 4 directions
            for (final int[] direction : DIRECTIONS) {
                final int nx = current.getCoordinate().x + direction[0];
                final int ny = current.getCoordinate().y + direction[1];

                // Check if the next position is within bounds, not visited, and not blocked
                if (ArrayUtils.isWithinBounds(map, nx, ny) && !visited[nx][ny] && !map[nx][ny].equals("#")) {
                    visited[nx][ny] = true; // Mark as visited
                    final Coordinate newCoordinate = new Coordinate(nx, ny);
                    final List<Coordinate> newPath = new ArrayList<>(current.getPath());
                    newPath.add(newCoordinate);
                    queue.add(new State(newCoordinate, current.getScore() + 1, newPath)); // Add to queue with updated distance
                }
            }
        }

        return new ArrayList<>(); // No path found
    }

    @Data
    @AllArgsConstructor
    private static class State {

        private final Coordinate coordinate;
        private final int score;
        private List<Coordinate> path;
    }

}
