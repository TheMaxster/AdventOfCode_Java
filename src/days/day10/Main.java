package days.day10;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.Set;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static final String NORTH = "North";
    public static final String SOUTH = "South";
    public static final String EAST = "East";
    public static final String WEST = "West";

    private static final Map<String, List<String>> VALID_FOR_NORTH_MAP = Map.of(
            "L", List.of("|", "F", "7"),
            "J", List.of("|", "F", "7"),
            "|", List.of("|", "F", "7"),
            "S", List.of("|", "F", "7"),
            "F", Collections.emptyList(),
            "7", Collections.emptyList()
    );
    private static final Map<String, List<String>> VALID_FOR_SOUTH_MAP = Map.of(
            "7", List.of("|", "J", "L"),
            "F", List.of("|", "J", "L"),
            "|", List.of("|", "J", "L"),
            "S", List.of("|", "J", "L"),
            "J", Collections.emptyList(),
            "L", Collections.emptyList()
    );
    private static final Map<String, List<String>> VALID_FOR_WEST_MAP = Map.of(
            "J", List.of("-", "F", "L"),
            "7", List.of("-", "F", "L"),
            "-", List.of("-", "F", "L"),
            "S", List.of("-", "F", "L"),
            "F", Collections.emptyList(),
            "L", Collections.emptyList()
    );
    private static final Map<String, List<String>> VALID_FOR_EAST_MAP = Map.of(
            "L", List.of("-", "J", "7"),
            "F", List.of("-", "J", "7"),
            "-", List.of("-", "J", "7"),
            "S", List.of("-", "J", "7"),
            "J", Collections.emptyList(),
            "7", Collections.emptyList()
    );
    private static final Map<String, Map<String, List<String>>> VALID_FOR_DIRECTION_MAP = Map.of(
            NORTH, VALID_FOR_NORTH_MAP,
            SOUTH, VALID_FOR_SOUTH_MAP,
            WEST, VALID_FOR_WEST_MAP,
            EAST, VALID_FOR_EAST_MAP
    );

    private static final List<String> DIRECTIONS = List.of(NORTH, SOUTH, WEST, EAST);

    private static int TOP_TO_BOTTOM;
    private static int LEFT_TO_RIGHT;
    private static String[][] MAP;

    public static void main(String[] args) {
        //         final String filePath = System.getProperty("user.dir") + "/resources/days/day10/input_10_test_01.txt";
        //      final String filePath = System.getProperty("user.dir") + "/resources/days/day10/input_10_test_02.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day10/input_10.txt";

        MAP = ImportUtils.readAsArray(filePath);

        TOP_TO_BOTTOM = MAP.length;
        LEFT_TO_RIGHT = MAP[0].length;

        Coordinate start = getStartCoordinate();
        Utils.log("Start is: " + start.toString());

        Set<Coordinate> visited = goFurther(start);

        long validVisits = visited.stream().filter(Coordinate::valid).count();

        Utils.log("Solution Part 1: We run in the circle with nodes: " + validVisits);
        Utils.log("So the farthest part is the half: " + validVisits / 2);

        String[][] newMap = MAP.clone();
        for (Coordinate coordinate : visited) {
            newMap[coordinate.row()][coordinate.column()] = "X";
        }
  //      printStringArray(newMap);
    }


    private static void printStringArray(String[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                System.out.print(array[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static Set<Coordinate> goFurther(Coordinate start) {
        Set<Coordinate> visited = new HashSet<>();
        Queue<Coordinate> queue = new LinkedList<>();
        queue.add(start);

        int length = 0;

        while (!queue.isEmpty()) {
            Coordinate current = queue.poll();

            Utils.log(current.toString() + " : " + visited.contains(current));

            if (!visited.contains(current)) {
                length++;
                visited.add(current);

                for (String direction : DIRECTIONS) {
                    Coordinate nextCoordinate = checkDirection(current, direction);
                    if (nextCoordinate.valid() && !visited.contains(nextCoordinate)) {
                        queue.add(nextCoordinate);
                    }
                }
            }
        }

        return visited;
    }

    private static Coordinate checkDirection(
            final Coordinate start,
            final String direction
    ) {
        int row = start.row() + (direction.equals(SOUTH) ? 1 : (direction.equals(NORTH) ? -1 : 0));
        int column = start.column() + (direction.equals(EAST) ? 1 : (direction.equals(WEST) ? -1 : 0));

        List<String> validValues = VALID_FOR_DIRECTION_MAP.get(direction).getOrDefault(start.value(), Collections.emptyList());

        if (checkDimensions(row, column) && validValues.contains(MAP[row][column])) {
            return new Coordinate(row, column, MAP[row][column], true);
        }

        return new Coordinate(row, column, "o", false);
    }


    private static boolean checkDimensions(
            final int row,
            final int column
    ) {
        return row >= 0 && row <= TOP_TO_BOTTOM - 1 && column >= 0 && column <= LEFT_TO_RIGHT - 1;
    }


    private static Coordinate getStartCoordinate() {
        for (int i = 0; i < TOP_TO_BOTTOM; i++) {
            for (int j = 0; j < LEFT_TO_RIGHT; j++) {
                if (Objects.equals(MAP[i][j], "S")) {
                    return new Coordinate(i, j, MAP[i][j], true);
                }
            }
        }
        throw new IllegalStateException();
    }


    private record Coordinate(int row, int column, String value, boolean valid) {

    }


}