package year2024;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ArrayUtils;
import utils.Coordinate;
import utils.Direction;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/6
 */
public class Day06 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day06/input_test_01.txt";

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] matrix = ImportUtils.convertListToArray(input);

        final Coordinate startingPoint = ArrayUtils.findAllOccurences(matrix, "^").get(0);
        log("Starting point: " + startingPoint);

        final PathFindingResult result = getPointsVisited(matrix, startingPoint);

        return Integer.toString(result.pointsVisited.size());
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final String[][] matrix = ImportUtils.convertListToArray(input);

        final Coordinate startingPoint = ArrayUtils.findAllOccurences(matrix, "^").get(0);
        log("Starting point: " + startingPoint);

        final PathFindingResult result = getPointsVisited(matrix, startingPoint);
        result.pointsVisited.remove(startingPoint);

        final Long foundLoops = result.pointsVisited.stream()
                .map(coord -> createModifiedDeepCopy(matrix, coord))
                .map(copy -> getPointsVisited(copy, startingPoint))
                .filter(PathFindingResult::isLoopFound)
                .count();

        return String.valueOf(foundLoops);
    }

    private PathFindingResult getPointsVisited(
            final String[][] matrix,
            final Coordinate startingPoint
    ) {

        boolean reachedEnd = false;
        Coordinate currentPoint = startingPoint;
        Direction direction = Direction.NORTH;
        final Set<Coordinate> pointsVisited = new HashSet<>();
        pointsVisited.add(startingPoint);
        int stepsMade = 0;
        boolean loopFound = false;

        while (!reachedEnd) {

            // That is an assumption we make for part 2!
            if (stepsMade >= 2 * pointsVisited.size()) {
                loopFound = true;
                break;
            }

            final Coordinate nextPoint = currentPoint.nextCoordinate(direction);
            reachedEnd = !ArrayUtils.isWithinBounds(matrix, nextPoint);
            if (reachedEnd) {
                pointsVisited.add(currentPoint);
                stepsMade++;
                continue;
            }

            final String nextPointString = matrix[nextPoint.x][nextPoint.y];
            if (nextPointString.equals("#")) {
                direction = Direction.getNextDirectionClockwise(direction);
            } else {
                pointsVisited.add(currentPoint);
                stepsMade++;
                currentPoint = nextPoint;
            }
        }
        return new PathFindingResult(pointsVisited, loopFound);
    }

    private String[][] createModifiedDeepCopy(
            final String[][] matrix,
            final Coordinate coordinate
    ) {
        final String[][] copy = ArrayUtils.deepCopy2Array(matrix);
        copy[coordinate.x][coordinate.y] = "#";
        return copy;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Data
    @AllArgsConstructor
    public class PathFindingResult {

        private Set<Coordinate> pointsVisited;
        private boolean loopFound;
    }

}
