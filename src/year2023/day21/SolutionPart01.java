package year2023.day21;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import utils.ImportUtils;
import utils.Utils;

public class SolutionPart01 {

    private record Coordinates(int x, int y) {

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

        public int getDeltaX() {
            return deltaX;
        }

        public int getDeltaY() {
            return deltaY;
        }
    }

    public static void main(final String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/year2023/day21/input_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day21/input.txt";

        final String[][] grid = ImportUtils.readAsArray(filePath);

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

        calculate(start, stepsToMake, grid);

    }

    private static void calculate(
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

            Utils.log("Step: " + i);

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

            Utils.log("Next Coords: " + nextCoords.toString());
            Utils.log("Next Coords Size: " + nextCoords.size());
            Utils.log("Visited Coords: " + visited.toString());
            Utils.log("Visited Size: " + visited.size());

            currentCoords = nextCoords;
            nextCoords = new HashSet<>();

            final String[][] currentGrid = Utils.deepCopy2Array(grid);
            for (int j = 0; j < currentGrid.length; j++) {
                for (int k = 0; k < currentGrid[0].length; k++) {
                    if (currentCoords.contains(new Coordinates(j, k))) {
                        currentGrid[j][k] = "O";
                    }
                }
            }
            Utils.printMap(currentGrid);

        }

        Utils.log("Current garden plots: " + currentCoords.size());
        Utils.log("Visited garden plots: " + visited.size());
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


}
