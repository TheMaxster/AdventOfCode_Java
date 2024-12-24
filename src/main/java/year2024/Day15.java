package year2024;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import application.Day;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day15 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day15/input_test_01.txt";
    // private static final String FILE_PATH = "src/main/resources/year2024/day15/input_test_02.txt";
    //   private static final String FILE_PATH = "src/main/resources/year2024/day15/input_test_03.txt";

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Pair<String[][], List<String>> parsedInput = parseInput(input);
        final String[][] map = parsedInput.getLeft();
        final List<String> instructions = parsedInput.getRight();

        logMap(map);
        log(instructions.toString());

        final List<Coordinate> boxes = calculate(map, instructions);
        log(boxes.toString());
        final long sum = boxes.stream().mapToLong(box -> box.getX() * 100 + box.getY()).sum();

        return String.valueOf(sum);

    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Pair<String[][], List<String>> parsedInput = parseInput(input);
        final String[][] map = parsedInput.getLeft();
        final List<String> instructions = parsedInput.getRight();

        final String[][] scaledUpMap = scaleUpMap(map);
        logMap(scaledUpMap);
        log(instructions.toString());

        final List<Coordinate> boxes = calculateForPart2(scaledUpMap, instructions);
        log(boxes.toString());
        final long sum = boxes.stream().mapToLong(box -> box.getX() * 100 + box.getY()).sum();

        return String.valueOf(sum);
    }

    private String[][] scaleUpMap(final String[][] map) {
        final String[][] scaledUpMap = new String[map.length][2 * map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                if (Objects.equals(map[i][j], "#")) {
                    scaledUpMap[i][2 * j] = "#";
                    scaledUpMap[i][2 * j + 1] = "#";
                } else if (map[i][j].equals("O")) {
                    scaledUpMap[i][2 * j] = "[";
                    scaledUpMap[i][2 * j + 1] = "]";
                } else if (map[i][j].equals(".")) {
                    scaledUpMap[i][2 * j] = ".";
                    scaledUpMap[i][2 * j + 1] = ".";
                } else if (map[i][j].equals("@")) {
                    scaledUpMap[i][2 * j] = "@";
                    scaledUpMap[i][2 * j + 1] = ".";
                }
            }
        }

        return scaledUpMap;
    }

    private List<Coordinate> calculate(
            final String[][] map,
            final List<String> instructions
    ) {
        final List<List<Coordinate>> boxes = ArrayUtils.findAllOccurences(map, "O").stream().map(List::of).toList();
        final Coordinate start = ArrayUtils.findAllOccurences(map, "@").get(0);
        final List<Coordinate> frame = ArrayUtils.findAllOccurences(map, "#");

        instructions.forEach(instruction -> {
            move(start, boxes, frame, parseDirection(instruction));
            log("Instruction: " + instruction);
            if (getLoggingEnabled()) {
                createAndLogMap(start, boxes, frame, map);
            }
        });

        return boxes.stream().flatMap(Collection::stream).toList();
    }

    private List<Coordinate> calculateForPart2(
            final String[][] map,
            final List<String> instructions
    ) {
        final List<Coordinate> boxes = ArrayUtils.findAllOccurences(map, "[");
        final Coordinate start = ArrayUtils.findAllOccurences(map, "@").get(0);
        final List<Coordinate> frames = ArrayUtils.findAllOccurences(map, "#");

        final List<List<Coordinate>> part2Boxes = boxes.stream()
                .map(box -> List.of(box, new Coordinate(box.getX(), box.getY() + 1)))
                .toList();

        instructions.forEach(instruction -> {
            move(start, part2Boxes, frames, parseDirection(instruction));
            log("Instruction: " + instruction);
            if (getLoggingEnabled()) {
                createAndLogMap(start, part2Boxes, frames, map);
            }
        });

        return boxes;
    }

    private Pair<Integer, Integer> parseDirection(final String instruction) {
        return switch (instruction) {
            case "<" -> Pair.of(0, -1);
            case ">" -> Pair.of(0, 1);
            case "v" -> Pair.of(1, 0);
            case "^" -> Pair.of(-1, 0);
            default -> Pair.of(0, 0);
        };
    }

    private void move(
            final Coordinate start,
            final List<List<Coordinate>> boxes,
            final List<Coordinate> frames,
            final Pair<Integer, Integer> direction
    ) {
        final Queue<List<Coordinate>> coordsToCheck = new ArrayDeque<>();
        coordsToCheck.add(List.of(start));
        final Set<List<Coordinate>> objectsToMove = new HashSet<>();
        objectsToMove.add(List.of(start));

        while (!coordsToCheck.isEmpty()) {
            final List<Coordinate> object = coordsToCheck.poll();
            for (final Coordinate coord : object) {
                final int newX = coord.getX() + direction.getLeft();
                final int newY = coord.getY() + direction.getRight();

                if (frames.contains(new Coordinate(newX, newY))) {
                    return;
                }

                boxes.forEach(box -> {
                    if (box.contains(new Coordinate(newX, newY)) && objectsToMove.add(box)) {
                        coordsToCheck.add(box);
                    }
                });
            }
        }

        objectsToMove.forEach(object -> object.forEach(coord -> {
            coord.setX(coord.getX() + direction.getLeft());
            coord.setY(coord.getY() + direction.getRight());
        }));
    }

    private void createAndLogMap(
            final Coordinate start,
            final List<List<Coordinate>> boxes,
            final List<Coordinate> frames,
            final String[][] map
    ) {
        final String[][] tmpMap = new String[map.length][map[0].length];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                tmpMap[i][j] = ".";

                for (final List<Coordinate> box : boxes) {
                    for (final Coordinate boxTile : box) {
                        if (boxTile.x == i && boxTile.y == j) {
                            tmpMap[i][j] = "O";
                            break;
                        }
                    }

                }

                for (final Coordinate frame : frames) {
                    if (frame.x == i && frame.y == j) {
                        tmpMap[i][j] = "#";
                        break;
                    }
                }

                if (start.x == i && start.y == j) {
                    tmpMap[i][j] = "@";
                }


            }


        }

        logMap(tmpMap);
    }

    private Pair<String[][], List<String>> parseInput(final List<String> importInput) {
        final List<String> mapLines = new ArrayList<>();
        final StringBuilder instructions = new StringBuilder();

        importInput.forEach(line -> {
            if (line.isEmpty()) {
                return;
            }
            if (line.matches("[<>v^]+")) {
                instructions.append(line);
            } else {
                mapLines.add(line);
            }
        });

        return Pair.of(ImportUtils.convertListToArray(mapLines), List.of(instructions.toString().split("")));
    }


}
