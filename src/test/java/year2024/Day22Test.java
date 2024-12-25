package year2024;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ImportUtils;

public class Day22Test {

    private static final String SRC_FILE = "src/main/resources/year2024/day22/input.txt";
    private static final List<String> INPUT_FILE = ImportUtils.readAsList(SRC_FILE);

    private static final Day22 DAY = new Day22();

    @Test
    void testPart1() {

        final String result = DAY.part1(INPUT_FILE);
        Assertions.assertEquals("18694566361", result);
    }

    @Test
    void testPart2() {
        final String result = DAY.part2(INPUT_FILE);
        Assertions.assertEquals("2100", result);
    }
}
