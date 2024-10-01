package year2023.day18;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Draft_01 {

    private record Instruction(Direction direction, int meters, String colorCode) {

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

    private static final Map<String, Direction> DIRECTION_MAP_PART_1 = Map.of(
            "U", Direction.UP,
            "D", Direction.DOWN,
            "L", Direction.LEFT,
            "R", Direction.RIGHT
    );

    private static final Map<String, Direction> DIRECTION_MAP_PART_2 = Map.of(
            "3", Direction.UP,
            "1", Direction.DOWN,
            "2", Direction.LEFT,
            "0", Direction.RIGHT
    );

    private static record Coordinates(int x, int y) {

    }

    private static final int ROWS = 500;
    private static final int COLUMNS = 500;

    public static void main(final String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/year2023/day18/input_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day18/input.txt";

        final List<String> stringInstructions = ImportUtils.readAsList(filePath);

        calculatePart1(stringInstructions);

        //calculatePart2(stringInstructions);

    }

    //    private static void calculatePart2(final List<String> stringInstructions) {
    //        List<Instruction> instructions = createInstructions(stringInstructions);
    //        List<Instruction> newInstruction = instructions.stream().map(Instruction::colorCode).map(color -> {
    //            int distance = Integer.parseInt(color.substring(2, 7), 16);
    //            Direction direction = DIRECTION_MAP_PART_2.get(color.substring(7, 8));
    //            Utils.log(color + " -> " + distance + " "+direction);
    //            return new Instruction(direction, distance, null);
    //        }).toList();
    //
    //
    //    }


    private static void calculatePart1(final List<String> stringInstructions) {
        final List<Instruction> instructions = createInstructions(stringInstructions);

        final String[][] map = initializeMap();
        drawBoundaries(map, instructions);

        final boolean[][] visited = new boolean[ROWS][COLUMNS];

        // We will do reverse-flood-fill. We flood-fill everything outside the #-barriers.
        final int startX = 0;
        final int startY = 0;

        floodFillIterative(map, visited, startX, startY);
        final long wholeArea = ROWS * COLUMNS;

        final long outerArea = countArea(visited);
        final long innerArea = wholeArea - outerArea;

        Utils.log("Part 1: Inner Area: " + innerArea);
    }

    private static List<Instruction> createInstructions(final List<String> stringInstructions) {
        final List<Instruction> instructions = new ArrayList<>();
        for (final String instruction : stringInstructions) {
            final String[] instructionArray = instruction.split(" ");
            instructions.add(
                    new Instruction(
                            DIRECTION_MAP_PART_1.get(instructionArray[0]),
                            Integer.parseInt(instructionArray[1]),
                            instructionArray[2])
            );
        }
        return instructions;
    }

    private static void drawBoundaries(
            final String[][] map,
            final List<Instruction> instructions
    ) {
        Coordinates current = new Coordinates(150, 100);
        map[current.x][current.y] = "#";

        for (final Instruction instruction : instructions) {
            for (int i = 0; i < instruction.meters(); i++) {
                final int newX = current.x + instruction.direction().deltaX;
                final int newY = current.y + instruction.direction().deltaY;
                map[newX][newY] = "#";
                current = new Coordinates(newX, newY);
            }
        }
    }

    private static String[][] initializeMap() {
        final String[][] map = new String[ROWS][COLUMNS];
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[0].length; j++) {
                map[i][j] = ".";
            }
        }
        return map;
    }

    private static void floodFillIterative(
            final String[][] map,
            final boolean[][] visited,
            final int startX,
            final int startY
    ) {
        final Deque<Coordinates> stack = new ArrayDeque<>();
        stack.push(new Coordinates(startX, startY));

        while (!stack.isEmpty()) {
            final Coordinates p = stack.pop();

            if (p.x < 0 || p.y < 0 || p.x >= ROWS || p.y >= COLUMNS || visited[p.x][p.y] || Objects.equals(map[p.x][p.y], "#")) {
                continue;
            }

            visited[p.x][p.y] = true;
            stack.push(new Coordinates(p.x + 1, p.y));
            stack.push(new Coordinates(p.x - 1, p.y));
            stack.push(new Coordinates(p.x, p.y + 1));
            stack.push(new Coordinates(p.x, p.y - 1));
        }
    }

    private static long countArea(final boolean[][] visited) {
        long count = 0;
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (visited[i][j]) {
                    count++;
                }
            }
        }
        return count;
    }


}
