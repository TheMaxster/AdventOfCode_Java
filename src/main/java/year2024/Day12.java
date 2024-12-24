package year2024;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/12
 */
public class Day12 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day12/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(input);
        final GardenPlot[][] gardenMap = parseInput(map);

        final Map<String, List<GardenPlot>> groupedGardens = Arrays.stream(gardenMap)
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(GardenPlot::getRegionId));

        final Pair<Integer, Integer> prices = calculatePrice(groupedGardens);
        return Integer.toString(prices.getLeft());
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] map = ImportUtils.convertListToArray(input);
        final GardenPlot[][] gardenMap = parseInput(map);

        final Map<String, List<GardenPlot>> groupedGardens = Arrays.stream(gardenMap)
                .flatMap(Arrays::stream)
                .collect(Collectors.groupingBy(GardenPlot::getRegionId));

        final Pair<Integer, Integer> prices = calculatePrice(groupedGardens);
        return Integer.toString(prices.getRight());
    }

    private Pair<Integer, Integer> calculatePrice(final Map<String, List<GardenPlot>> mappedGarden) {
        int totalPrice1 = 0;
        int totalPrice2 = 0;
        for (final Map.Entry<String, List<GardenPlot>> entry : mappedGarden.entrySet()) {
            log("Region: " + entry.getKey() + ": " + entry.getValue().get(0).getLetter());
            final int areaSize = entry.getValue().size();
            log("AreaSize: " + areaSize);
            final int perimeter = entry.getValue().stream().mapToInt(GardenPlot::getDiffNeighbors).sum();
            log("Perimeter: " + perimeter);
            final int price = areaSize * perimeter;
            log("Price: " + price);
            final int sides = entry.getValue().stream().mapToInt(GardenPlot::getCornerAmount).sum();
            log("Sides: " + sides);
            final int price2 = areaSize * sides;
            log("Price2: " + price2);
            log(""); // New line
            totalPrice1 += price;
            totalPrice2 += price2;
        }
        return Pair.of(totalPrice1, totalPrice2);
    }

    private GardenPlot[][] parseInput(final String[][] map) {
        final int rows = map.length;
        final int cols = map[0].length;
        final GardenPlot[][] gardenMap = new GardenPlot[rows][cols];

        int regionId = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (gardenMap[i][j] != null) {
                    continue;
                }

                fillRegion(map, gardenMap, i, j, regionId);
                regionId++;
            }
        }
        return gardenMap;
    }

    private void fillRegion(
            final String[][] map,
            final GardenPlot[][] gardenMap,
            final int startX,
            final int startY,
            final int regionId
    ) {
        final String regionLetter = map[startX][startY];
        final Queue<Coordinate> queue = new ArrayDeque<>();
        queue.add(new Coordinate(startX, startY));

        while (!queue.isEmpty()) {
            final Coordinate current = queue.poll();
            final int x = current.x;
            final int y = current.y;

            if (gardenMap[x][y] != null) {
                continue;
            }

            final int diffNeighbors = countDifferentNeighbors(map, x, y);
            final int cornerAmount = countCorners(map, x, y);

            gardenMap[x][y] = new GardenPlot(
                    true,
                    String.valueOf(regionId),
                    regionLetter,
                    x,
                    y,
                    diffNeighbors,
                    cornerAmount
            );

            // Find and process surrounding coordinates with the same region letter
            final List<Coordinate> neighbors = ArrayUtils.filterSurroundingCoordinates(map, current, regionLetter);
            for (final Coordinate neighbor : neighbors) {
                if (gardenMap[neighbor.x][neighbor.y] == null) {
                    queue.add(neighbor);
                }
            }
        }
    }

    public static int countDifferentNeighbors(
            final String[][] map,
            final int x,
            final int y
    ) {
        final int[][] directions = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        final String current = map[x][y];
        int count = 0;

        for (final int[] dir : directions) {
            final int nx = x + dir[0];
            final int ny = y + dir[1];
            if (!ArrayUtils.isWithinBounds(map, nx, ny) || !Objects.equals(map[nx][ny], current)) {
                count++;
            }
        }

        return count;
    }

    private static int countCorners(
            final String[][] map,
            final int i,
            final int j
    ) {
        // Check all possible "L" shaped patterns around the current point
        final String current = map[i][j];
        int corners = 0;

        // First check the outer corners.
        corners += (differs(map, i - 1, j, current) && differs(map, i, j - 1, current)) ? 1 : 0;
        corners += (differs(map, i - 1, j, current) && differs(map, i, j + 1, current)) ? 1 : 0;
        corners += (differs(map, i + 1, j, current) && differs(map, i, j - 1, current)) ? 1 : 0;
        corners += (differs(map, i + 1, j, current) && differs(map, i, j + 1, current)) ? 1 : 0;

        // Second check the inner corners.
        corners += (same(map, i - 1, j, current) && same(map, i, j - 1, current) && differs(map, i - 1, j - 1, current)) ? 1 : 0;
        corners += (same(map, i - 1, j, current) && same(map, i, j + 1, current) && differs(map, i - 1, j + 1, current)) ? 1 : 0;
        corners += (same(map, i + 1, j, current) && same(map, i, j - 1, current) && differs(map, i + 1, j - 1, current)) ? 1 : 0;
        corners += (same(map, i + 1, j, current) && same(map, i, j + 1, current) && differs(map, i + 1, j + 1, current)) ? 1 : 0;

        return corners;
    }

    private static boolean differs(
            final String[][] map,
            final int i,
            final int j,
            final String current
    ) {
        return !ArrayUtils.isWithinBounds(map, i, j) || !Objects.equals(map[i][j], current);
    }

    private static boolean same(
            final String[][] map,
            final int i,
            final int j,
            final String current
    ) {
        return !differs(map, i, j, current);
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Data
    @AllArgsConstructor
    public static class GardenPlot {

        boolean visited;
        String regionId;
        String letter;
        int x, y;
        int diffNeighbors, cornerAmount;
    }
}
