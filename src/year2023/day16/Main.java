package year2023.day16;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(final String[] args) {
        // final String filePath = System.getProperty("user.dir") + "/resources/year2023/day16/input_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day16/input.txt";
        final String[][] map = ImportUtils.readAsArray(filePath);

        // Part 1
        final Beam startBeamPart1 = new Beam(new Coordinates(0, 0), Direction.EAST);
        final Set<Coordinates> energizedTiles = processMap(startBeamPart1, map);

        final String[][] energizedMap = generateEnergizedMap(map, energizedTiles);
        Utils.printMap(energizedMap);

        Utils.log("Part 1: Total amount of energized tiles is " + energizedTiles.size());

        // Part 2
        int maxAmountOfEnegerizedTiles = 0;

        // Process west end of map.
        for (int i = 0; i < map.length; i++) {
            final Beam startBeamPart2 = new Beam(new Coordinates(i, 0), Direction.EAST);
            Utils.log(startBeamPart2.toString());
            final int amountOfEnergizedTiles = processMap(startBeamPart2, map).size();
            if (amountOfEnergizedTiles > maxAmountOfEnegerizedTiles) {
                maxAmountOfEnegerizedTiles = amountOfEnergizedTiles;
            }
        }

        // Process east end of map.
        for (int i = 0; i < map.length; i++) {
            final Beam startBeamPart2 = new Beam(new Coordinates(i, map[i].length - 1), Direction.WEST);
            Utils.log(startBeamPart2.toString());
            final int amountOfEnergizedTiles = processMap(startBeamPart2, map).size();
            if (amountOfEnergizedTiles > maxAmountOfEnegerizedTiles) {
                maxAmountOfEnegerizedTiles = amountOfEnergizedTiles;
            }
        }

        // Process north end of map.
        for (int i = 0; i < map[0].length; i++) {
            final Beam startBeamPart2 = new Beam(new Coordinates(0, i), Direction.SOUTH);
            Utils.log(startBeamPart2.toString());
            final int amountOfEnergizedTiles = processMap(startBeamPart2, map).size();
            if (amountOfEnergizedTiles > maxAmountOfEnegerizedTiles) {
                maxAmountOfEnegerizedTiles = amountOfEnergizedTiles;
            }
        }

        // Process south end of map.
        for (int i = 0; i < map[0].length; i++) {
            final Beam startBeamPart2 = new Beam(new Coordinates(map.length - 1, i), Direction.NORTH);
            Utils.log(startBeamPart2.toString());
            final int amountOfEnergizedTiles = processMap(startBeamPart2, map).size();
            if (amountOfEnergizedTiles > maxAmountOfEnegerizedTiles) {
                maxAmountOfEnegerizedTiles = amountOfEnergizedTiles;
            }
        }

        Utils.log("Part 2: Max amount of energized tiles is " + maxAmountOfEnegerizedTiles);
    }

    private static Set<Coordinates> processMap(
            final Beam startBeam,
            final String[][] map
    ) {
        final LinkedList<Beam> queue = new LinkedList<>();
        queue.add(startBeam);
        final Set<Beam> allBeams = new HashSet<>();

        while (!queue.isEmpty()) {
            final Beam beam = queue.poll();

            if (allBeams.add(beam)) {
                final List<Direction> nextDirection = calculateNextDirections(map, beam);

                for (final Direction direction : nextDirection) {
                    final Coordinates nextCoords = calculateNextCoords(beam.coords(), direction, map);
                    if (nextCoords != null) {
                        queue.add(new Beam(nextCoords, direction));
                    }
                }


            }
        }

        return allBeams.stream().map(Beam::coords).collect(Collectors.toSet());
    }


    private static List<Direction> calculateNextDirections(
            final String[][] map,
            final Beam beam
    ) {
        final Coordinates coords = beam.coords();
        final Direction direction = beam.direction();
        final String currentLocation = map[coords.x()][coords.y()];

        return switch (currentLocation) {
            case "." -> List.of(beam.direction());
            case "-" -> processHorizontalMirror(direction);
            case "|" -> processVerticalMirror(direction);
            case "/" -> processSlashMirror(direction);
            case "\\" -> processBackSlashMirror(direction);
            default -> throw new IllegalStateException("We should never be here");
        };

    }

    private static String[][] generateEnergizedMap(
            final String[][] map,
            final Set<Coordinates> energizedTiles
    ) {
        final String[][] deepCopy = Utils.deepCopy2Array(map);

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (energizedTiles.contains(new Coordinates(i, j))) {
                    deepCopy[i][j] = "#";
                } else {
                    deepCopy[i][j] = ".";
                }
            }
        }
        return deepCopy;
    }

    private static List<Direction> processHorizontalMirror(final Direction direction) {
        return switch (direction) {
            case NORTH, SOUTH -> List.of(Direction.EAST, Direction.WEST);
            case WEST, EAST -> List.of(direction);
        };
    }

    private static List<Direction> processVerticalMirror(final Direction direction) {
        return switch (direction) {
            case NORTH, SOUTH -> List.of(direction);
            case WEST, EAST -> List.of(Direction.NORTH, Direction.SOUTH);
        };
    }

    private static List<Direction> processSlashMirror(final Direction direction) {
        return switch (direction) {
            case NORTH -> List.of(Direction.EAST);
            case SOUTH -> List.of(Direction.WEST);
            case WEST -> List.of(Direction.SOUTH);
            case EAST -> List.of(Direction.NORTH);
        };

    }

    private static List<Direction> processBackSlashMirror(final Direction direction) {
        return switch (direction) {
            case NORTH -> List.of(Direction.WEST);
            case SOUTH -> List.of(Direction.EAST);
            case WEST -> List.of(Direction.NORTH);
            case EAST -> List.of(Direction.SOUTH);
        };

    }

    private static Coordinates calculateNextCoords(
            final Coordinates coords,
            final Direction direction,
            final String[][] map
    ) {
        final int maxX = map.length;
        final int maxY = map[0].length;
        int nextX = coords.x();
        int nextY = coords.y();

        switch (direction) {
            case NORTH:
                nextX -= 1;
            case SOUTH:
                nextX += 1;
            case EAST:
                nextY += 1;
            case WEST:
                nextY -= 1;
        }

        if (nextX >= 0 && nextX < maxX && nextY >= 0 && nextY < maxY) {
            return new Coordinates(nextX, nextY);
        } else {
            // Utils.log("Coords " + nextX + "/" + nextY + " are not visible.");
            return null;
        }
    }

    private enum Direction {
        NORTH, SOUTH, EAST, WEST
    }

    private record Coordinates(int x, int y) {

    }

    private record Beam(Coordinates coords, Direction direction) {

    }


}
