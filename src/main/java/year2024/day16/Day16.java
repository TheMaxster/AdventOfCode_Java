package year2024.day16;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day16 extends Day {

    //  private static final String FILE_PATH = "src/main/resources/year2024/day16/input_test_01.txt";
    private static final String FILE_PATH = "src/main/resources/year2024/day16/input_test_02.txt";


    // Directions: North, East, South, West
    private static final int[][] DIRECTIONS = {
            {-1, 0}, // North
            {0, 1},  // East
            {1, 0},  // South
            {0, -1}  // West
    };

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(input);
        final List<State> shortestRouts = findAllShortestPaths(map);
        return String.valueOf(shortestRouts.get(0).getScore());
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(input);
        final List<State> shortestRoutes = findAllShortestPaths(map);
        final Set<Coordinate> uniqueCoords = shortestRoutes.stream()
                .flatMap(route -> route.getPath().stream())
                .collect(Collectors.toSet());
        return String.valueOf(uniqueCoords.size());
    }

    public static List<State> findAllShortestPaths(final String[][] maze) {
        final Coordinate start = ArrayUtils.findAllOccurences(maze, "S").get(0);
        final Coordinate end = ArrayUtils.findAllOccurences(maze, "E").get(0);

        // Priority queue for BFS (min-heap by score)
        final PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(s -> s.score));
        // Map to track visited states
        final Map<String, Integer> visited = new HashMap<>();
        final List<State> bestPaths = new ArrayList<>();
        int minScore = Integer.MAX_VALUE;

        // Initial state: start facing East (index 1 in DIRECTIONS)
        pq.add(new State(start, 1, 0, List.of(start)));
        visited.put(encodeState(start, 1), 0);

        while (!pq.isEmpty()) {
            final State current = pq.poll();

            // If we reach the end, check the score
            if (current.getCoordinate().getX() == end.getX() && current.getCoordinate().getY() == end.getY()) {
                if (current.score < minScore) {
                    // Found a new minimum score
                    minScore = current.score;
                    bestPaths.clear();
                }
                if (current.score == minScore) {
                    bestPaths.add(current);
                }
                continue;
            }

            // Try all possible actions
            // 1. Move forward
            final Coordinate currentCoordinate = current.getCoordinate();
            final int newX = currentCoordinate.getX() + DIRECTIONS[current.direction][0];
            final int newY = currentCoordinate.getY() + DIRECTIONS[current.direction][1];

            if (ArrayUtils.isWithinBounds(maze, newX, newY) && !maze[newX][newY].equals("#")) {
                final List<Coordinate> path = new ArrayList<>(current.getPath());
                final Coordinate newCoord = new Coordinate(newX, newY);
                path.add(newCoord);
                final State nextState = new State(newCoord, current.direction, current.score + 1, path);
                final String encoded = encodeState(newCoord, current.direction);

                // Allow revisit if this path has the same or better score
                if (!visited.containsKey(encoded) || visited.get(encoded) >= nextState.score) {
                    visited.put(encoded, nextState.score);
                    pq.add(nextState);
                }
            }

            // 2. Rotate clockwise
            final int newDirectionClockwise = (current.direction + 1) % 4;
            final State nextClockwise = new State(current.getCoordinate(), newDirectionClockwise, current.score + 1000, current.getPath());
            final String encodedClockwise = encodeState(current.getCoordinate(), newDirectionClockwise);

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedClockwise) || visited.get(encodedClockwise) >= nextClockwise.score) {
                visited.put(encodedClockwise, nextClockwise.score);
                pq.add(nextClockwise);
            }

            // 3. Rotate counterclockwise
            final int newDirectionCounterclockwise = (current.direction + 3) % 4; // +3 is equivalent to -1 modulo 4
            final State nextCounterclockwise = new State(current.getCoordinate(), newDirectionCounterclockwise, current.score + 1000,
                    current.getPath());
            final String encodedCounterclockwise = encodeState(current.getCoordinate(), newDirectionCounterclockwise);

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedCounterclockwise) || visited.get(encodedCounterclockwise) >= nextCounterclockwise.score) {
                visited.put(encodedCounterclockwise, nextCounterclockwise.score);
                pq.add(nextCounterclockwise);
            }
        }

        return bestPaths;
    }

    // Encode the state as a string for visited tracking
    private static String encodeState(
            final Coordinate coodinate,
            final int direction
    ) {
        return coodinate.getX() + "," + coodinate.getY() + "," + direction;
    }

    // State class representing a position in the maze with direction and score
    @Data
    @AllArgsConstructor
    private static class State {

        private final Coordinate coordinate;
        private final int direction, score;
        private final List<Coordinate> path;
    }


    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }
}
