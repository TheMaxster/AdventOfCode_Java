package year2024;

import java.util.List;

import application.Day;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/4
 */
public class Day04 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day04/input_test_01.txt";

    public String part1(final List<String> input) {
        // final List<String> testInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] matrix = ImportUtils.convertListToArray(input);

        final int findings = ArrayUtils.findAllOccurences(matrix, "X").stream()
                .mapToInt(x -> checkLetterInNeighborhood(x, matrix))
                .sum();

        return String.valueOf(findings);
    }

    private int checkLetterInNeighborhood(
            final Coordinate xCoordinate,
            final String[][] matrix
    ) {
        return
                checkMas(xCoordinate, matrix, -1, 0, -2, 0, -3, 0)
                        + checkMas(xCoordinate, matrix, -1, -1, -2, -2, -3, -3)
                        + checkMas(xCoordinate, matrix, 0, -1, 0, -2, 0, -3)
                        + checkMas(xCoordinate, matrix, 1, -1, 2, -2, 3, -3)
                        + checkMas(xCoordinate, matrix, 1, 0, 2, 0, 3, 0)
                        + checkMas(xCoordinate, matrix, 1, 1, 2, 2, 3, 3)
                        + checkMas(xCoordinate, matrix, 0, 1, 0, 2, 0, 3)
                        + checkMas(xCoordinate, matrix, -1, 1, -2, 2, -3, 3);

    }

    private int checkMas(
            final Coordinate xCoordinate,
            final String[][] matrix,
            final int mX,
            final int mY,
            final int aX,
            final int aY,
            final int sX,
            final int sY
    ) {
        try {
            if (matrix[xCoordinate.x + mX][xCoordinate.y + mY].equals("M")
                    && matrix[xCoordinate.x + aX][xCoordinate.y + aY].equals("A")
                    && matrix[xCoordinate.x + sX][xCoordinate.y + sY].equals("S")
            ) {
                return 1;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Nothing to do here.
        }
        return 0;
    }

    public String part2(final List<String> input) {
        // final List<String> testInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] matrix = ImportUtils.convertListToArray(input);

        final int findings = ArrayUtils.findAllOccurences(matrix, "A").stream()
                .mapToInt(a -> checkLetterInNeighborhoodPart2(a, matrix))
                .sum();

        return String.valueOf(findings);
    }


    private int checkLetterInNeighborhoodPart2(
            final Coordinate aCoordinate,
            final String[][] matrix
    ) {
        return checkMasPart2(aCoordinate, matrix, -1, -1, -1, 1, 1, 1, 1, -1)
                + checkMasPart2(aCoordinate, matrix, -1, 1, 1, 1, 1, -1, -1, -1)
                + checkMasPart2(aCoordinate, matrix, 1, 1, 1, -1, -1, -1, -1, 1)
                + checkMasPart2(aCoordinate, matrix, 1, -1, -1, -1, -1, 1, 1, 1);

    }

    private int checkMasPart2(
            final Coordinate xCoordinate,
            final String[][] matrix,
            final int m1X,
            final int m1Y,
            final int m2X,
            final int m2Y,
            final int s1X,
            final int s1Y,
            final int s2X,
            final int s2Y
    ) {
        try {
            if (matrix[xCoordinate.x + m1X][xCoordinate.y + m1Y].equals("M")
                    && matrix[xCoordinate.x + m2X][xCoordinate.y + m2Y].equals("M")
                    && matrix[xCoordinate.x + s1X][xCoordinate.y + s1Y].equals("S")
                    && matrix[xCoordinate.x + s2X][xCoordinate.y + s2Y].equals("S")
            ) {
                return 1;
            }
        } catch (final ArrayIndexOutOfBoundsException e) {
            // Nothing to do here.
        }
        return 0;
    }


    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

}
