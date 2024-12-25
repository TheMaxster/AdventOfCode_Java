package year2023;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import application.Day;
import utils.ArrayUtils;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2023/day/21
 */
public class Day21 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2023/day21/input_test_01.txt";


    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String[][] grid = ImportUtils.convertListToArray(input);

        Coordinates start = null;
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                if (Objects.equals(grid[i][j], "S")) {
                    start = new Coordinates(j, i);
                    break;
                }
            }
        }

        final int stepsToMake = 64;

        final int gardenPlots = calculate(start, stepsToMake, grid);
        return String.valueOf(gardenPlots);
    }

    @Override
    public String part2(final List<String> input) {
        return "N/A";
    }

    private int calculate(
            final Coordinates start,
            final int stepsToMake,
            final String[][] grid
    ) {
        final Set<Coordinates> visited = new HashSet<>();
        visited.add(start);

        Set<Coordinates> currentCoords = new HashSet<>();
        Set<Coordinates> nextCoords = new HashSet<>();
        currentCoords.add(start);

        for (int i = 1; i <= stepsToMake; i++) {

            log("Step: " + i);

            for (final Coordinates currentCoord : currentCoords) {

                // Check all directions
                for (final Direction value : Direction.values()) {
                    final Optional<Coordinates> newCoordOpt = isDirectionVisitable(currentCoord, grid, value);
                    if (newCoordOpt.isPresent()) {
                        visited.add(newCoordOpt.get());
                        nextCoords.add(newCoordOpt.get());
                    }
                }

            }

            log("Next Coords: " + nextCoords.toString());
            log("Next Coords Size: " + nextCoords.size());
            log("Visited Coords: " + visited.toString());
            log("Visited Size: " + visited.size());

            currentCoords = nextCoords;
            nextCoords = new HashSet<>();

            final String[][] currentGrid = ArrayUtils.deepCopy2Array(grid);
            for (int j = 0; j < currentGrid.length; j++) {
                for (int k = 0; k < currentGrid[0].length; k++) {
                    if (currentCoords.contains(new Coordinates(j, k))) {
                        currentGrid[j][k] = "O";
                    }
                }
            }
            logMap(currentGrid);

        }

        log("Current garden plots: " + currentCoords.size());
        log("Visited garden plots: " + visited.size());

        return currentCoords.size();
    }

    private static Optional<Coordinates> isDirectionVisitable(
            final Coordinates currentCoord,
            final String[][] grid,
            final Direction direction
    ) {
        final int newX = currentCoord.x + direction.deltaX;
        final int newY = currentCoord.y + direction.deltaY;

        if (newX >= 0 && newX < grid.length && newY >= 0 && newY < grid[0].length && !Objects.equals(grid[newX][newY], "#")) {
            return Optional.of(new Coordinates(newX, newY));
        }
        return Optional.empty();
    }

    private enum Direction {
        UP(-1, 0),
        DOWN(1, 0),
        LEFT(0, -1),
        RIGHT(0, 1);

        private final int deltaX;
        private final int deltaY;

        Direction(
                final int deltaX,
                final int deltaY
        ) {
            this.deltaX = deltaX;
            this.deltaY = deltaY;
        }

    }

    private record Coordinates(int x, int y) {

    }


}
