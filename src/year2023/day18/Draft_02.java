package year2023.day18;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import utils.ImportUtils;
import utils.Utils;

public class Draft_02 {

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

    public static void main(final String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day18/input_test_01.txt";
        // final String filePath = System.getProperty("user.dir") + "/resources/year2023/day18/input.txt";

        final List<String> stringInstructions = ImportUtils.readAsList(filePath);

        final List<Instruction> instructions = createInstructions(stringInstructions);
        calculatePart1(instructions);


    }

    private static void calculatePart1(final List<Instruction> instructions) {

        final Set<Coordinates> boundaries = createBoundaries(instructions);

        // Startpunkt innerhalb des Gebiets (muss manuell festgelegt werden)
        final int startX = 3; // Ein Punkt innerhalb des Gebiets
        final int startY = 3; // Ein Punkt innerhalb des Gebiets

        final Set<Coordinates> visited = floodFillIterative(boundaries, startX, startY);

        final long innerArea = countArea(visited);
        final long boundariesArea = boundaries.size();

        Utils.log("Part 1: Inner Area: " + (innerArea + boundariesArea));
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

    private static Set<Coordinates> createBoundaries(

            final List<Instruction> instructions
    ) {
        final Set<Coordinates> boundaries = new HashSet<>();
        Coordinates current = new Coordinates(0, 0);
        boundaries.add(current);

        for (final Instruction instruction : instructions) {
            for (int i = 0; i < instruction.meters(); i++) {
                final int newX = current.x + instruction.direction().deltaX;
                final int newY = current.y + instruction.direction().deltaY;
                boundaries.add(new Coordinates(newX, newY));
                current = new Coordinates(newX, newY);
            }
        }

        return boundaries;
    }

    private static Set<Coordinates> floodFillIterative(
            final Set<Coordinates> boundaries,
            final int startX,
            final int startY
    ) {
        final Set<Coordinates> visited = new HashSet<>();

        final Deque<Coordinates> stack = new ArrayDeque<>();
        stack.push(new Coordinates(startX, startY));

        while (!stack.isEmpty()) {
            final Coordinates p = stack.pop();

            if (visited.contains(p) || boundaries.contains(p)) {
                continue;
            }

            visited.add(p);
            stack.push(new Coordinates(p.x + 1, p.y));
            stack.push(new Coordinates(p.x - 1, p.y));
            stack.push(new Coordinates(p.x, p.y + 1));
            stack.push(new Coordinates(p.x, p.y - 1));
        }

        return visited;
    }

    private static long countArea(final Set<Coordinates> visited) {
        return visited.size();
    }


}
