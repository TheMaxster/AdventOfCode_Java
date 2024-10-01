package days.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import utils.ImportUtils;
import utils.Utils;

public class Part2Draft {

    private record Rule(String letter, String operator, int value, String consequence) {

    }

    private record Part(int x, int m, int a, int s) {

    }

    public static void main(final String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day19/input_test_01.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day19/input.txt";

        final List<String> importData = ImportUtils.readAsList(filePath);

        final Map<String, List<Rule>> workflows = new HashMap<>();

        boolean skipToParts = false;
        for (final String line : importData) {
            if (line.isBlank()) {
                skipToParts = true;
                continue;
            }

            if (!skipToParts) {
                final String[] split = line.split("\\{");
                final String workflow = split[0];
                final String[] rules = split[1].substring(0, split[1].length() - 1).split(",");
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
                workflows.put(workflow, rulesList);
            }
        }

        long accepted = 0;

        for (int x = 1; x < 4000; x++) {
            for (int m = 1; m < 4000; m++) {
                for (int a = 1; a < 4000; a++) {
                    for (int s = 1; s < 4000; s++) {

                        List<Rule> rules = workflows.get("in");

                        while (rules != null) {
                            for (final Rule rule : rules) {
                                if (!Objects.equals(rule.letter, "else")) {
                                    final int letter = switch (rule.letter()) {
                                        case "x" -> x;
                                        case "m" -> m;
                                        case "a" -> a;
                                        case "s" -> s;
                                        default -> throw new IllegalStateException("Unexpected value: " + rule.letter());
                                    };

                                    final boolean result = switch (rule.operator()) {
                                        case "<" -> letter < rule.value;
                                        case ">" -> letter > rule.value;
                                        default -> throw new IllegalStateException("Unexpected value: " + rule.operator());
                                    };

                                    if (result) {

                                        if (Objects.equals(rule.consequence, "A")) {
                                            accepted++;
                                            rules = null;
                                            break;
                                        }
                                        if (Objects.equals(rule.consequence, "R")) {
                                            rules = null;
                                            break;
                                        }

                                        rules = workflows.get(rule.consequence);
                                        break;
                                    }
                                } else {

                                    if (Objects.equals(rule.consequence, "A")) {
                                        accepted++;
                                        rules = null;
                                        break;
                                    }
                                    if (Objects.equals(rule.consequence, "R")) {
                                        rules = null;
                                        break;
                                    }

                                    rules = workflows.get(rule.consequence);
                                    break;
                                }
                            }
                        }
                    }
                }
                Utils.log("m: " + m);
            }

            Utils.log("x: " + x);
        }

        Utils.log("All accepted: " + accepted);
    }

    private record State(String workflowKey, Part part) {

    }
}
