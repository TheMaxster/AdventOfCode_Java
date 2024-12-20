package utils;

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
