//package main.java.year2023.day17;
//
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.Comparator;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Map;
//import java.util.PriorityQueue;
//import java.util.Set;
//
//import main.java.utils.ImportUtils;
//import utils.ArrayUtils;
//
//public class Main_02 {
//
//    private static record Node(int x, int y, int weight, int steps, int lastDirX, int lastDirY, Node parent) {
//
//    }
//
//    private static record ReducedNode(int x, int y, int lastDirX, int lastDirY, int steps) {
//
//    }
//
//    /**
//     * THIS ALGORITHM DOES NOT WORK PROPERLY FOR THE FIRST EXAMPLE! Wir erhalten immer Länge 103 anstelle der richtigen Länge 102. Hier muss
//     * noch analysiert werden.
//     *
//     * @param args
//     */
//    public static void main(final String[] args) {
//        String filePath = null;
//        // filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day17/input_test_01.txt";
//        // filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day17/input_test_02.txt";
//        filePath = System.getProperty("user.dir") + "/resources/main.resources.year2023/day17/input.txt";
//
//        final String[][] map = ImportUtils.readAsArray(filePath);
//
//        // map[0][0]="0";
//
//        calculatePart1(map);
//
//        //    calculatePart2(map);
//
//    }
//
//    private static void calculatePart2(
//            final String[][] map
//    ) {
//        final List<Node> shortestPath = findShortestPath(map, 4, 10);
//
//        final String[][] mapToPrint = ArrayUtils.deepCopy2Array(map);
//        for (final Node node : shortestPath) {
//            System.out.println("(" + node.x + ", " + node.y + ")");
//            mapToPrint[node.x][node.y] = "X";
//        }
//
//        ArrayUtils.printMap(mapToPrint);
//
//        final int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
//        ArrayUtils.log("Solution Part 2: Shortest Path Length:" + sum);
//    }
//
//    private static void calculatePart1(final String[][] map) {
//        final List<Node> shortestPath = findShortestPath(map, 0, 3);
//
//        final String[][] mapToPrint = ArrayUtils.deepCopy2Array(map);
//        for (final Node node : shortestPath) {
//            System.out.println("(" + node.x + ", " + node.y + ")");
//            mapToPrint[node.x][node.y] = "X";
//        }
//
//        ArrayUtils.printMap(mapToPrint);
//
//        final int sum = shortestPath.stream().mapToInt(a -> Integer.parseInt(map[a.x][a.y])).sum();
//        ArrayUtils.log("Solution Part 1: Shortest Path Length: " + sum);
//    }
//
//    //    static class Node implements Comparable<Node> {
//    //        int x, y, weight, steps, lastDirX, lastDirY;
//    //        Node parent;
//    //
//    //        public Node(int x, int y, int weight, int steps, int lastDirX, int lastDirY, Node parent) {
//    //            this.x = x;
//    //            this.y = y;
//    //            this.weight = weight;
//    //            this.steps = steps;
//    //            this.lastDirX = lastDirX;
//    //            this.lastDirY = lastDirY;
//    //            this.parent = parent;
//    //        }
//    //
//    //        @Override
//    //        public int compareTo(Node other) {
//    //            return this.weight - other.weight;
//    //        }
//    //    }
//
//
//    private static List<Node> findShortestPath(
//            final String[][] grid,
//            final int minSteps,
//            final int maxSteps
//    ) {
//        final int[][] directions = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}};
//        final int rows = grid.length;
//        final int cols = grid[0].length;
//        final Node[][] nodeGrid = new Node[rows][cols];
//        final Set<ReducedNode> seen = new HashSet<>();
//
//        // Initialisierung der Distanzen und Knoten
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                nodeGrid[i][j] = new Node(i, j, Integer.MAX_VALUE, 0, -1, -1, null);
//            }
//        }
//
//        final PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparing(a -> a.weight));
//        final Node startNode = new Node(0, 1, 0, 0, -1, -1, null);
//        queue.add(startNode);
//        nodeGrid[0][0] = startNode;
//
//        final HashMap<List<Node>, Integer> paths = new HashMap();
//
//        while (!queue.isEmpty()) {
//            final Node current = queue.poll();
//
//            final ReducedNode reducedNode = new ReducedNode(current.x, current.y, current.lastDirX, current.lastDirY, current.steps);
//            if (seen.contains(reducedNode)) {
//                continue;
//            }
//
//            seen.add(reducedNode);
//
//            if (current.x == rows - 1 && current.y == cols - 1 && current.steps >= minSteps) {
//                return reconstructPath(nodeGrid[rows - 1][cols - 1]);
//
//                //                List<Node> path = reconstructPath(nodeGrid[rows - 1][cols - 1]);
//                //int value = path.stream().mapToInt(a -> Integer.parseInt(grid[a.x][a.y])).sum();
//                //                int value = path.get(path.size() - 1).weight();
//                //                paths.put(path, value);
//                //                continue;
//            }
//
//            final int[][] possibleDirections;
//            if (current.steps > 0 && current.steps < minSteps) {
//                possibleDirections = new int[][]{{current.lastDirX, current.lastDirY}};
//            } else {
//                possibleDirections = directions;
//            }
//
//            for (final int[] dir : possibleDirections) {
//                if (dir[0] != -current.lastDirX || dir[1] != -current.lastDirY) {
//                    final int newX = current.x + dir[0];
//                    final int newY = current.y + dir[1];
//                    final int newSteps = (dir[0] == current.lastDirX && dir[1] == current.lastDirY) ? current.steps + 1 : 1;
//
//                    if (newX >= 0 && newX < rows && newY >= 0 && newY < cols && newSteps <= maxSteps) {
//                        final int newWeight = current.weight + Integer.parseInt(grid[newX][newY]);
//                        final Node newNode = new Node(newX, newY, newWeight, newSteps, dir[0], dir[1], current);
//                        queue.add(newNode);
//                        nodeGrid[newX][newY] = newNode;
//                    }
//                }
//            }
//        }
//
//        // return Collections.emptyList(); // Falls kein Pfad gefunden wird
//        return Collections.min(paths.entrySet(), Map.Entry.comparingByValue()).getKey();
//
//    }
//
//    private static List<Node> reconstructPath(final Node target) {
//        final List<Node> path = new ArrayList<>();
//        Node current = target;
//        while (current != null) {
//            path.add(current);
//            current = current.parent;
//        }
//        Collections.reverse(path);
//        return path;
//    }
//
//}
