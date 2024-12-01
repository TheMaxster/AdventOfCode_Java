package year2024.day01;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.tuple.Pair;

import application.Day;
import utils.ImportUtils;
import utils.ListUtils;



/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day01 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day01/input_test_01.txt";
    final List<String> testInput = ImportUtils.readAsList(FILE_PATH);

    public String part1(final List<String> input) {
        final List<String> testInput = ImportUtils.readAsList(FILE_PATH);

        final List<String> inputToUse = input;

        final Pair<List<Integer>, List<Integer>> numberPairs = parseInput(inputToUse);

        numberPairs.getLeft().sort(Integer::compareTo);
        numberPairs.getRight().sort(Integer::compareTo);

        final List<Integer> differences = new ArrayList<>();
        for (int i = 0; i < inputToUse.size(); i++) {
            differences.add(Math.abs(numberPairs.getLeft().get(i) - numberPairs.getRight().get(i)));
        }
        return ListUtils.sumUpInt(differences).toString();
    }

    private Pair<List<Integer>, List<Integer>> parseInput(final List<String> input){
        final List<Integer> leftNumbers = new ArrayList<>();
        final List<Integer> rightNumbers = new ArrayList<>();

        for (final String s : input) {
            final String[] splitArray = s.split("   ");
            leftNumbers.add(Integer.parseInt(splitArray[0]));
            rightNumbers.add(Integer.parseInt(splitArray[1]));
        }

        return Pair.of(leftNumbers, rightNumbers);
    }

    public String part2(final List<String> input) {
        final List<String> testInput = ImportUtils.readAsList(FILE_PATH);

        final List<String> inputToUse = input;

        final Pair<List<Integer>, List<Integer>> numberPairs = parseInput(inputToUse);

        final List<Integer> similarityScoreList = new ArrayList<>();
        for (final Integer leftNumber : numberPairs.getLeft()) {
            int multiplier = 0;
            for (final Integer rightNumber : numberPairs.getRight()) {
                if(Objects.equals(leftNumber, rightNumber)) {
                    multiplier++;
                }
            }
            similarityScoreList.add(leftNumber * multiplier);
        }

        return ListUtils.sumUpInt(similarityScoreList).toString();

    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }
}
