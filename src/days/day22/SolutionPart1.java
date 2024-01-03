package days.day22;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Range;

import utils.ImportUtils;

public class SolutionPart1 {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day22/input_22_test_01.txt";
        //   final String filePath = System.getProperty("user.dir") + "/resources/days/day22/input_22.txt";

        List<String> bricksInput = ImportUtils.readAsList(filePath);

        Set<Brick> bricks = new HashSet<>();

        int uid = 100;
        for (String brickInput : bricksInput) {
            String[] split = brickInput.split("~");
            String[] startSplit = split[0].split(",");
            String[] endSplit = split[1].split(",");

            Brick brick = new Brick();
            brick.setUid(uid);
            brick.setX(Integer.parseInt(startSplit[0]));
            brick.setY(Integer.parseInt(startSplit[1]));
            brick.setZ(Integer.parseInt(startSplit[2]));
            brick.setDx(Integer.parseInt(endSplit[0]));
            brick.setDy(Integer.parseInt(endSplit[1]));
            brick.setDz(Integer.parseInt(endSplit[2]));

            bricks.add(brick);
            uid++;
        }

        dropBricks(bricks);


    }

    private static void dropBricks(final Set<Brick> bricks) {
        Map<BaseCoordinate, Integer> smallestEmptyZ = new HashMap<>();
        Set<BaseCoordinate> allRelevantBaseCoordinates = bricks.stream().flatMap(a -> a.getBaseCoordinates()).collect(
                Collectors.toSet());

        Map<Brick, Set<Brick>> restsOn = new HashMap<>();

        Queue<Brick> sortedBricks = new PriorityQueue<>(Comparator.comparing(Brick::getZ));
        sortedBricks.addAll(bricks);

        while(sorted)

    }


    private static record Coordinate(int x, int y, int z) {

    }

    private static record BaseCoordinate(int x, int y) {

    }

    private static class Brick {

        public List<BaseCoordinate> getBaseCoordinates() {
            List<BaseCoordinate> baseCoordinates = new ArrayList<>();
            for (int i = x; i <= dx; i++) {
                for (int j = y; j <= dy; j++) {
                    baseCoordinates.add(new BaseCoordinate(i, j));
                }
            }
            return baseCoordinates;
        }

        public List<Coordinate> getCoordinates() {
            List<Coordinate> coordinates = new ArrayList<>();
            for (int i = x; i <= dx; i++) {
                for (int j = y; j <= dy; j++) {
                    for (int k = z; k <= dz; k++) {

                        coordinates.add(new Coordinate(i, j, k));
                    }
                }
            }
            return coordinates;
        }

        int uid;
        int x;

        public int getUid() {
            return uid;
        }

        public void setUid(final int uid) {
            this.uid = uid;
        }

        public int getX() {
            return x;
        }

        public void setX(final int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(final int y) {
            this.y = y;
        }

        public int getZ() {
            return z;
        }

        public void setZ(final int z) {
            this.z = z;
        }

        public int getDx() {
            return dx;
        }

        public void setDx(final int dx) {
            this.dx = dx;
        }

        public int getDy() {
            return dy;
        }

        public void setDy(final int dy) {
            this.dy = dy;
        }

        public int getDz() {
            return dz;
        }

        public void setDz(final int dz) {
            this.dz = dz;
        }

        int y;
        int z;
        int dx;
        int dy;
        int dz;


    }

}