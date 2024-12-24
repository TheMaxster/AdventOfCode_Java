package year2023;

import java.util.List;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/19
 */
public class Day19 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final Day19_SolutionPart01 part01 = new Day19_SolutionPart01();
        return part01.calculate(input);
    }

    @Override
    public String part2(final List<String> input) {
        final Day19_SolutionPart02 part02 = new Day19_SolutionPart02();
        return part02.calculate(input);
    }
}
