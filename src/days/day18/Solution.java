package days.day18;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;
import utils.Utils;

/**
 * This is the solution especially for part 2 of day 18, but also working for part 1. It uses the shoelace formula for the calculation of
 * the polygon as well as Pick's theorem.
 *
 * Shoelace formula: https://en.wikipedia.org/wiki/Shoelace_formula
 *
 * Pick's theorem: https://en.wikipedia.org/wiki/Pick%27s_theorem
 */
public class Solution {

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
                int deltaX,
                int deltaY
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

    private static record Coordinates(long x, long y) {

    }

    public static void main(String[] args) {
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day18/input_18_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day18/input_18.txt";

        List<String> stringInstructions = ImportUtils.readAsList(filePath);

        List<Instruction> instructions = createInstructions(stringInstructions);
        BigInteger resultPart1 = calculate(instructions);
        Utils.log("Result Part 1: " + resultPart1);

        List<Instruction> decodedInstructions = decodeInstructions(instructions);
        BigInteger resultPart2 = calculate(decodedInstructions);
        Utils.log("Result Part 2: " + resultPart2);


    }

    private static List<Instruction> decodeInstructions(final List<Instruction> instructions) {
        return instructions.stream().map(Instruction::colorCode).map(color -> {
            int distance = Integer.parseInt(color.substring(2, 7), 16);
            Direction direction = DIRECTION_MAP_PART_2.get(color.substring(7, 8));
            // Utils.log(color + " -> " + distance + " " + direction);
            return new Instruction(direction, distance, null);
        }).toList();
    }

    /**
     * Do the calculation according to the instructions.
     *
     * @param instructions
     *         the instructions
     * @return the result
     */
    private static BigInteger calculate(final List<Instruction> instructions) {

        List<Coordinates> polygonPoints = createPolygon(instructions);

        //         Utils.log(polygons.toString());

        long polygonArea = calculatePolygonArea(polygonPoints);
        long boundaryPoints = instructions.stream().mapToLong(Instruction::meters).sum();

        // We calculate the inner area with picks theorem.
        long innerArea = polygonArea - (boundaryPoints / 2) + 1;

        Utils.log("boundaryPoints: " + boundaryPoints);
        Utils.log("polygonArea: " + polygonArea);
        Utils.log("innerArea: " + innerArea);

        // Return total length as a sum of inner area and boundary area.
        return BigInteger.valueOf(innerArea + boundaryPoints);

    }


    /**
     * We calculate the Polygon area with the formula of shoelace.
     *
     * @param polygonPoints
     *         the points of the polygon
     * @return the area of the polygon
     */
    private static long calculatePolygonArea(List<Coordinates> polygonPoints) {
        long area = 0;
        int n = polygonPoints.size();

        for (int i = 0; i < n; i++) {
            Coordinates current = polygonPoints.get(i);
            Coordinates next = polygonPoints.get((i + 1) % n); // Next point with wrap around.

            area += current.x * next.y - next.x * current.y;
        }

        return Math.abs(area / 2);
    }

    /**
     * We create the instructions according to the string instructions.
     *
     * @param stringInstructions
     *         the string instructions
     * @return a list of instructions in order of the string instructions
     */
    private static List<Instruction> createInstructions(final List<String> stringInstructions) {
        List<Instruction> instructions = new ArrayList<>();
        for (String instruction : stringInstructions) {
            String[] instructionArray = instruction.split(" ");
            instructions.add(
                    new Instruction(
                            DIRECTION_MAP_PART_1.get(instructionArray[0]),
                            Integer.parseInt(instructionArray[1]),
                            instructionArray[2])
            );
        }
        return instructions;
    }

    /**
     * We create the polygon and start on coordinates (0,0).
     *
     * @param instructions
     *         the instructions
     * @return a list with the coordinations of the polygon according to the order in the instructions
     */
    private static List<Coordinates> createPolygon(final List<Instruction> instructions) {
        List<Coordinates> polygons = new ArrayList<>();
        Coordinates current = new Coordinates(0, 0);
        polygons.add(current);

        for (Instruction instruction : instructions) {
            long newX = current.x + instruction.meters * instruction.direction().deltaX;
            long newY = current.y + instruction.meters * instruction.direction().deltaY;
            polygons.add(new Coordinates(newX, newY));
            current = new Coordinates(newX, newY);
        }

        return polygons;
    }


}