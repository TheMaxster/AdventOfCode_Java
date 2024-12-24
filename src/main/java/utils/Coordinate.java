package utils;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Coordinate {

    public int x;
    public int y;

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    public int manhattanDistance(final Coordinate other) {
        return Math.abs(x - other.x) + Math.abs(y - other.y);
    }

    public List<Coordinate> getNeighbourhood() {
        return List.of(
                new Coordinate(x + 1, y), new Coordinate(x, y + 1),
                new Coordinate(x - 1, y), new Coordinate(x, y - 1)
        );
    }

    public Coordinate nextCoordinate(
            final Direction dir
    ) {
        return switch (dir) {
            case NORTH -> new Coordinate(this.x - 1, this.y);
            case EAST -> new Coordinate(this.x, this.y + 1);
            case SOUTH -> new Coordinate(this.x + 1, this.y);
            case WEST -> new Coordinate(this.x, this.y - 1);
        };
    }
    //
    //    @Override
    //    public boolean equals(final Object obj) {
    //        if (this == obj){ return true;}
    //        if (obj == null || getClass() != obj.getClass()){ return false;}
    //
    //        Coordinate coord = (Coordinate) obj;
    //        return coord.x == this.x && coord.y == this.y;
    //    }
}
