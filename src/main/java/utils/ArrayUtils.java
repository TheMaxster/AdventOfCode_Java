package utils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

import org.apache.commons.collections4.CollectionUtils;

public class ArrayUtils {

    public static void log(final String logStatement) {
        System.out.println(logStatement);
    }

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

    public static void printMap(final String[][] array) {
        for (final String[] row : array) {
            for (final String element : row) {
                System.out.print(element);
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public static int sumUpInt(final List<Integer> list) {
        return list.stream().reduce(Integer::sum).orElse(0);
    }

    public static BigDecimal sumUpBd(final List<BigDecimal> list) {
        return CollectionUtils.emptyIfNull(list).stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
}
