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
import utils.ArrayUtils;
import utils.Coordinate;
import utils.Direction;
import utils.ImportUtils;
import utils.State;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day16 extends Day {

    //  private static final String FILE_PATH = "src/main/resources/year2024/day16/input_test_01.txt";
    private static final String FILE_PATH = "src/main/resources/year2024/day16/input_test_02.txt";

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
        final PriorityQueue<State> pq = new PriorityQueue<>(Comparator.comparingInt(State::getScore));

        // Map to track visited states
        final Map<String, Integer> visited = new HashMap<>();
        final List<State> bestPaths = new ArrayList<>();
        int minScore = Integer.MAX_VALUE;

        // Initial state: start facing East (index 1 in DIRECTIONS)
        pq.add(new State(start, 0, List.of(start), Direction.EAST));
        visited.put(encodeState(start, Direction.EAST), 0);

        while (!pq.isEmpty()) {
            final State current = pq.poll();

            // If we reach the end, check the score
            if (current.getCoordinate().getX() == end.getX() && current.getCoordinate().getY() == end.getY()) {
                if (current.getScore() < minScore) {
                    // Found a new minimum score
                    minScore = current.getScore();
                    bestPaths.clear();
                }
                if (current.getScore() == minScore) {
                    bestPaths.add(current);
                }
                continue;
            }

            // Try all possible actions
            // 1. Move forward
            final Coordinate currentCoordinate = current.getCoordinate();
            final Coordinate newCoord = currentCoordinate.nextCoordinate(current.getDirection());

            if (ArrayUtils.isWithinBounds(maze, newCoord) && !maze[newCoord.getX()][newCoord.getY()].equals("#")) {
                final List<Coordinate> path = new ArrayList<>(current.getPath());
                path.add(newCoord);
                final State nextState = new State(newCoord, current.getScore() + 1, path, current.getDirection());
                final String encoded = encodeState(newCoord, current.getDirection());

                // Allow revisit if this path has the same or better score
                if (!visited.containsKey(encoded) || visited.get(encoded) >= nextState.getScore()) {
                    visited.put(encoded, nextState.getScore());
                    pq.add(nextState);
                }
            }

            // 2. Rotate clockwise
            final Direction newDirectionClockwise = Direction.getNextDirectionClockwise(current.getDirection());
            final State nextClockwise = new State(
                    current.getCoordinate(),
                    current.getScore() + 1000,
                    current.getPath(),
                    newDirectionClockwise
            );
            final String encodedClockwise = encodeState(current.getCoordinate(), newDirectionClockwise);

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedClockwise) || visited.get(encodedClockwise) >= nextClockwise.getScore()) {
                visited.put(encodedClockwise, nextClockwise.getScore());
                pq.add(nextClockwise);
            }

            // 3. Rotate counterclockwise
            final Direction newDirectionCounterclockwise = Direction.getNextDirectionCounterClockwise(current.getDirection());
            final State nextCounterclockwise = new State(
                    current.getCoordinate(),
                    current.getScore() + 1000,
                    current.getPath(),
                    newDirectionCounterclockwise
            );
            final String encodedCounterclockwise = encodeState(current.getCoordinate(), newDirectionCounterclockwise);

            // Allow revisit if this path has the same or better score
            if (!visited.containsKey(encodedCounterclockwise) || visited.get(encodedCounterclockwise) >= nextCounterclockwise.getScore()) {
                visited.put(encodedCounterclockwise, nextCounterclockwise.getScore());
                pq.add(nextCounterclockwise);
            }
        }

        return bestPaths;
    }

    // Encode the state as a string for visited tracking
    private static String encodeState(
            final Coordinate coodinate,
            final Direction direction
    ) {
        return coodinate.getX() + "," + coodinate.getY() + "," + direction;
    }

    //    // State class representing a position in the maze with direction and score
    //    @Data
    //    @AllArgsConstructor
    //    private static class State {
    //
    //        private final Coordinate coordinate;
    //        private final int direction, score;
    //        private final List<Coordinate> path;
    //    }


    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }
}
