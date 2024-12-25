package year2023;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

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
        final List<BigInteger> seedValues = getSeedValues(input);
        final List<BigInteger> locations = new ArrayList<>();

        for (final BigInteger seed : seedValues) {
            final BigInteger location = analyzeSeed(seed);
            locations.add(location);
        }

        final BigInteger minValue = locations.stream()
                .min(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
        log("Part 1 Location Minimum: " + minValue.toString());
        return minValue.toString();
    }

    @Override
    public String part2(final List<String> input) {
        final List<BigInteger> seedValues = getSeedValues(input);
        final List<Tuple> tupleList = new ArrayList<>();

        for (int i = 0; i < seedValues.size(); i = i + 2) {
            final BigInteger start = seedValues.get(i);
            final BigInteger range = seedValues.get(i + 1);

            tupleList.add(new Tuple(start, range));
        }

        final List<BigInteger> resultList = tupleList.parallelStream()
                .map(this::analyseNumbersInRange)
                .toList();

        final BigInteger minimum2 = Collections.min(resultList);

        log("Part 2 Location Minimum: " + minimum2.toString());
        return minimum2.toString();
    }

    public static List<BigInteger> getSeedValues(final List<String> input) {
        final List<BigInteger> seedValues = new ArrayList<>();

        final Scanner scanner = ImportUtils.convertListToScanner(input);

        // Read line with seeds.
        if (scanner.hasNextLine()) {
            final String seedsLine = scanner.nextLine();
            final String[] seedTokens = seedsLine.replace("seeds:", "").trim().split(" ");
            for (final String seedToken : seedTokens) {
                seedValues.add(new BigInteger(seedToken));
            }
        }

        // Read all maps.
        seedToSoilMap = parseMap(scanner);
        soilToFertilizerMap = parseMap(scanner);
        fertilizerToWaterMap = parseMap(scanner);
        waterToLightMap = parseMap(scanner);
        lightToTemperatureMap = parseMap(scanner);
        temperatureToHumidityMap = parseMap(scanner);
        humidityToLocationMap = parseMap(scanner);

        scanner.close();

        return seedValues;
    }

    private BigInteger analyzeSeed(
            final BigInteger seed
    ) {
        final BigInteger soil = handleMap(seedToSoilMap, seed);
        log("Soil: " + soil);
        final BigInteger fertilizer = handleMap(soilToFertilizerMap, soil);
        log("Fertilizer: " + fertilizer);
        final BigInteger water = handleMap(fertilizerToWaterMap, fertilizer);
        log("Water: " + water);
        final BigInteger light = handleMap(waterToLightMap, water);
        log("Light: " + light);
        final BigInteger temperature = handleMap(lightToTemperatureMap, light);
        log("Temperature: " + temperature);
        final BigInteger humidity = handleMap(temperatureToHumidityMap, temperature);
        log("Humidity: " + humidity);
        final BigInteger location = handleMap(humidityToLocationMap, humidity);
        log("Location: " + location);
        return location;
    }

    private BigInteger analyseNumbersInRange(final Tuple tuple) {
        final BigInteger start = tuple.getStart();
        final BigInteger range = tuple.getRange();

        final BigInteger step = BigInteger.valueOf(1000);
        final BigInteger end = start.add(range).subtract(BigInteger.ONE);

        BigInteger minimum = null;
        BigInteger previousLocation = null;
        BigInteger previousSeed = null;

        for (BigInteger current = start; current.compareTo(end) <= 0; current = current.add(step)) {
            final BigInteger location = analyzeSeed(current);

            minimum = (minimum == null) ? location : minimum.min(location);

            if (previousLocation != null && previousSeed != null) {
                final BigInteger expectedDifference = current.subtract(previousSeed);
                final BigInteger actualDifference = location.subtract(previousLocation);
                if (!expectedDifference.equals(actualDifference)) {
                    log("Linearity assumption broken. Reanalyzing range: " + previousSeed + " to " + current);
                    minimum = reanalyzeRange(previousSeed, current.subtract(BigInteger.ONE), minimum);
                    previousLocation = null;
                    previousSeed = null;
                    continue;
                }
            }

            previousSeed = current;
            previousLocation = location;
        }

        // Final pass for any remaining seeds in the last range
        if (start.add(range).subtract(BigInteger.ONE).compareTo(end) > 0) {
            log("Final pass for remaining range: " + end.add(BigInteger.ONE) + " to " + start.add(range).subtract(BigInteger.ONE));
            minimum = reanalyzeRange(end.add(BigInteger.ONE), start.add(range).subtract(BigInteger.ONE), minimum);
        }

        log("Minimum: " + minimum + " (Range: " + start + ", " + range + ")");
        return minimum;
    }

    private BigInteger reanalyzeRange(
            final BigInteger rangeStart,
            final BigInteger rangeEnd,
            BigInteger currentMinimum
    ) {
        for (BigInteger current = rangeStart; current.compareTo(rangeEnd) <= 0; current = current.add(BigInteger.ONE)) {
            final BigInteger location = analyzeSeed(current);
            currentMinimum = (currentMinimum == null) ? location : currentMinimum.min(location);
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
                final BigInteger destinationRangeStart = new BigInteger(values[0]);
                final BigInteger sourceRangeStart = new BigInteger(values[1]);
                final BigInteger range = new BigInteger(values[2]);
                map.add(new MappingData(destinationRangeStart, sourceRangeStart, range));
            }
        }
        return map;
    }


    private BigInteger handleMap(
            final List<MappingData> map,
            final BigInteger input
    ) {
        for (final MappingData mappingData : map) {
            final BigInteger sourceRangeStart = mappingData.sourceRangeStart();
            final BigInteger range = mappingData.range();
            final BigInteger sourceRangeEnd = sourceRangeStart.add(range);
            if (input.compareTo(sourceRangeStart) >= 0 && input.compareTo(sourceRangeEnd) < 0) {
                log("Input: " + input);
                log("Source Range start: " + sourceRangeStart);
                log("Source Range end: " + sourceRangeEnd);
                log("Range: " + range);
                return input.add(mappingData.destinationRangeStart().subtract(mappingData.sourceRangeStart()));
            }
        }

        return input;
    }

    public record MappingData(BigInteger destinationRangeStart, BigInteger sourceRangeStart, BigInteger range) {

    }

    @Data
    @AllArgsConstructor
    public class Tuple {

        final BigInteger start;
        final BigInteger range;

        public String toString() {
            return start.toString() + "-" + range.toString();
        }

    }


}
