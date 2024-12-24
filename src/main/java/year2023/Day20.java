package year2023;

import java.util.List;

import application.Day;

public class Day20 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final Day20_SolutionPart01 part01 = new Day20_SolutionPart01();
        return part01.calculate(input);
    }

    @Override
    public String part2(final List<String> input) {
        final Day20_SolutionPart02 part02 = new Day20_SolutionPart02();
        return part02.calculate(input);
    }
}
