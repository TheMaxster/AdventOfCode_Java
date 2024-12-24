package utils;

public enum Direction {
    NORTH, SOUTH, EAST, WEST;

    public static Direction getNextDirectionClockwise(final Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.EAST;
            case EAST -> Direction.SOUTH;
            case SOUTH -> Direction.WEST;
            case WEST -> Direction.NORTH;
        };
    }

    public static Direction getNextDirectionCounterClockwise(final Direction direction) {
        return switch (direction) {
            case NORTH -> Direction.WEST;
            case EAST -> Direction.NORTH;
            case SOUTH -> Direction.EAST;
            case WEST -> Direction.SOUTH;
        };
    }
}
