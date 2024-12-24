package utils;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class State {

    private final Coordinate coordinate;
    private final int score;
    private List<Coordinate> path;
    private Direction direction;

    public State(
            final Coordinate coordinate,
            final int score,
            final List<Coordinate> path
    ) {
        this.coordinate = coordinate;
        this.score = score;
        this.path = path;
    }
}
