package application;

import java.util.List;

public abstract class Day {

    protected void log(final String logStatement) {
        if (getLoggingEnabled()) {
            System.out.println(logStatement);
        }
    }

    public void logMap(final String[][] array) {
        if (!getLoggingEnabled()) {
            return;
        }

        for (final String[] row : array) {
            for (final String element : row) {
                System.out.print(element + " ");
            }
            System.out.println(); // New line
        }
        System.out.println(" ");
    }

    public void logDenseMap(final String[][] array) {
        if (!getLoggingEnabled()) {
            return;
        }

        for (final String[] row : array) {
            for (final String element : row) {
                System.out.print(element);
            }
            System.out.println(); // New line
        }
    }

    public abstract Boolean getLoggingEnabled();

    //    public default abstract void setLoggingEnabled(final Boolean loggingEnabled);

    public abstract String part1(List<String> input);

    public abstract String part2(List<String> input);

}
