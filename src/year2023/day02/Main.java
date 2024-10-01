package year2023.day02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;

public class Main {

    /**
     * The configuration for the balls.
     */
    private enum BallConfiguration {
        RED(12),
        GREEN(13),
        BLUE(14);

        private final int maxAmount;

        BallConfiguration(int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }
    }

    public static void main(String[] args) {

        // final String filePath = System.getProperty("user.dir") + "/resources/days/day02/input_02_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day02/input_02.txt";

        final List<String> lines = ImportUtils.readAsList(filePath);

        // Map for saving id (key) and the game result map (value)
        final HashMap<Integer, Map<String, Integer>> resultMap = new HashMap<>();
        lines.stream().forEach(line -> processGame(line, resultMap));

        System.out.println("ResultMap: " + resultMap);

        // Part 1:
        final List<Integer> validIdNumberList = calculatePart1(resultMap);
        System.out.println("ValidIdNumberList: " + validIdNumberList);
        final int result = validIdNumberList.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Solution Part 1: " + result);

        // Part 2:
        final List<Integer> productList = calculatePart2(resultMap);
        System.out.println("ProductList: " + productList);
        final int result2 = productList.stream().mapToInt(Integer::intValue).sum();
        System.out.println("Solution Part 2: " + result2);
    }

    public static void processGame(
            final String line,
            final Map<Integer, Map<String, Integer>> resultMap
    ) {
        // Split the line into id and the actual game results.
        final String[] parts = line.split(":");
        if (parts.length == 2) {
            final String[] gameIdArray = parts[0].split("Game ");
            int gameId = Integer.parseInt(gameIdArray[1]);

            // System.out.println("Game " + gameId + ": ");

            // Map to save the game results of each turn.
            final Map<String, Integer> maxColorMapOfGame = new HashMap();

            final String[] games = parts[1].split(";");
            for (String game : games) {

                // Map for saving of the appearance of each color for the specific turn.
                final Map<String, Integer> colorMapOfTurn = processGameSection(game);

                // Save only the maximum appearance of each color in the map.
                for (Map.Entry<String, Integer> turnEntry : colorMapOfTurn.entrySet()) {
                    final String turnKey = turnEntry.getKey();
                    if (maxColorMapOfGame.containsKey(turnKey) && maxColorMapOfGame.get(turnKey) < colorMapOfTurn.get(turnKey)) {
                        maxColorMapOfGame.put(turnKey, colorMapOfTurn.get(turnKey));
                    } else if (!maxColorMapOfGame.containsKey(turnKey)) {
                        maxColorMapOfGame.put(turnKey, colorMapOfTurn.get(turnKey));
                    }
                }
            }

            resultMap.put(gameId, maxColorMapOfGame);

        } else {
            // We should never get here.
            System.out.println("Invalid game data format: " + line);
        }
    }

    public static Map<String, Integer> processGameSection(String section) {
        final String[] colors = section.trim().split(",");
        final Map<String, Integer> colorMap = new HashMap<>();

        for (String color : colors) {
            final String[] colorInfo = color.trim().split(" ");
            if (colorInfo.length == 2) {
                final String colorName = colorInfo[1].toLowerCase();
                int count = Integer.parseInt(colorInfo[0]);
                colorMap.put(colorName, count);
            } else {
                // We should never get here.
                System.out.println("Invalid color format: " + color);
            }
        }

        // Output the color counts for the current game section
        System.out.println(colorMap);

        return colorMap;
    }

    private static List<Integer> calculatePart2(final HashMap<Integer, Map<String, Integer>> resultMap) {
        return resultMap.values().stream()
                .map(Main::getProductOfNecessaryBalls)
                .toList();
    }

    private static int getProductOfNecessaryBalls(final Map<String, Integer> maxColorMapOfGame) {
        return maxColorMapOfGame.values().stream()
                .mapToInt(value -> value != null ? value : 1)
                .reduce(1, (x, y) -> x * y);
    }

    private static List<Integer> calculatePart1(final HashMap<Integer, Map<String, Integer>> resultMap) {
        return resultMap.entrySet().stream()
                .filter(entry -> isGameValid(entry.getValue()))
                .map(Map.Entry::getKey)
                .toList();
    }

    private static boolean isGameValid(final Map<String, Integer> colorMapOfGame) {
        for (BallConfiguration value : BallConfiguration.values()) {
            String valueName = value.name().toLowerCase();
            boolean isGameValid = colorMapOfGame.containsKey(valueName)
                    && colorMapOfGame.get(valueName) != null
                    && colorMapOfGame.get(valueName) <= value.maxAmount;
            if (!isGameValid) {
                return false;
            }
        }
        return true;
    }

}
