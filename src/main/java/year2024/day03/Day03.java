package year2024.day03;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import application.Day;

/**
 * See https://adventofcode.com/2024/day/3
 */
public class Day03 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day03/input_test_01.txt";

    public String part1(final List<String> input) {
        //final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final List<Pair<Integer, Integer>> matches = new ArrayList<>();
        input.forEach(s -> matches.addAll(findAllMatches(s)));

        final Integer sum = matches.stream()
                .mapToInt(p -> p.getLeft() * p.getRight())
                .sum();

        return sum.toString();
    }

    private List<Pair<Integer, Integer>> findAllMatches(final String text) {
        final String regex = "mul\\((\\d+),(\\d+)\\)";
        final Matcher matcher = Pattern.compile(regex).matcher(text);

        final List<Pair<Integer, Integer>> matches = new ArrayList<>();

        while (matcher.find()) {
            log(matcher.group());
            matches.add(Pair.of(
                    Integer.valueOf(matcher.group(1)),
                    Integer.valueOf(matcher.group(2))
            ));
        }
        return matches;
    }

    public String part2(final List<String> input) {
        //final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final String longString = String.join("", input);

        final List<Pair<Integer, Integer>> matches = findAllMatchesForPart2(longString);

        final Integer sum = matches.stream()
                .mapToInt(p -> p.getLeft() * p.getRight())
                .sum();

        return sum.toString();
    }

    private List<Pair<Integer, Integer>> findAllMatchesForPart2(final String text) {
        final String regex = "mul\\((\\d+),(\\d+)\\)|do\\(\\)|don't\\(\\)";
        final Matcher matcher = Pattern.compile(regex).matcher(text);

        final List<Pair<Integer, Integer>> matches = new ArrayList<>();

        boolean enabled = true;

        while (matcher.find()) {
            log(enabled + ": " + matcher.group());

            if (Objects.equals(matcher.group(), "do()")) {
                enabled = true;
            } else if (Objects.equals(matcher.group(), "don't()")) {
                enabled = false;
            } else if (enabled) {
                matches.add(Pair.of(
                        Integer.valueOf(matcher.group(1)),
                        Integer.valueOf(matcher.group(2))
                ));
            }
        }

        return matches;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }
}
