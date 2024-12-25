package year2024;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ImportUtils;

public class Day17Test {

    private static final String SRC_FILE = "src/main/resources/year2024/day17/input.txt";
    private static final List<String> INPUT_FILE = ImportUtils.readAsList(SRC_FILE);

    private static final Day17 DAY = new Day17();

    @Test
    void testPart1() {

        final String result = DAY.part1(INPUT_FILE);
        Assertions.assertEquals("5,1,3,4,3,7,2,1,7", result);
    }

    @Test
    void testPart2() {
        final String result = DAY.part2(INPUT_FILE);
        Assertions.assertEquals("216584205979245", result);
    }
}
