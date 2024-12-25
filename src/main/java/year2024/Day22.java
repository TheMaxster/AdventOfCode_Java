package year2024;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;
import utils.ImportUtils;
import utils.ListUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day22 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day22/input_test_01.txt";

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final int numberOfIterations = 2000;

        final List<Long> numbers = new ArrayList<>();
        for (final String s : input) {
            long tmpLong = Long.parseLong(s);

            for (int i = 0; i < numberOfIterations; i++) {
                tmpLong = doCalculation(tmpLong);
                // log(Long.toString(tmpLong));
            }

            numbers.add(tmpLong);
        }

        return ListUtils.sumUpLong(numbers).toString();
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final int numberOfIterations = 2000;

        final Map<List<Long>, Long> summingMap = new HashMap<>();
        for (final String sequence : input) {
            final Map<List<Long>, Long> sequences2Prices = generateSequences(Long.parseLong(sequence), numberOfIterations);
            sequences2Prices.forEach((sequences, prices) -> summingMap.merge(sequences, prices, Long::sum));
        }
        final long max = summingMap.values().stream().mapToLong(Long::longValue).max().orElseThrow();
        return Long.toString(max);
    }

    private Map<List<Long>, Long> generateSequences(
            final long seed,
            final int numberOfIterations
    ) {
        final Map<List<Long>, Long> result = new HashMap<>();
        long prevSeed = seed;
        long prevPrice = seed % 10L;
        final List<Long> collector = new ArrayList<>();

        for (int i = 0; i < numberOfIterations; i++) {
            final long nextSeed = doCalculation(prevSeed);
            final long price = nextSeed % 10L;
            final long diff = price - prevPrice;

            collector.add(diff);
            if (collector.size() > 4) {
                collector.remove(0); // Manually remove the first element to maintain a sliding window
            }
            if (collector.size() == 4) {
                result.putIfAbsent(new ArrayList<>(collector), price);
            }

            prevPrice = price;
            prevSeed = nextSeed;
        }
        return result;
    }


    private long doCalculation(final long l) {
        final long l2 = prune(mix(l, l * 64));
        final long l3 = prune(mix(l2 / 32, l2));
        final long l4 = prune(mix(l3 * 2048, l3));
        return l4;
    }

    private long mix(
            final long i,
            final long j
    ) {
        return i ^ j;
    }

    private long prune(
            final long i
    ) {
        return i % 16777216;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

}
