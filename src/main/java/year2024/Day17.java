package year2024;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/1
 */
public class Day17 extends Day {

    // private static final String FILE_PATH = "src/main/resources/year2024/day17/input_test_01.txt";
    private static final String FILE_PATH = "src/main/resources/year2024/day17/input_test_02.txt";

    private static final Pattern PATTERN_REGISTER = Pattern.compile("Register [A-C]: (\\d+)");

    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final List<String> usedInput = input;
        final int A = Integer.parseInt(getValue(PATTERN_REGISTER, usedInput.get(0)).orElse(""));
        final int B = Integer.parseInt(getValue(PATTERN_REGISTER, usedInput.get(1)).orElse(""));
        final int C = Integer.parseInt(getValue(PATTERN_REGISTER, usedInput.get(2)).orElse(""));
        final String ogProgram = usedInput.get(4).split("Program: ")[1];
        final Long[] program = Arrays.stream(ogProgram.split(",")).map(Long::parseLong).toList().toArray(new Long[]{});

        final List<Long> output = runProgram(A, B, C, program);
        return String.join(",", output.stream().map(String::valueOf).toArray(String[]::new));
    }

    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final List<String> usedInput = input;
        final int B = Integer.parseInt(getValue(PATTERN_REGISTER, usedInput.get(1)).orElse(""));
        final int C = Integer.parseInt(getValue(PATTERN_REGISTER, usedInput.get(2)).orElse(""));
        final String ogProgram = usedInput.get(4).split("Program: ")[1];
        final Long[] program = Arrays.stream(ogProgram.split(",")).map(Long::parseLong).toList().toArray(new Long[]{});

        long result = 0;
        List<Long> output;
        // Log the first 10.000 to see a system in the output.
        for (int i = 0; i < 10000; i++) {
            output = runProgram(result, B, C, program);
            result++;
            log(result + ": " + output);
        }

        return String.valueOf(findValueOfA(1, B, C, program));
    }

    private static List<Long> runProgram(
            long A,
            long B,
            long C,
            final Long[] program
    ) {
        long instructionPointer = 0; // Start from the first instruction
        final List<Long> output = new ArrayList<>(); // To collect outputs

        while (instructionPointer < program.length) {
            final long opcode = program[(int) instructionPointer];    // Current instruction
            final long operand = program[(int) (instructionPointer + 1)]; // Operand of the instruction

            switch ((int) opcode) {
                case 0 -> A /= Math.pow(2, getComboValue(operand, A, B, C)); // adv
                case 1 -> B ^= operand; // bxl
                case 2 -> B = getComboValue(operand, A, B, C) % 8; // bst
                case 3 -> { // jnz
                    if (A != 0) {
                        instructionPointer = operand;
                        continue;
                    }
                }
                case 4 -> B ^= C; // bxc
                case 5 -> output.add(getComboValue(operand, A, B, C) % 8); // out
                case 6 -> B = A / (long) Math.pow(2, getComboValue(operand, A, B, C)); // bdv
                case 7 -> C = A / (long) Math.pow(2, getComboValue(operand, A, B, C)); // cdv
                default -> throw new IllegalArgumentException("Unknown opcode: " + opcode);
            }
            instructionPointer += 2;
        }

        // Join the output with commas
        return output;
    }

    // Helper to compute the value of combo operands
    private static long getComboValue(
            final long operand,
            final long A,
            final long B,
            final long C
    ) {
        return switch ((int) operand) {
            case 0, 1, 2, 3 -> operand; // Literal values 0-3
            case 4 -> A; // Register A
            case 5 -> B; // Register B
            case 6 -> C; // Register C
            default -> throw new IllegalArgumentException("Invalid combo operand: " + operand);
        };
    }

    private long findValueOfA(
            long a,
            final long b,
            final long c,
            final Long[] inst
    ) {
        while (true) {
            final List<Long> output = runProgram(a, b, c, inst);

            if (Arrays.equals(output.toArray(), inst)) {
                return a;
            }

            // If our output is too short, we try again with a double "a"
            if (inst.length > output.size()) {
                a *= 2;
                continue;
            }

            // If our output is too long, we try again with a half "a"
            if (inst.length < output.size()) {
                a /= 2;
            }

            // Key Insight: every nth digit increments at every 8^n th step.
            if (inst.length == output.size()) {
                for (int j = inst.length - 1; j >= 0; j--) {
                    // Test out every position in the array.
                    if (!Objects.equals(inst[j], output.get(j))) {
                        a += (long) Math.pow(8, j);
                        break;
                    }
                }
            }
        }

    }

    private Optional<String> getValue(
            final Pattern pattern,
            final String string
    ) {
        final Matcher matcher = pattern.matcher(string);
        if (matcher.find()) {
            return Optional.of(matcher.group(1));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

}

