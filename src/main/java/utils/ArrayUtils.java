package utils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The ArrayUtils for the AoC setup.
 *
 * @author mglembock
 */
public class ArrayUtils {

    // Directions: North, East, South, West
    public static final int[][] DIRECTIONS = {
            {-1, 0}, // North
            {0, 1},  // East
            {1, 0},  // South
            {0, -1}  // West
    };

    public static String[][] transpose(final String[][] original) {
        final int numRows = original.length;
        final int numCols = original[0].length;

        final String[][] transposed = new String[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposed[j][i] = original[i][j];
            }
        }

        return transposed;
    }

    public static String[][] rotateRight(final String[][] original) {
        final int originalRows = original.length;
        final int originalCols = original[0].length;

        final String[][] rotated = new String[originalCols][originalRows];

        for (int i = 0; i < originalRows; i++) {
            for (int j = 0; j < originalCols; j++) {
                rotated[j][originalRows - 1 - i] = original[i][j];
            }
        }

        return rotated;
    }

    public static String[][] rotateLeft(final String[][] original) {
        final int originalRows = original.length;
        final int originalCols = original[0].length;

        final String[][] rotated = new String[originalCols][originalRows];

        for (int i = 0; i < originalRows; i++) {
            for (int j = 0; j < originalCols; j++) {
                rotated[originalCols - 1 - j][i] = original[i][j];
            }
        }

        return rotated;
    }

    public static String[][] deepCopy2Array(final String[][] original) {
        final String[][] copy = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new String[original[i].length];
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    public static List<Coordinate> findAllOccurences(
            final String[][] matrix,
            final String letter
    ) {
        final List<Coordinate> occurences = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j].equals(letter)) {
                    occurences.add(new Coordinate(i, j));
                }
            }
        }
        return occurences;
    }

    public static List<Coordinate> findAllOccurences(
            final char[][] matrix,
            final char letter
    ) {
        final List<Coordinate> occurences = new ArrayList<>();
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                if (matrix[i][j] == letter) {
                    occurences.add(new Coordinate(i, j));
                }
            }
        }
        return occurences;
    }

    public static List<Coordinate> filterSurroundingCoordinates(
            final String[][] map,
            final Coordinate coords,
            final String target
    ) {
        // Check boundaries to avoid ArrayIndexOutOfBoundsException
        final int rows = map.length;
        final int cols = map[0].length;

        final int x = coords.getX();
        final int y = coords.getY();

        final List<Coordinate> result = new ArrayList<>();

        // Check the left cell
        if (x > 0 && map[x - 1][y].equals(target)) {
            result.add(new Coordinate(x - 1, y));
        }

        // Check the right cell
        if (x < rows - 1 && map[x + 1][y].equals(target)) {
            result.add(new Coordinate(x + 1, y));
        }

        // Check the top cell
        if (y > 0 && map[x][y - 1].equals(target)) {
            result.add(new Coordinate(x, y - 1));
        }

        // Check the bottom cell
        if (y < cols - 1 && map[x][y + 1].equals(target)) {
            result.add(new Coordinate(x, y + 1));
        }

        return result;
    }


    public static String generateHash(final String[][] array) {
        final StringBuilder hash = new StringBuilder();
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                hash.append(array[i][j]);
            }
        }
        return hash.toString();
    }

    public static Integer[] removeFirstOccurrence(
            final Integer[] array,
            final int element
    ) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                final Integer[] newArray = new Integer[array.length - 1];
                System.arraycopy(array, 0, newArray, 0, i);
                System.arraycopy(array, i + 1, newArray, i, array.length - i - 1);
                return newArray;
            }
        }
        return array;
    }

    public static Long[] removeFirstOccurrence(
            final Long[] array,
            final long element
    ) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == element) {
                final Long[] newArray = new Long[array.length - 1];
                System.arraycopy(array, 0, newArray, 0, i);
                System.arraycopy(array, i + 1, newArray, i, array.length - i - 1);
                return newArray;
            }
        }
        return array;
    }

    public static boolean isWithinBounds(
            final String[][] matrix,
            final Coordinate coordinate
    ) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false; // Leere oder null-Matrix
        }

        final int x = coordinate.x;
        final int y = coordinate.y;

        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
    }

    public static boolean isWithinBounds(
            final char[][] matrix,
            final Coordinate coordinate
    ) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false; // Leere oder null-Matrix
        }

        final int x = coordinate.x;
        final int y = coordinate.y;

        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
    }

    public static boolean isWithinBounds(
            final String[][] matrix,
            final int x,
            final int y
    ) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
    }

    public static boolean isWithinBounds(
            final char[][] matrix,
            final int x,
            final int y
    ) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false;
        }
        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
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


}
