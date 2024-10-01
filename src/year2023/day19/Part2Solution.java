package days.day19;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Range;

import utils.ImportUtils;
import utils.Regex;
import utils.Utils;

public class Part2Solution {

    private record Rule(String letter, String operator, int value, String consequence) {

    }

    private record Part(int x, int m, int a, int s) {

    }

    public static void main(String[] args) {
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day19/input_19_test_01.txt";
         final String filePath = System.getProperty("user.dir") + "/resources/days/day19/input_19.txt";


        String input = ImportUtils.readAsPlainString(filePath);
      String result =   part2Solution(input);

      Utils.log(result);
    }

    public static String part2Solution(String input) {
        String[] inputParts = input.trim().split("\n\n");
        long sum = 0L;
        HashMap<String, Workflow> workflows = parseWorkflows(inputParts[0]);
        HashMap<String, ArrayList<HashMap<Character, Range<Integer>>>> workflowQueue = new HashMap<>();
        ArrayList<HashMap<Character, Range<Integer>>> acceptedRanges = new ArrayList<>();
        boolean hasUnprocessedRanges = true;

        for (Workflow workflow: workflows.values()) {
            workflowQueue.put(workflow.name, new ArrayList<>());
        }

        List<HashMap<Character, Range<Integer>>> partRanges = new ArrayList<>() {{
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

            for (Map.Entry<String, ArrayList<HashMap<Character, Range<Integer>>>> work: workflowQueue.entrySet()) {
                if (work.getValue().isEmpty()) {
                    continue;
                }

                hasUnprocessedRanges = true;

                Workflow workflow = workflows.get(work.getKey());
                ArrayList<HashMap<Character, Range<Integer>>> ranges = work.getValue();
                workflowQueue.put(work.getKey(), new ArrayList<>());

                nextRange:
                for (HashMap<Character, Range<Integer>> range: ranges) {
                    for (Condition condition: workflow.conditions) {
                        Range<Integer> registerRange = range.get(condition.register);
                        Range<Integer> conditionRange;
                        Range<Integer> remainingRange;

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

                        Range<Integer> overlapping = registerRange.intersection(conditionRange);
                        HashMap<Character, Range<Integer>> newRange = new HashMap<>(range);
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

        for (HashMap<Character, Range<Integer>> acceptedRange: acceptedRanges) {
            long rangeProduct = 1L;
            for (Map.Entry<Character, Range<Integer>> es: acceptedRange.entrySet()) {
                Range<Integer> range = es.getValue();
                rangeProduct *= range.upperEndpoint() - range.lowerEndpoint() + 1;
            }

            sum += rangeProduct;
        }

        return String.valueOf(sum);
    }

    private static HashMap<String, Workflow> parseWorkflows(String input) {
        HashMap<String, Workflow> workflows = new HashMap<>();
        String[] workflowsLines = input.split("\n");

        for (String workflowLine: workflowsLines) {
            LinkedList<Condition> conditions = new LinkedList<>();
            List<String> workflowHeaders = Regex.matchGroups("(\\w+)\\{(.*)\\}", workflowLine);
            assert workflowHeaders != null;
            String[] workflowParts = workflowHeaders.get(1).split(",");

            for (int i = 0; i < workflowParts.length - 1; i++) {
                List<String> c = Regex.matchGroups("(\\w+)([^\\w])([0-9]+):(\\w+)", workflowParts[i]);
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

    record Condition(Character register, Character operation, Integer value, String targetWorkflow) {}
    record Workflow(String name, List<Condition> conditions, String targetWorkflow) {}
}