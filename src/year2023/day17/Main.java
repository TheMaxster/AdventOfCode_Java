package year2023.day17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(final String[] args) {
        //final String filePath = System.getProperty("user.dir") + "/resources/year2023/day17/input_test_01.txt";
        //    final String filePath = System.getProperty("user.dir") + "/resources/year2023/day17/input_test_02.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day17/input.txt";

        final String[][] map = ImportUtils.readAsArray(filePath);

        // map[0][0]="0";

        final List<Node> shortestPath = findShortestPath(map);
        final int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
        Utils.log("Solution Part 1: Shortest Path Length: " + sum);

        for (final Node node : shortestPath) {
            System.out.println("(" + node.x + ", " + node.y + ")");
            map[node.x][node.y] = "X";
        }

        Utils.printMap(map);


    }

    static class Node implements Comparable<Node> {

        int x, y, weight, steps, lastDirX, lastDirY;
        Node parent;

        public Node(
                final int x,
                final int y,
                final int weight,
                final int steps,
                final int lastDirX,
                final int lastDirY,
                final Node parent
        ) {
            this.x = x;
            this.y = y;
            this.weight = weight;
            this.steps = steps;
            this.lastDirX = lastDirX;
            this.lastDirY = lastDirY;
            this.parent = parent;
        }

        @Override
        public int compareTo(final Node other) {
            return this.weight - other.weight;
        }
    }

    private record ReducedNode(int x, int y, int lastDirX, int lastDirY, int steps) {

    }

    public static List<Node> findShortestPath(final String[][] grid) {
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

        final PriorityQueue<Node> queue = new PriorityQueue<>();
        final Node startNode = new Node(0, 0, 0, 0, -1, -1, null);
        queue.add(startNode);
        nodeGrid[0][0] = startNode;

        while (!queue.isEmpty()) {
            final Node current = queue.poll();

            final ReducedNode reducedNode = new ReducedNode(current.x, current.y, current.lastDirX, current.lastDirY, current.steps);
            if (seen.contains(reducedNode)) {
                continue;
            }

            seen.add(reducedNode);

            if (current.x == rows - 1 && current.y == cols - 1) {
                return reconstructPath(nodeGrid[rows - 1][cols - 1]);
            }

            for (final int[] dir : directions) {
                if (dir[0] != -current.lastDirX || dir[1] != -current.lastDirY) {
                    final int newX = current.x + dir[0];
                    final int newY = current.y + dir[1];
                    final int newSteps = (dir[0] == current.lastDirX && dir[1] == current.lastDirY) ? current.steps + 1 : 1;

                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && newSteps <= 3) {
                        final int newWeight = current.weight + Integer.parseInt(grid[newX][newY]);
                        final Node newNode = new Node(newX, newY, newWeight, newSteps, dir[0], dir[1], current);
                        queue.add(newNode);
                        nodeGrid[newX][newY] = newNode;
                    }
                }
            }
        }

        return Collections.emptyList(); // Falls kein Pfad gefunden wird
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
