package year2024.day24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/24
 */
public class Day24 extends Day {


    private static final String FILE_PATH = "src/main/resources/year2024/day24/input_test_01.txt";


    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Map<String, Integer> initialWireValues = new HashMap<>();
        final List<String> gates = new ArrayList<>();

        parseInput(initialWireValues, gates, input);

        final Map<String, Integer> wireValues = simulateSystem(initialWireValues, gates);

        // Compute the binary number from z wires
        final List<String> zWires = wireValues.keySet().stream()
                .filter(wire -> wire.startsWith("z"))
                .sorted(Comparator.reverseOrder())
                .toList();

        final StringBuilder binary = new StringBuilder();
        for (final String zWire : zWires) {
            binary.append(wireValues.get(zWire));
        }

        log(binary.toString());
        final Long int2BaseTwo = Long.parseLong(binary.toString(), 2);
        return String.valueOf(int2BaseTwo);
    }

    private void parseInput(
            final Map<String, Integer> initialWireValues,
            final List<String> gates,
            final List<String> input
    ) {
        boolean firstBlock = true;
        for (final String s : input) {
            if (s.isEmpty()) {
                firstBlock = false;
                continue;
            }
            if (firstBlock) {
                final String[] block = s.split(":");
                final String key = block[0];
                final int value = Integer.parseInt(block[1].trim());
                initialWireValues.put(key, value);
            }
            if (!firstBlock) {
                gates.add(s);
            }
        }

    }

    public String part2(final List<String> input) {
        return "test";
    }

    private Map<String, Integer> simulateSystem(
            final Map<String, Integer> initialWireValues,
            final List<String> gates
    ) {
        final Map<String, Integer> wireValues = new HashMap<>(initialWireValues);
        final Map<String, String[]> gateDefinitions = new HashMap<>();

        // Parse gates
        for (final String gate : gates) {
            final String[] parts = gate.split(" -> ");
            final String output = parts[1];
            gateDefinitions.put(output, parts[0].split(" "));
        }

        // Resolve all wires
        for (final String wire : gateDefinitions.keySet()) {
            resolveWire(wire, wireValues, gateDefinitions);
        }

        return wireValues;
    }

    private static int resolveWire(
            final String wire,
            final Map<String, Integer> wireValues,
            final Map<String, String[]> gateDefinitions
    ) {
        // If already resolved
        if (wireValues.containsKey(wire)) {
            return wireValues.get(wire);
        }

        final String[] definition = gateDefinitions.get(wire);
        final String operand1 = definition[0];
        final String operator = definition.length > 2 ? definition[1] : null;
        final String operand2 = definition.length > 2 ? definition[2] : null;

        final int value1 = operand1.matches("\\d") ? Integer.parseInt(operand1) : resolveWire(operand1, wireValues, gateDefinitions);
        final int value2 = operand2 != null ? (operand2.matches("\\d") ? Integer.parseInt(operand2)
                : resolveWire(operand2, wireValues, gateDefinitions)) : 0;

        final int result;
        switch (operator) {
            case "AND":
                result = value1 & value2;
                break;
            case "OR":
                result = value1 | value2;
                break;
            case "XOR":
                result = value1 ^ value2;
                break;
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }

        wireValues.put(wire, result);
        return result;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }


}
