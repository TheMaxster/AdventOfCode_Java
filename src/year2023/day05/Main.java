package year2023.day05;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Main {


    private static List<MappingData> seedToSoilMap = new ArrayList<>();
    private static List<MappingData> soilToFertilizerMap = new ArrayList<>();
    private static List<MappingData> fertilizerToWaterMap = new ArrayList<>();
    private static List<MappingData> waterToLightMap = new ArrayList<>();
    private static List<MappingData> lightToTemperatureMap = new ArrayList<>();
    private static List<MappingData> temperatureToHumidityMap = new ArrayList<>();
    private static List<MappingData> humidityToLocationMap = new ArrayList<>();

    public static void main(final String[] args) {
        //final String filePath = System.getProperty("user.dir") + "/resources/year2023/day05/input_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/year2023/day05/input.txt";

        final List<BigInteger> seedValues = new ArrayList<>();

        try {
            final File file = new File(filePath);
            final Scanner scanner = new Scanner(file);

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
        } catch (final FileNotFoundException e) {
            System.err.println("File not found: " + filePath);
            e.printStackTrace();
        }

        // Testausgabe
        System.out.println("Print maps for testing only:");
        printMap(seedToSoilMap);
        printMap(soilToFertilizerMap);
        // Repeat for other maps...

        // Part 1
        final List<BigInteger> locations = new ArrayList<>();

        for (final BigInteger seed : seedValues) {
            final BigInteger location = analyzeSeed(seed);
            locations.add(location);
            //   System.out.println("---------------");
        }

        final BigInteger minValue = locations.stream()
                .min(BigInteger::compareTo)
                .orElse(BigInteger.ZERO);
        System.out.println("Part 1 Location Minimum: " + minValue.toString());

        // Part 2

        final BigInteger minimum = null;

        final List<Tuple> tupleList = new ArrayList<>();

        for (int i = 0; i < seedValues.size(); i = i + 2) {
            final BigInteger start = seedValues.get(i);
            final BigInteger range = seedValues.get(i + 1);

            tupleList.add(new Tuple(start, range));

            //            BigInteger tmpMinimum = analyseNumbersInRange(start, range);
            //            System.out.println("Minimum: "+tmpMinimum.toString()+" (Range: "+start+", "+range+")");
            //
            //            if (minimum == null) {
            //                minimum = tmpMinimum;
            //            } else {
            //                minimum = minimum.min(tmpMinimum);
            //            }
        }

        final List<BigInteger> resultList = tupleList.parallelStream()
                .map(tuple -> analyseNumbersInRange(tuple.start(), tuple.range()))
                .toList();

        final BigInteger minimum2 = Collections.min(resultList);

        System.out.println("Part 2 Location Minimum: " + minimum2.toString());
    }

    private static BigInteger analyzeSeed(
            final BigInteger seed
    ) {
        //        System.out.println("Seed: " + seed);
        final BigInteger soil = handleMap(seedToSoilMap, seed);
        //        System.out.println("Soil: " + soil);
        final BigInteger fertilizer = handleMap(soilToFertilizerMap, soil);
        //        System.out.println("Fertilizer: " + fertilizer);
        final BigInteger water = handleMap(fertilizerToWaterMap, fertilizer);
        //        System.out.println("Water: " + water);
        final BigInteger light = handleMap(waterToLightMap, water);
        //        System.out.println("Light: " + light);
        final BigInteger temperature = handleMap(lightToTemperatureMap, light);
        //        System.out.println("Temperature: " + temperature);
        final BigInteger humidity = handleMap(temperatureToHumidityMap, temperature);
        //        System.out.println("Humidity: " + humidity);
        final BigInteger location = handleMap(humidityToLocationMap, humidity);
        //        System.out.println("Location: " + location);
        return location;
    }


    private static BigInteger analyseNumbersInRange(
            final BigInteger start,
            final BigInteger range
    ) {
        BigInteger current = start;
        final BigInteger end = start.add(range).subtract(BigInteger.ONE);

        BigInteger minimum = null;

        while (current.compareTo(end) <= 0) {

            final BigInteger location = analyzeSeed(current);

            if (minimum == null) {
                minimum = location;
            } else {
                minimum = minimum.min(location);
            }

            current = current.add(BigInteger.ONE);
        }

        System.out.println("Minimum: " + minimum + " (Range: " + start + ", " + range + ")");
        return minimum;

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


    private static BigInteger handleMap(
            final List<MappingData> map,
            final BigInteger input
    ) {
        for (final MappingData mappingData : map) {
            final BigInteger sourceRangeStart = mappingData.sourceRangeStart();
            final BigInteger range = mappingData.range();
            final BigInteger sourceRangeEnd = sourceRangeStart.add(range);
            if (input.compareTo(sourceRangeStart) >= 0 && input.compareTo(sourceRangeEnd) < 0) {
                //                System.out.println("Input: "+input);
                //                System.out.println("Source Range start: "+sourceRangeStart);
                //                System.out.println("Source Range end: "+sourceRangeEnd);
                //                System.out.println("Range: "+range);
                return input.add(mappingData.destinationRangeStart().subtract(mappingData.sourceRangeStart()));
            }
        }

        return input;
    }

    private static void printMap(final List<MappingData> map) {
        for (final MappingData data : map) {
            System.out.println("Destination Range Start: " + data.destinationRangeStart +
                    ", Source Range Start: " + data.sourceRangeStart +
                    ", Range: " + data.range);
        }
    }

    public record Tuple(BigInteger start, BigInteger range) {

    }

    public record MappingData(BigInteger destinationRangeStart, BigInteger sourceRangeStart, BigInteger range) {

    }


}
