package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * The ImportUtils for the AoC setup.
 *
 * @author mglembock
 */
public class ImportUtils {

    public static String readAsPlainString(final String filePath) {
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> readAsList(final String filePath) {
        final List<String> linesList = new ArrayList<>();

        try (final BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                linesList.add(line);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        return linesList;
    }

    public static String[][] readAsArray(final String filePath) {
        final List<String[]> linesList = new ArrayList<>();

        try (final BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Convert each character to a String and store in an array
                final String[] chars = line.split("");
                linesList.add(chars);
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }

        // Convert the list to a 2D String array
        final String[][] linesArray = new String[linesList.size()][];
        linesList.toArray(linesArray);

        return linesArray;
    }

    public static String[][] convertListToArray(final List<String> list) {
        final String[][] array = new String[list.size()][];

        for (int i = 0; i < list.size(); i++) {
            array[i] = list.get(i).split("");  // String wird in seine Zeichen aufgeteilt
        }

        return array;
    }

    public static Integer[][] convertListToIntArray(
            final List<String> input,
            final String delimiter
    ) {
        final Integer[][] result = new Integer[input.size()][];

        for (int i = 0; i < input.size(); i++) {
            final String[] parts = input.get(i).split(delimiter);

            result[i] = new Integer[parts.length];
            for (int j = 0; j < parts.length; j++) {
                result[i][j] = Integer.parseInt(parts[j].trim());
            }
        }
        return result;
    }

    public static Scanner convertListToScanner(final List<String> list) {
        // Merge all elements of the list into a single string, separated by line breaks
        final StringBuilder sb = new StringBuilder();
        for (final String line : list) {
            sb.append(line).append(System.lineSeparator());
        }

        // Create a scanner that uses this string as input source
        return new Scanner(sb.toString());
    }

    public static String convertListToString(final List<String> list) {
        return String.join(System.lineSeparator(), list)
                .replace("\r", ""); // Replace carriage returns
    }

}
