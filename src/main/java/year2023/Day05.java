package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2023/day/5
 */
public class Day05 extends Day {

    private static List<MappingData> seedToSoilMap = new ArrayList<>();
    private static List<MappingData> soilToFertilizerMap = new ArrayList<>();
    private static List<MappingData> fertilizerToWaterMap = new ArrayList<>();
    private static List<MappingData> waterToLightMap = new ArrayList<>();
    private static List<MappingData> lightToTemperatureMap = new ArrayList<>();
    private static List<MappingData> temperatureToHumidityMap = new ArrayList<>();
    private static List<MappingData> humidityToLocationMap = new ArrayList<>();

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String part1(final List<String> input) {
        final List<Long> seedValues = getSeedValues(input);
        final long minValue = seedValues.stream()
                .mapToLong(this::analyzeSeed)
                .min()
                .orElse(0L);

        return Long.toString(minValue);
    }

    @Override
    public String part2(final List<String> input) {
        final List<Long> seedValues = getSeedValues(input);
        final List<Tuple> tupleList = new ArrayList<>();

        for (int i = 0; i < seedValues.size(); i = i + 2) {
            final long start = seedValues.get(i);
            final long range = seedValues.get(i + 1);
            tupleList.add(new Tuple(start, range));
        }

        final List<Long> resultList = tupleList.parallelStream()
                .map(this::analyseNumbersInRange)
                .toList();

        final long minimum = Collections.min(resultList);

        return String.valueOf(minimum);
    }

    public static List<Long> getSeedValues(final List<String> input) {
        final Scanner scanner = ImportUtils.convertListToScanner(input);
        final List<Long> seedValues = Arrays.stream(scanner.nextLine().replace("seeds:", "").trim().split(" "))
                .map(Long::parseLong)
                .collect(Collectors.toList());

        seedToSoilMap = parseMap(scanner);
        soilToFertilizerMap = parseMap(scanner);
        fertilizerToWaterMap = parseMap(scanner);
        waterToLightMap = parseMap(scanner);
        lightToTemperatureMap = parseMap(scanner);
        temperatureToHumidityMap = parseMap(scanner);
        humidityToLocationMap = parseMap(scanner);

        return seedValues;
    }

    private long analyzeSeed(
            final long seed
    ) {
        final long soil = handleMap(seedToSoilMap, seed);
        log("Soil: " + soil);
        final long fertilizer = handleMap(soilToFertilizerMap, soil);
        log("Fertilizer: " + fertilizer);
        final long water = handleMap(fertilizerToWaterMap, fertilizer);
        log("Water: " + water);
        final long light = handleMap(waterToLightMap, water);
        log("Light: " + light);
        final long temperature = handleMap(lightToTemperatureMap, light);
        log("Temperature: " + temperature);
        final long humidity = handleMap(temperatureToHumidityMap, temperature);
        log("Humidity: " + humidity);
        final long location = handleMap(humidityToLocationMap, humidity);
        log("Location: " + location);
        return location;
    }

    private long analyseNumbersInRange(final Tuple tuple) {
        final long step = 1000L;
        final long start = tuple.getStart();
        final long range = tuple.getRange();
        final long end = start + range - 1;

        long minimum = Long.MAX_VALUE;
        Long previousLocation = null;
        Long previousSeed = null;

        for (long current = start; current <= end; current += step) {
            final long location = analyzeSeed(current);
            minimum = Math.min(minimum, location);

            if (previousLocation != null && previousSeed != null) {
                final long expectedDifference = current - previousSeed;
                final long actualDifference = location - previousLocation;
                if (expectedDifference != actualDifference) {
                    log("Linearity assumption broken. Reanalyzing range: " + previousSeed + " to " + current);
                    minimum = reanalyzeRange(previousSeed, current - 1, minimum);
                    previousLocation = null;
                    previousSeed = null;
                    continue;
                }
            }

            previousSeed = current;
            previousLocation = location;
        }

        if (start + range - 1 > end) {
            log("Final pass for remaining range: " + (end + 1) + " to " + (start + range - 1));
            minimum = reanalyzeRange(end + 1, start + range - 1, minimum);
        }

        log("Minimum: " + minimum + " (Range: " + start + ", " + range + ")");
        return minimum;
    }

    private long reanalyzeRange(
            final long rangeStart,
            final long rangeEnd,
            long currentMinimum
    ) {
        for (long current = rangeStart; current <= rangeEnd; current++) {
            final long location = analyzeSeed(current);
            currentMinimum = Math.min(currentMinimum, location);
        }
        return currentMinimum;
    }

    private static List<MappingData> parseMap(final Scanner scanner) {
        scanner.nextLine();
        final List<MappingData> map = new ArrayList<>();
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine().trim();

            // Skip line if empty
            if (line.isEmpty()) {
                break; // We reached the end of the current map.
            }

            if (!line.contains("-to-")) {
                final String[] values = line.split(" ");
                final long destinationRangeStart = Long.parseLong(values[0]);
                final long sourceRangeStart = Long.parseLong(values[1]);
                final long range = Long.parseLong(values[2]);
                map.add(new MappingData(destinationRangeStart, sourceRangeStart, range));
            }
        }
        return map;
    }


    private long handleMap(
            final List<MappingData> map,
            final long input
    ) {
        return map.stream()
                .filter(mappingData -> input >= mappingData.sourceRangeStart()
                        && input < mappingData.sourceRangeStart() + mappingData.range())
                .mapToLong(mappingData -> input + (mappingData.destinationRangeStart() - mappingData.sourceRangeStart()))
                .findFirst()
                .orElse(input);
    }

    public record MappingData(long destinationRangeStart, long sourceRangeStart, long range) {

    }

    @Data
    @AllArgsConstructor
    public static class Tuple {

        private final long start;
        private final long range;
    }


}
