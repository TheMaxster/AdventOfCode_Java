package year2023.day02;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2023/day/2
 */
public class Day02 extends Day {

    //    private static final String FILE_PATH = "resources/main.resources.year2023/day02/input_test_01.txt";
    private static final String FILE_PATH = "resources/main.resources.year2023/day02/input.txt";

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {

        // Map for saving id (key) and the game result map (value)
        final HashMap<Integer, Map<String, Integer>> resultMap = new HashMap<>();
        input.stream().forEach(line -> processGame(line, resultMap));

        log("ResultMap: " + resultMap);

        final List<Integer> validIdNumberList = calculatePart1(resultMap);
        log("ValidIdNumberList: " + validIdNumberList);
        return ListUtils.sumUpInt(validIdNumberList).toString();
    }

    @Override
    public String part2(final List<String> input) {

        // Map for saving id (key) and the game result map (value)
        final HashMap<Integer, Map<String, Integer>> resultMap = new HashMap<>();
        input.stream().forEach(line -> processGame(line, resultMap));

        log("ResultMap: " + resultMap);

        final List<Integer> productList = calculatePart2(resultMap);
        log("ProductList: " + productList);
        return ListUtils.sumUpInt(productList).toString();
    }

    /**
     * The configuration for the balls.
     */
    private enum BallConfiguration {
        RED(12),
        GREEN(13),
        BLUE(14);

        private final int maxAmount;

        BallConfiguration(final int maxAmount) {
            this.maxAmount = maxAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }
    }

    //    public static void main(final String[] args) {
    //
    //        final List<String> lines = ImportUtils.readAsList(FILE_PATH);
    //
    //        // Map for saving id (key) and the game result map (value)
    //        final HashMap<Integer, Map<String, Integer>> resultMap = new HashMap<>();
    //        lines.stream().forEach(line -> processGame(line, resultMap));
    //
    //        log("ResultMap: " + resultMap);
    //
    //        // Part 1:
    //        final List<Integer> validIdNumberList = calculatePart1(resultMap);
    //        log("ValidIdNumberList: " + validIdNumberList);
    //        final int result = ListUtils.sumUpInt(validIdNumberList);
    //        log("Solution Part 1: " + result);
    //
    //        // Part 2:
    //        final List<Integer> productList = calculatePart2(resultMap);
    //        log("ProductList: " + productList);
    //        final int result2 = ListUtils.sumUpInt(productList);
    //        log("Solution Part 2: " + result2);
    //    }

    public void processGame(
            final String line,
            final Map<Integer, Map<String, Integer>> resultMap
    ) {
        // Split the line into id and the actual game results.
        final String[] parts = line.split(":");
        if (parts.length == 2) {
            final String[] gameIdArray = parts[0].split("Game ");
            final int gameId = Integer.parseInt(gameIdArray[1]);

            // log("Game " + gameId + ": ");

            // Map to save the game results of each turn.
            final Map<String, Integer> maxColorMapOfGame = new HashMap();

            final String[] games = parts[1].split(";");
            for (final String game : games) {

                // Map for saving of the appearance of each color for the specific turn.
                final Map<String, Integer> colorMapOfTurn = processGameSection(game);

                // Save only the maximum appearance of each color in the map.
                for (final Map.Entry<String, Integer> turnEntry : colorMapOfTurn.entrySet()) {
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
            log("Invalid game data format: " + line);
        }
    }

    public Map<String, Integer> processGameSection(final String section) {
        final String[] colors = section.trim().split(",");
        final Map<String, Integer> colorMap = new HashMap<>();

        for (final String color : colors) {
            final String[] colorInfo = color.trim().split(" ");
            if (colorInfo.length == 2) {
                final String colorName = colorInfo[1].toLowerCase();
                final int count = Integer.parseInt(colorInfo[0]);
                colorMap.put(colorName, count);
            } else {
                // We should never get here.
                log("Invalid color format: " + color);
            }
        }

        // Output the color counts for the current game section
        log(colorMap.toString());

        return colorMap;
    }

    private static List<Integer> calculatePart2(final HashMap<Integer, Map<String, Integer>> resultMap) {
        return resultMap.values().stream()
                .map(Day02::getProductOfNecessaryBalls)
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
        for (final BallConfiguration value : BallConfiguration.values()) {
            final String valueName = value.name().toLowerCase();
            final boolean isGameValid = colorMapOfGame.containsKey(valueName)
                    && colorMapOfGame.get(valueName) != null
                    && colorMapOfGame.get(valueName) <= value.maxAmount;
            if (!isGameValid) {
                return false;
            }
        }
        return true;
    }

}
