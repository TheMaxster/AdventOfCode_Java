package year2023;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import utils.ImportUtils;

public class Day21Test {

    private static final String SRC_FILE = "src/main/resources/year2123/day21/input.txt";
    private static final List<String> INPUT_FILE = ImportUtils.readAsList(SRC_FILE);

    private static final Day21 DAY = new Day21();

    @Test
    void testPart1() {
        final String result = DAY.part1(INPUT_FILE);
        Assertions.assertEquals("3731", result);
    }

    //    @Test
    //    void testPart2() {
    //        final String result = DAY.part2(INPUT_FILE);
    //        Assertions.assertEquals("238421328103151", result);
    //    }
}
