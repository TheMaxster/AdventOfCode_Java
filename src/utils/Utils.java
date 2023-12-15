package utils;

import java.util.List;

public class Utils {

    public static void log(String logStatement){
        System.out.println(logStatement);
    }

    public static String[][] transpose(String[][] original) {
        int numRows = original.length;
        int numCols = original[0].length;

        String[][] transposed = new String[numCols][numRows];

        for (int i = 0; i < numRows; i++) {
            for (int j = 0; j < numCols; j++) {
                transposed[j][i] = original[i][j];
            }
        }

        return transposed;
    }

    public static String[][] rotateRight(String[][] original) {
        int originalRows = original.length;
        int originalCols = original[0].length;

        String[][] rotated = new String[originalCols][originalRows];

        for (int i = 0; i < originalRows; i++) {
            for (int j = 0; j < originalCols; j++) {
                rotated[j][originalRows - 1 - i] = original[i][j];
            }
        }

        return rotated;
    }

    public static String[][] rotateLeft(String[][] original) {
        int originalRows = original.length;
        int originalCols = original[0].length;

        String[][] rotated = new String[originalCols][originalRows];

        for (int i = 0; i < originalRows; i++) {
            for (int j = 0; j < originalCols; j++) {
                rotated[originalCols - 1 - j][i] = original[i][j];
            }
        }

        return rotated;
    }

    public static String[][] deepCopy2Array(String[][] original) {
        String[][] copy = new String[original.length][];
        for (int i = 0; i < original.length; i++) {
            copy[i] = new String[original[i].length];
            for (int j = 0; j < original[i].length; j++) {
                copy[i][j] = original[i][j];
            }
        }
        return copy;
    }

    public static void printMap(String[][] array) {
        for (String[] row : array) {
            for (String element : row) {
                System.out.print(element);
            }
            System.out.println(" ");
        }
        System.out.println(" ");
    }

    public static int sumUp(final List<Integer> pointsForRow) {
        return pointsForRow.stream().reduce(Integer::sum).orElse(0);
    }

    public static String generateHash(String[][] array){
        StringBuilder hash = new StringBuilder();
        for(int i = 0; i<array.length; i++){
            for(int j = 0; j< array[0].length; j++){
                hash.append(array[i][j]);
            }
        }
        return hash.toString();
    }
}
