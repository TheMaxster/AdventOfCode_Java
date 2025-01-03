/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package application;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;
import year2023.Day01;
import year2023.Day02;
import year2023.Day03;
import year2023.Day04;
import year2023.Day05;
import year2023.Day06;
import year2023.Day07;
import year2023.Day08;
import year2023.Day09;
import year2023.Day10;
import year2023.Day11;
import year2023.Day12;
import year2023.Day13;
import year2023.Day14;
import year2023.Day15;
import year2023.Day16;
import year2023.Day17;
import year2023.Day18;
import year2023.Day19;
import year2023.Day20;
import year2023.Day21;
import year2023.Day22;
import year2024.Day23;
import year2024.Day24_Reddit;
import year2024.Day25;

public class Application {

    private static final Map<Integer, Day> DAYS2023;
    private static final Map<Integer, Day> DAYS2024;
    private static final Map<Integer, Map<Integer, Day>> YEARS;

    static {
        DAYS2023 = new HashMap<>();
        DAYS2023.put(1, new Day01());
        DAYS2023.put(2, new Day02());
        DAYS2023.put(3, new Day03());
        DAYS2023.put(4, new Day04());
        DAYS2023.put(5, new Day05()); // Part 2 needs a lot of time
        DAYS2023.put(6, new Day06());
        DAYS2023.put(7, new Day07());
        DAYS2023.put(8, new Day08());
        DAYS2023.put(9, new Day09());
        DAYS2023.put(10, new Day10());
        DAYS2023.put(11, new Day11());
        DAYS2023.put(12, new Day12());
        DAYS2023.put(13, new Day13());
        DAYS2023.put(14, new Day14());
        DAYS2023.put(15, new Day15());
        DAYS2023.put(16, new Day16());
        DAYS2023.put(17, new Day17());
        DAYS2023.put(18, new Day18());
        DAYS2023.put(19, new Day19());
        DAYS2023.put(20, new Day20()); // Part 01 doesn't work
        DAYS2023.put(21, new Day21());
        DAYS2023.put(22, new Day22()); // Not existing

        DAYS2024 = new HashMap<>();
        DAYS2024.put(1, new year2024.Day01());
        DAYS2024.put(2, new year2024.Day02());
        DAYS2024.put(3, new year2024.Day03());
        DAYS2024.put(4, new year2024.Day04());
        DAYS2024.put(5, new year2024.Day05());
        DAYS2024.put(6, new year2024.Day06());
        DAYS2024.put(7, new year2024.Day07());
        DAYS2024.put(8, new year2024.Day08());
        DAYS2024.put(9, new year2024.Day09());
        DAYS2024.put(10, new year2024.Day10());
        DAYS2024.put(11, new year2024.Day11());
        DAYS2024.put(12, new year2024.Day12());
        DAYS2024.put(13, new year2024.Day13());
        DAYS2024.put(14, new year2024.Day14());
        DAYS2024.put(15, new year2024.Day15());
        DAYS2024.put(16, new year2024.Day16());
        DAYS2024.put(17, new year2024.Day17());
        DAYS2024.put(18, new year2024.Day18());
        DAYS2024.put(19, new year2024.Day19());
        DAYS2024.put(20, new year2024.Day20());
        DAYS2024.put(21, new year2024.Day21());
        DAYS2024.put(22, new year2024.Day22());
        DAYS2024.put(23, new Day23());
        // DAYS2024.put(24, new Day24());
        DAYS2024.put(24, new Day24_Reddit());
        DAYS2024.put(25, new Day25());

        YEARS = new HashMap<>();
        YEARS.put(2023, DAYS2023);
        YEARS.put(2024, DAYS2024);
    }

    private static List<String> loadInput(
            final int day,
            final int year
    ) {
        String paddedDay = String.valueOf(day);
        if (day < 10) {
            paddedDay = "0" + day;
        }
        final String filePath = "src/main/resources/year" + year + "/day" + paddedDay + "/input.txt";

        return ImportUtils.readAsList(filePath);
    }

    public static void main(final String[] args) {
        final int year = args.length != 0 ? Integer.parseInt(args[0]) : 2023;
        final int day = args.length != 0 ? Integer.parseInt(args[1]) : 1;

        final List<String> input = loadInput(day, year);

        System.out.println("Executing: " + year + "-" + day);
        System.out.println("Solution Part 1: " + YEARS.get(year).get(day).part1(input));
        System.out.println("Solution Part 2: " + YEARS.get(year).get(day).part2(input));

    }
}
