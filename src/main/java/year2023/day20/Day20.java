package year2023.day20;

import java.util.List;

import application.Day;

public class Day20 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final SolutionPart01 part01 = new SolutionPart01();
        return part01.calculate(input);
    }

    @Override
    public String part2(final List<String> input) {
        final SolutionPart02 part02 = new SolutionPart02();
        return part02.calculate(input);
    }
}
