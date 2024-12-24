package year2024.day24;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import application.Day;
import lombok.AllArgsConstructor;
import lombok.Data;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/24
 */
public class Day24 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day24/input_test_01.txt";

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Map<String, Integer> initialWireValues = new HashMap<>();
        final List<String> gates = new ArrayList<>();

        parseInput(initialWireValues, gates, input);

        final Map<String, Integer> wireValues = simulateSystem(initialWireValues, gates);

        final String binary = wireValues.keySet().stream()
                .filter(wire -> wire.startsWith("z"))
                .sorted(Comparator.reverseOrder())
                .map(wireValues::get)
                .map(String::valueOf)
                .collect(Collectors.joining());

        log(binary);
        return String.valueOf(Long.parseLong(binary, 2));
    }

    private void parseInput(
            final Map<String, Integer> initialWireValues,
            final List<String> gates,
            final List<String> input
    ) {
        boolean parsingInitialValues = true;

        for (final String line : input) {
            if (line.isEmpty()) {
                parsingInitialValues = false;
            } else if (parsingInitialValues) {
                final String[] parts = line.split(":");
                initialWireValues.put(parts[0], Integer.parseInt(parts[1].trim()));
            } else {
                gates.add(line);
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
        final Map<String, Gate> gateDefinitions = new HashMap<>();

        // Parse gates
        for (final String gate : gates) {
            final String[] parts = gate.split(" -> ");
            final String output = parts[1];
            final String[] operation = parts[0].split(" ");
            final String operand1 = operation[0];
            final Operation operator = Operation.valueOf(operation[1]);
            final String operand2 = operation[2];
            final Gate newGate = new Gate(operand1, operand2, operator);
            gateDefinitions.put(output, newGate);
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
            final Map<String, Gate> gateDefinitions
    ) {
        // If already resolved
        if (wireValues.containsKey(wire)) {
            return wireValues.get(wire);
        }

        final Gate operation = gateDefinitions.get(wire);
        final String operand1 = operation.a;
        final Operation operator = operation.op;
        final String operand2 = operation.b;

        final int value1 = resolveWire(operand1, wireValues, gateDefinitions);
        final int value2 = resolveWire(operand2, wireValues, gateDefinitions);

        final int result = switch (operator) {
            case AND -> value1 & value2;
            case OR -> value1 | value2;
            case XOR -> value1 ^ value2;
        };

        wireValues.put(wire, result);
        return result;
    }

    private enum Operation {AND, OR, XOR}

    @Data
    @AllArgsConstructor
    private class Gate {

        public String a, b;
        public Operation op;
    }


}
