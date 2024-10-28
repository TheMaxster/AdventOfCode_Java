package year2023.day19;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import application.DaySolution;

public class SolutionPart01 extends DaySolution {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String calculate(final List<String> input) {
        final List<Part> partsList = new ArrayList<>();
        final Map<String, List<Rule>> workflows = new HashMap<>();

        boolean skipToParts = false;
        for (final String line : input) {
            if (line.isBlank()) {
                skipToParts = true;
                continue;
            }

            if (!skipToParts) {
                final String[] split = line.split("\\{");
                final String workflow = split[0];
                final String[] rules = split[1].substring(0, split[1].length() - 1).split(",");
                workflows.put(workflow, createRulesList(rules));
            } else {
                final String[] part = line.substring(1, line.length() - 1).split(",");
                partsList.add(createPart(part));
            }
        }

        for (final Map.Entry<String, List<Rule>> entry : workflows.entrySet()) {
            log(entry.getKey() + " " + entry.getValue().toString());
        }

        final Set<Part> acceptedParts = new HashSet<>();

        for (final Part part : partsList) {
            List<Rule> rules = workflows.get("in");

            while (!rules.isEmpty()) {
                for (final Rule rule : rules) {
                    if (!Objects.equals(rule.letter, "else")) {
                        final int letter = getLetter(rule, part);
                        final boolean result = calculateResult(rule, letter);

                        if (result) {
                            rules = getNewRules(rule, part, acceptedParts, workflows);
                            break;
                        }
                    } else {
                        rules = getNewRules(rule, part, acceptedParts, workflows);
                        break;
                    }
                }
            }

        }

        final int totalSum = acceptedParts.stream().mapToInt(Part::getPartSum).sum();
        log("Total sum: " + totalSum);
        return String.valueOf(totalSum);
    }

    private static List<Rule> createRulesList(final String[] rules) {
        final List<Rule> rulesList = new ArrayList<>();
        for (final String rule : rules) {
            if (rule.contains(":")) {
                final String letter = rule.substring(0, 1);
                final String operator = rule.substring(1, 2);
                final int value = Integer.parseInt(rule.substring(2).split(":")[0]);
                final String consequence = rule.substring(2).split(":")[1];
                rulesList.add(new Rule(letter, operator, value, consequence));
            } else {
                rulesList.add(new Rule("else", null, 0, rule));
            }
        }
        return rulesList;
    }

    private static Part createPart(final String[] part) {
        int x = 0;
        int m = 0;
        int a = 0;
        int s = 0;

        for (final String partString : part) {
            final String[] equation = partString.split("=");
            switch (equation[0]) {
                case "x" -> x = Integer.parseInt(equation[1]);
                case "m" -> m = Integer.parseInt(equation[1]);
                case "a" -> a = Integer.parseInt(equation[1]);
                case "s" -> s = Integer.parseInt(equation[1]);
                default -> throw new IllegalStateException("Unexpected value: " + equation[0]);
            }
        }

        return new Part(x, m, a, s);
    }

    private static int getLetter(
            final Rule rule,
            final Part part
    ) {
        return switch (rule.letter()) {
            case "x" -> part.x();
            case "m" -> part.m();
            case "a" -> part.a();
            case "s" -> part.s();
            default -> throw new IllegalStateException("Unexpected value: " + rule.letter());
        };
    }

    private static boolean calculateResult(
            final Rule rule,
            final int letter
    ) {
        return switch (rule.operator()) {
            case "<" -> letter < rule.value;
            case ">" -> letter > rule.value;
            default -> throw new IllegalStateException("Unexpected value: " + rule.operator());
        };
    }

    private List<Rule> getNewRules(
            final Rule rule,
            final Part part,
            final Set<Part> acceptedParts,
            final Map<String, List<Rule>> workflows
    ) {
        log(part + " -> " + rule.consequence);
        if (Objects.equals(rule.consequence, "A")) {
            acceptedParts.add(part);
            return Collections.emptyList();
        } else if (Objects.equals(rule.consequence, "R")) {
            return Collections.emptyList();
        } else {
            return workflows.get(rule.consequence);
        }
    }

    private record Rule(String letter, String operator, int value, String consequence) {

    }

    private record Part(int x, int m, int a, int s) {

        public int getPartSum() {
            return x + m + a + s;
        }

    }
}
