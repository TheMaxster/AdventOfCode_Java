package days.day17;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day17/input_17_test_01.txt";
      //    final String filePath = System.getProperty("user.dir") + "/resources/days/day17/input_17_test_02.txt";
        // final String filePath = System.getProperty("user.dir") + "/resources/days/day17/input_17.txt";

        String[][] map = ImportUtils.readAsArray(filePath);

        // map[0][0]="0";

        List<Node> shortestPath = findShortestPath(map);
        int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
        Utils.log("Solution Part 1: Shortest Path Length: " + sum);

        for (Node node : shortestPath) {
            System.out.println("(" + node.x + ", " + node.y + ")");
            map[node.x][node.y]="X";
        }

        Utils.printMap(map);


    }

    static class Node implements Comparable<Node> {
        int x, y, weight, steps, lastDirX, lastDirY;
        Node parent;

        public Node(int x, int y, int weight, int steps, int lastDirX, int lastDirY, Node parent) {
            this.x = x;
            this.y = y;
            this.weight = weight;
            this.steps = steps;
            this.lastDirX = lastDirX;
            this.lastDirY = lastDirY;
            this.parent = parent;
        }

        @Override
        public int compareTo(Node other) {
            return this.weight - other.weight;
        }
    }

    private record ReducedNode(int x, int y, int lastDirX, int lastDirY, int steps){}

    public static List<Node> findShortestPath(String[][] grid) {
        int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
        int rows = grid.length;
        int cols = grid[0].length;
        Node[][] nodeGrid = new Node[rows][cols];
        Set<ReducedNode> seen = new HashSet<>();

        // Initialisierung der Distanzen und Knoten
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                nodeGrid[i][j] = new Node(i, j, Integer.MAX_VALUE, 0, -1, -1, null);
            }
        }

        PriorityQueue<Node> queue = new PriorityQueue<>();
        Node startNode = new Node(0, 0, 0, 0, -1, -1, null);
        queue.add(startNode);
        nodeGrid[0][0] = startNode;


        while (!queue.isEmpty()) {
            Node current = queue.poll();

            ReducedNode reducedNode = new ReducedNode(current.x, current.y, current.lastDirX, current.lastDirY, current.steps);
            if(seen.contains(reducedNode)){
                continue;
            }

            seen.add(reducedNode);

            if (current.x == rows - 1 && current.y == cols - 1) {
                return reconstructPath(nodeGrid[rows - 1][cols - 1]);
            }

            for (int[] dir : directions) {
                if(dir[0] != -current.lastDirX || dir[1] != -current.lastDirY) {
                    int newX = current.x + dir[0];
                    int newY = current.y + dir[1];
                    int newSteps = (dir[0] == current.lastDirX && dir[1] == current.lastDirY) ? current.steps + 1 : 1;

                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && newSteps <= 3) {
                        int newWeight = current.weight + Integer.parseInt(grid[newX][newY]);
                        Node newNode = new Node(newX, newY, newWeight, newSteps, dir[0], dir[1], current);
                        queue.add(newNode);
                        nodeGrid[newX][newY] = newNode;
                    }
                }
            }
        }

        return Collections.emptyList(); // Falls kein Pfad gefunden wird
    }

    private static List<Node> reconstructPath(Node target) {
        List<Node> path = new ArrayList<>();
        Node current = target;
        while (current != null) {
            path.add(current);
            current = current.parent;
        }
        Collections.reverse(path);
        return path;
    }

}