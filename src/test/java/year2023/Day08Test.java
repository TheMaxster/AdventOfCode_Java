package year2023;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ImportUtils;

public class Day08Test {

    private static final String SRC_FILE = "src/main/resources/year2023/day08/input.txt";
    private static final List<String> INPUT_FILE = ImportUtils.readAsList(SRC_FILE);

    private static final Day08 DAY = new Day08();

    @Test
    void testPart1() {
        final String result = DAY.part1(INPUT_FILE);
        Assertions.assertEquals("12083", result);
    }

    @Test
    void testPart2() {
        final String result = DAY.part2(INPUT_FILE);
        Assertions.assertEquals("13385272668829", result);
    }
}