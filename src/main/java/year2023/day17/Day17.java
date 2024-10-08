package year2023.day17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import application.Day;
import utils.ArrayUtils;
import utils.ImportUtils;

/**
 * THIS ALGORITHM DOES NOT WORK PROPERLY FOR THE FIRST EXAMPLE! Wir erhalten immer Länge 103 anstelle der richtigen Länge 102. Hier muss
 * noch analysiert werden.
 *
 *
 * See https://adventofcode.com/2023/day/17 .
 */
public class Day17 extends Day {

    private static record Node(int x, int y, int weight, int steps, int lastDirX, int lastDirY, Node parent) {

    }

    private static record ReducedNode(int x, int y, int lastDirX, int lastDirY, int steps) {

    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final String[][] map = ImportUtils.convertListToArray(input);
        return calculatePart1(map);
    }

    @Override
    public String part2(final List<String> input) {
        final String[][] map = ImportUtils.convertListToArray(input);
        map[0][0] = "0";
        return calculatePart2(map);
    }

    private String calculatePart1(final String[][] map) {
        final List<Node> shortestPath = findShortestPath(map, 0, 3);

        final String[][] mapToPrint = ArrayUtils.deepCopy2Array(map);
        for (final Node node : shortestPath) {
            System.out.println("(" + node.x + ", " + node.y + ")");
            mapToPrint[node.x][node.y] = "X";
        }

        logMap(mapToPrint);

        final int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
        log("Solution Part 1: Shortest Path Length: " + sum);
        return String.valueOf(sum);
    }

    private String calculatePart2(final String[][] map) {
        final List<Node> shortestPath = findShortestPath(map, 4, 10);

        final String[][] mapToPrint = ArrayUtils.deepCopy2Array(map);
        for (final Node node : shortestPath) {
            System.out.println("(" + node.x + ", " + node.y + ")");
            mapToPrint[node.x][node.y] = "X";
        }

        logMap(mapToPrint);

        final int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
        log("Solution Part 2: Shortest Path Length:" + sum);
        return String.valueOf(sum);
    }

    private static List<Node> findShortestPath(
            final String[][] grid,
            final int minSteps,
            final int maxSteps
    ) {
        final int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        final int rows = grid.length;
        final int cols = grid[0].length;
        final Node[][] nodeGrid = new Node[rows][cols];
        final Set<ReducedNode> seen = new HashSet<>();

        // Initialisierung der Distanzen und Knoten
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodeGrid[i][j] = new Node(i, j, Integer.MAX_VALUE, 0, -1, -1, null);
            }
        }

        final PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(a -> a.weight));
        final Node startNode = new Node(0, 1, 0, 0, -1, -1, null);
        queue.add(startNode);
        nodeGrid[0][0] = startNode;

        final HashMap<List<Node>, Integer> paths = new HashMap();

        while (!queue.isEmpty()) {
            final Node current = queue.poll();

            final ReducedNode reducedNode = new ReducedNode(current.x, current.y, current.lastDirX, current.lastDirY, current.steps);
            if (seen.contains(reducedNode)) {
                continue;
            }

            seen.add(reducedNode);

            if (current.x == rows - 1 && current.y == cols - 1 && current.steps >= minSteps) {
                return reconstructPath(nodeGrid[rows - 1][cols - 1]);
            }

            final int[][] possibleDirections;
            if (current.steps > 0 && current.steps < minSteps) {
                possibleDirections = new int[][]{{current.lastDirX, current.lastDirY}};
            } else {
                possibleDirections = directions;
            }

            for (final int[] dir : possibleDirections) {
                if (dir[0] != -current.lastDirX || dir[1] != -current.lastDirY) {
                    final int newX = current.x + dir[0];
                    final int newY = current.y + dir[1];
                    final int newSteps = (dir[0] == current.lastDirX && dir[1] == current.lastDirY) ? current.steps + 1 : 1;

                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && newSteps <= maxSteps) {
                        final int newWeight = current.weight + Integer.parseInt(grid[newX][newY]);
                        final Node newNode = new Node(newX, newY, newWeight, newSteps, dir[0], dir[1], current);
                        queue.add(newNode);
                        nodeGrid[newX][newY] = newNode;
                    }
                }
            }
        }

        return Collections.min(paths.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    private static List<Node> reconstructPath(final Node target) {
        final List<Node> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

}
