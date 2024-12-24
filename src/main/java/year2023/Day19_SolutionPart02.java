package year2023;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Range;

import application.DaySolution;
import utils.ImportUtils;
import utils.RegexUtils;

public class Day19_SolutionPart02 extends DaySolution {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String calculate(final List<String> input) {
        final String inputString = ImportUtils.convertListToString(input);
        return part2Solution(inputString);
    }

    public static String part2Solution(final String input) {
        final String[] inputParts = input.trim().split("\n\n");
        long sum = 0L;
        final HashMap<String, Workflow> workflows = parseWorkflows(inputParts[0]);
        final HashMap<String, ArrayList<HashMap<Character, Range<Integer>>>> workflowQueue = new HashMap<>();
        final ArrayList<HashMap<Character, Range<Integer>>> acceptedRanges = new ArrayList<>();
        boolean hasUnprocessedRanges = true;

        for (final Workflow workflow : workflows.values()) {
            workflowQueue.put(workflow.name, new ArrayList<>());
        }

        final List<HashMap<Character, Range<Integer>>> partRanges = new ArrayList<>() {{
            add(new HashMap<>() {{
                put('x', Range.closed(1, 4000));
                put('m', Range.closed(1, 4000));
                put('a', Range.closed(1, 4000));
                put('s', Range.closed(1, 4000));
            }});
        }};

        workflowQueue.get("in").addAll(partRanges);

        while (hasUnprocessedRanges) {
            hasUnprocessedRanges = false;

            for (final Map.Entry<String, ArrayList<HashMap<Character, Range<Integer>>>> work : workflowQueue.entrySet()) {
                if (work.getValue().isEmpty()) {
                    continue;
                }

                hasUnprocessedRanges = true;

                final Workflow workflow = workflows.get(work.getKey());
                final ArrayList<HashMap<Character, Range<Integer>>> ranges = work.getValue();
                workflowQueue.put(work.getKey(), new ArrayList<>());

                nextRange:
                for (final HashMap<Character, Range<Integer>> range : ranges) {
                    for (final Condition condition : workflow.conditions) {
                        final Range<Integer> registerRange = range.get(condition.register);
                        final Range<Integer> conditionRange;
                        final Range<Integer> remainingRange;

                        if (condition.operation == '<') {
                            conditionRange = Range.closed(1, condition.value - 1);
                            remainingRange = Range.closed(condition.value, 4000);
                        } else {
                            conditionRange = Range.closed(condition.value + 1, 4000);
                            remainingRange = Range.closed(1, condition.value);
                        }

                        if (!registerRange.isConnected(conditionRange)) {
                            continue;
                        }

                        final Range<Integer> overlapping = registerRange.intersection(conditionRange);
                        final HashMap<Character, Range<Integer>> newRange = new HashMap<>(range);
                        newRange.put(condition.register, overlapping);

                        if (condition.targetWorkflow.equals("A")) {
                            acceptedRanges.add(newRange);
                        } else if (!condition.targetWorkflow.equals("R")) {
                            workflowQueue.get(condition.targetWorkflow).add(newRange);
                        }

                        if (remainingRange.isConnected(registerRange)) {
                            range.put(condition.register, remainingRange.intersection(registerRange));
                        } else {
                            continue nextRange;
                        }
                    }

                    if (workflow.targetWorkflow.equals("A")) {
                        acceptedRanges.add(range);
                    } else if (!workflow.targetWorkflow.equals("R")) {
                        workflowQueue.get(workflow.targetWorkflow).add(range);
                    }
                }
            }
        }

        for (final HashMap<Character, Range<Integer>> acceptedRange : acceptedRanges) {
            long rangeProduct = 1L;
            for (final Map.Entry<Character, Range<Integer>> es : acceptedRange.entrySet()) {
                final Range<Integer> range = es.getValue();
                rangeProduct *= range.upperEndpoint() - range.lowerEndpoint() + 1;
            }

            sum += rangeProduct;
        }

        return String.valueOf(sum);
    }

    private static HashMap<String, Workflow> parseWorkflows(final String input) {
        final HashMap<String, Workflow> workflows = new HashMap<>();
        final String[] workflowsLines = input.split("\n");

        for (final String workflowLine : workflowsLines) {
            final LinkedList<Condition> conditions = new LinkedList<>();
            final List<String> workflowHeaders = RegexUtils.matchGroups("(\\w+)\\{(.*)\\}", workflowLine);
            assert workflowHeaders != null;
            final String[] workflowParts = workflowHeaders.get(1).split(",");

            for (int i = 0; i < workflowParts.length - 1; i++) {
                final List<String> c = RegexUtils.matchGroups("(\\w+)([^\\w])([0-9]+):(\\w+)", workflowParts[i]);
                assert c != null;
                conditions.add(new Condition(c.get(0).charAt(0), c.get(1).charAt(0), Integer.parseInt(c.get(2)), c.get(3)));
            }

            workflows.put(
                    workflowHeaders.get(0),
                    new Workflow(workflowHeaders.get(0), conditions, workflowParts[workflowParts.length - 1])
            );
        }

        return workflows;
    }

    record Condition(Character register, Character operation, Integer value, String targetWorkflow) {

    }

    record Workflow(String name, List<Condition> conditions, String targetWorkflow) {

    }

    private record Rule(String letter, String operator, int value, String consequence) {

    }

    private record Part(int x, int m, int a, int s) {

    }
}
