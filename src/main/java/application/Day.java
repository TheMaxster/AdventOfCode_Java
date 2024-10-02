package application;

import java.util.List;

public abstract class Day {

    protected void log(final String logStatement) {
        if (getLoggingEnabled()) {
            System.out.println(logStatement);
        }
    }

    public abstract Boolean getLoggingEnabled();

    //    public default abstract void setLoggingEnabled(final Boolean loggingEnabled);

    public abstract String part1(List<String> input);

    public abstract String part2(List<String> input);

}
