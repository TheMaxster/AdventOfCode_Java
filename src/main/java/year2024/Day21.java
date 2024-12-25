package year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import application.Day;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;
import utils.State;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day21 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day21/input_test_01.txt";
    private static final Coordinate KEY_PAD_START = new Coordinate(2, 3);
    private static final Coordinate DIR_PAD_START = new Coordinate(2, 0);

    private static final String[][] KEY_PAD = {
            {"7", "8", "9"},
            {"4", "5", "6"},
            {"1", "2", "3"},
            {"#", "0", "A"}
    };
    private static final String[][] DIR_PAD = {
            {"#", "^", "A"},
            {"<", "v", ">"}
    };
    private static final Map<String, Coordinate> COORD_OF = Map.of(
            "^", new Coordinate(1, 0),
            "A", new Coordinate(2, 0),
            "<", new Coordinate(0, 1),
            "v", new Coordinate(1, 1),
            ">", new Coordinate(2, 1)
    );
    private static final Map<CacheKey, Long> CACHE = new HashMap<>();

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        return Long.toString(solve(2, input));
    }

    public String part2(final List<String> input) {
        return Long.toString(solve(25, input));
    }

    private long solve(
            final int depth,
            final List<String> input
    ) {
        long sum = 0;
        for (final String code : input) {
            long best = Long.MAX_VALUE;
            final List<String> robot1Moves = innerCalculate(code, KEY_PAD_START, "", KEY_PAD);
            for (final String robot1Move : robot1Moves) {
                best = Math.min(best, countPushes(robot1Move, depth));
            }
            sum += best * codeToNumber(code);
        }
        return sum;
    }

    private int codeToNumber(final String code) {
        return Integer.parseInt(code.substring(0, code.length() - 1));
    }

    private List<String> innerCalculate(
            final String code,
            final Coordinate start,
            final String core,
            final String[][] pad
    ) {
        if (code.isEmpty()) {
            return List.of(core);
        }

        final String target = code.substring(0, 1);
        final List<String> collectedPaths = new ArrayList<>();

        for (final State state : findAllShortestPaths(pad, start, target)) {
            collectedPaths.addAll(
                    innerCalculate(code.substring(1), state.getCoordinate(), core + deserializePath(state.getPath()) + "A", pad));
        }

        return collectedPaths;
    }

    private long countPushes(
            final String robot1Move,
            final int depth
    ) {
        if (depth == 0) {
            return robot1Move.length();
        }

        final CacheKey key = new CacheKey(robot1Move, depth);
        if (CACHE.containsKey(key)) {
            return CACHE.get(key);
        }

        Coordinate position = DIR_PAD_START;
        long totalPushes = 0L;

        for (final String s : robot1Move.split("")) {
            long minPushes = Long.MAX_VALUE;

            final List<State> states = findAllShortestPaths(DIR_PAD, position, s);
            for (final State state : states) {
                final String path = deserializePath(state.getPath()) + "A";
                minPushes = Math.min(minPushes, countPushes(path, depth - 1));
            }
            totalPushes += minPushes;
            position = COORD_OF.get(s);
        }

        CACHE.put(key, totalPushes);
        return totalPushes;
    }

    private String deserializePath(final List<Coordinate> path) {
        final StringBuilder result = new StringBuilder();
        for (int i = 0; i < path.size() - 1; i++) {
            final Coordinate one = path.get(i);
            final Coordinate other = path.get(i + 1);

            if (one.x < other.x) {
                result.append('>');
            } else if (one.x > other.x) {
                result.append('<');
            } else if (one.y < other.y) {
                result.append('v');
            } else if (one.y > other.y) {
                result.append('^');
            }
        }
        return result.toString();
    }

    /**
     * In this method, the x and y are use y and x. But it only looks weird, it works properly! It's more correct than other challenges,
     * because in other challenges most maps are quadratic. Here they are not.
     *
     * @param pad
     *         the pad
     * @param start
     *         the start
     * @param target
     *         the target
     * @return
     */
    public static List<State> findAllShortestPaths(
            final String[][] pad,
            final Coordinate start,
            final String target
    ) {
        final int rows = pad.length;
        final int cols = pad[0].length;

        final List<State> shortestPaths = new ArrayList<>();
        final int[][] distances = new int[rows][cols];
        for (final int[] row : distances) {
            Arrays.fill(row, Integer.MAX_VALUE);
        }

        final Queue<State> queue = new LinkedList<>();
        queue.add(new State(start, 0, List.of(start)));
        distances[start.y][start.x] = 0;

        while (!queue.isEmpty()) {
            final State current = queue.poll();

            final Coordinate currentCoord = current.getCoordinate();
            final int y = currentCoord.y;
            final int x = currentCoord.x;

            if (pad[y][x].equals(target)) {
                if (current.getScore() < distances[y][x]) {
                    distances[y][x] = current.getScore();
                    shortestPaths.clear();
                }
                if (current.getScore() == distances[y][x]) {
                    shortestPaths.add(current);
                }
                continue;
            }

            for (final Coordinate direction : currentCoord.getNeighbourhood()) {
                final int nx = direction.getX();
                final int ny = direction.getY();

                if (ArrayUtils.isWithinBounds(pad, ny, nx) && !pad[ny][nx].equals("#")) {
                    final int newScore = current.getScore() + 1;
                    if (newScore <= distances[ny][nx]) {
                        final List<Coordinate> newPath = new ArrayList<>(current.getPath());
                        newPath.add(new Coordinate(nx, ny));
                        distances[ny][nx] = newScore;
                        queue.add(new State(new Coordinate(nx, ny), newScore, newPath));
                    }
                }
            }
        }
        return shortestPaths;
    }

    private record CacheKey(String road, int depth) {

    }


}
