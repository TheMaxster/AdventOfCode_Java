package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImportUtils {

    public static List<String> readAsList(final String filePath) {
        final List<String> linesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                linesList.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return linesList;
    }

    public static String[][] readAsArray(final String filePath) {
        final List<String[]> linesList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                // Convert each character to a String and store in an array
                String[] chars = line.split("");
                linesList.add(chars);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert the list to a 2D String array
        String[][] linesArray = new String[linesList.size()][];
        linesList.toArray(linesArray);

        return linesArray;
    }
}
