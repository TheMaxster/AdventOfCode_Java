package year2024;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day14 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day14/input_test_01.txt";
    private static final int TEST_WIDTH = 11;
    private static final int TEST_HEIGHT = 7;
    private static final int WIDTH = 101; //TEST_WIDTH;
    private static final int HEIGHT = 103;// TEST_HEIGHT;
    private static final Pattern PATTERN = Pattern.compile("p=(-?\\d+),(-?\\d+) v=(-?\\d+),(-?\\d+)");

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final int numberOfSeconds = 100;
        final List<Robot> robots = parseInput(input);
        final List<Robot> newRobots = calculateNewPositions(robots, WIDTH, HEIGHT, numberOfSeconds);

        final int medianWidth = (WIDTH - 1) / 2;
        final int medianHeight = (HEIGHT - 1) / 2;

        final List<Integer> sectorResults = calculateSectors(newRobots, medianWidth, medianHeight);
        final int result = sectorResults.stream().reduce(1, (a, b) -> a * b);

        return String.valueOf(result);
    }

    public String part2(final List<String> input) {
        final List<Robot> robots = parseInput(input);

        int numberOfSeconds = 0;
        int maxSectorResult = 0;
        List<Robot> newRobots = robots;

        while (maxSectorResult < (robots.size() / 2)) { // We assume that more than half of the robots are needed to form the tree
            numberOfSeconds++;
            newRobots = calculateNewPositions(robots, WIDTH, HEIGHT, numberOfSeconds);

            final int medianWidth = (WIDTH - 1) / 2;
            final int medianHeight = (HEIGHT - 1) / 2;
            maxSectorResult = calculateSectors(newRobots, medianWidth, medianHeight).stream().max(Integer::compareTo).get();
        }

        // Visual check
        final String[][] map = new String[HEIGHT][HEIGHT];
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                map[i][j] = ".";
            }
        }
        newRobots.forEach(robot -> {
            map[robot.getX()][robot.getY()] = "#";
        });
        logMap(map);

        return String.valueOf(numberOfSeconds);
    }

    private List<Robot> parseInput(final List<String> input) {
        final List<Robot> robots = new ArrayList<>();
        int id = 1;
        for (final String s : input) {
            final Matcher matcher = PATTERN.matcher(s);
            if (matcher.matches()) {
                final int x = Integer.parseInt(matcher.group(1));
                final int y = Integer.parseInt(matcher.group(2));
                final int dx = Integer.parseInt(matcher.group(3));
                final int dy = Integer.parseInt(matcher.group(4));
                final Robot robot = new Robot(id++, x, y, dx, dy);
                robots.add(robot);
            }
        }

        return robots;
    }

    private List<Integer> calculateSectors(
            final List<Robot> robots,
            final int medianWidth,
            final int medianHeight
    ) {
        int sector1 = 0;
        int sector2 = 0;
        int sector3 = 0;
        int sector4 = 0;

        for (final Robot newRobot : robots) {
            final int x = newRobot.getX();
            final int y = newRobot.getY();
            if (x < medianWidth && y < medianHeight) {
                sector1++;
            } else if (x > medianWidth && y < medianHeight) {
                sector2++;
            } else if (x < medianWidth && y > medianHeight) {
                sector3++;
            } else if (x > medianWidth && y > medianHeight) {
                sector4++;
            }
        }

        return List.of(sector1, sector2, sector3, sector4);

    }

    private List<Robot> calculateNewPositions(
            final List<Robot> robots,
            final int width,
            final int height,
            final int numberOfSeconds
    ) {
        final List<Robot> newRobots = new ArrayList<>();
        for (final Robot robot : robots) {
            final int newX = (robot.getX() + numberOfSeconds * robot.getDx()) % width;
            final int newY = (robot.getY() + numberOfSeconds * robot.getDy()) % height;

            final int correctedNewX = newX < 0 ? newX + width : newX;
            final int correctedNewY = newY < 0 ? newY + height : newY;
            final Robot newRobot = new Robot(robot.getId(), correctedNewX, correctedNewY, robot.getDx(), robot.getDy());
            newRobots.add(newRobot);
            log("Seconds " + numberOfSeconds + ": Id: " + newRobot.getId() + " -> " + newRobot.getX() + " " + newRobot.getY());
        }
        return newRobots;
    }


    @Data
    @AllArgsConstructor
    private static class Robot {

        int id;
        int x;
        int y;
        int dx;
        int dy;
    }


}
