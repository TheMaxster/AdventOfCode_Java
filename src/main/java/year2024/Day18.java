package year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Day;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day18 extends Day {


    private static final String FILE_PATH = "src/main/resources/year2024/day18/input_test_01.txt";
    private static final int TEST_WIDTH = 7;
    private static final int TEST_HEIGHT = 7;

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int widthToUse = 71; // TEST_WIDTH;
        final int heightToUse = 71; // TEST_HEIGHT;
        final int bytesToFall = 1024; // 12
        final List<String> importToUse = input;

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final int bytesToFall = 12;
        //        final List<String> importToUse = importInput;

        final List<Coordinate> bytes = parseInput(importToUse);
        final Coordinate start = new Coordinate(0, 0);
        final Coordinate end = new Coordinate(widthToUse - 1, heightToUse - 1);

        final String[][] map = createMap(widthToUse, heightToUse, bytesToFall, bytes);

        final int steps = ArrayUtils.findShortestPath(map, start, end).size();

        return String.valueOf(steps);
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int widthToUse = 71; // TEST_WIDTH;
        final int heightToUse = 71; // TEST_HEIGHT;
        final int bytesToFall = 1024; // 12
        final List<String> importToUse = input;

        //        final int widthToUse = TEST_WIDTH;
        //        final int heightToUse = TEST_HEIGHT;
        //        final int bytesToFall = 12;
        //        final List<String> importToUse = importInput;

        final List<Coordinate> bytes = parseInput(importToUse);
        final Coordinate start = new Coordinate(0, 0);
        final Coordinate end = new Coordinate(widthToUse - 1, heightToUse - 1);

        Coordinate result = null;
        for (int i = 1; i < bytes.size(); i++) {
            final String[][] map = createMap(widthToUse, heightToUse, i, bytes);

            final int steps = ArrayUtils.findShortestPath(map, start, end).size();
            if (steps == -1) {
                result = bytes.get(i - 1);
                break;
            }

        }

        return result.y + "," + result.x;
    }

    private static String[][] createMap(
            final int widthToUse,
            final int heightToUse,
            final int bytesToFall,
            final List<Coordinate> bytes
    ) {
        final String[][] map = new String[widthToUse][heightToUse];
        for (final String[] strings : map) {
            Arrays.fill(strings, ".");
        }

        for (int i = 0; i < bytesToFall; i++) {
            final Coordinate aByte = bytes.get(i);
            map[aByte.getX()][aByte.getY()] = "#";
        }
        return map;
    }

    private List<Coordinate> parseInput(final List<String> importInput) {
        final Pattern pattern = Pattern.compile("(\\d+),(\\d+)");
        final List<Coordinate> coordinates = new ArrayList<>();
        for (final String s : importInput) {
            final Matcher matcher = pattern.matcher(s);
            if (matcher.matches()) {
                final int x = Integer.parseInt(matcher.group(1));
                final int y = Integer.parseInt(matcher.group(2));
                coordinates.add(new Coordinate(y, x));
            }
        }
        return coordinates;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

}
