package utils;

import java.util.ArrayList;
import java.util.List;

/**
 * The ArrayUtils for the AoC setup.
 *
 * @author mglembock
 */
public class ArrayUtils {

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
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
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

    public static boolean isWithinBounds(String[][] matrix, Coordinate coordinate) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0) {
            return false; // Leere oder null-Matrix
        }

        int x = coordinate.x;
        int y = coordinate.y;

        return x >= 0 && x < matrix.length && y >= 0 && y < matrix[0].length;
    }
}
