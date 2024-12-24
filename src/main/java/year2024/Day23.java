package year2024;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import application.Day;
import utils.ImportUtils;

/**
 * See https://adventofcode.com/2024/day/23
 */
public class Day23 extends Day {

    private static final String FILE_PATH = "src/main/resources/year2024/day23/input_test_01.txt";

    @Override
    public String part1(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);

        final Map<String, Set<String>> graph = buildGraph(input);

        // Find all triangles
        final Set<Set<String>> triangles = findTriangles(graph);

        // Filter triangles containing at least one computer starting with 't'
        final long count = triangles.stream()
                .filter(triangle -> triangle.stream().anyMatch(node -> node.startsWith("t")))
                .count();

        return String.valueOf(count);
    }

    @Override
    public String part2(final List<String> input) {
        final List<String> importInput = ImportUtils.readAsList(FILE_PATH);
        final Map<String, Set<String>> graph = buildGraph(input);

        final Set<String> largestClique = findLargestClique(graph);
        final String password = largestClique.stream()
                .sorted()
                .collect(Collectors.joining(","));

        return password;
    }


    // Find the largest clique using the Bron-Kerbosch algorithm
    private static Set<String> findLargestClique(final Map<String, Set<String>> graph) {
        final Set<String> largestClique = new HashSet<>();
        bronKerbosch(new HashSet<>(), graph.keySet(), new HashSet<>(), graph, largestClique);
        return largestClique;
    }

    private static void bronKerbosch(
            final Set<String> r,
            final Set<String> p,
            final Set<String> x,
            final Map<String, Set<String>> graph,
            final Set<String> largestClique
    ) {
        if (p.isEmpty() && x.isEmpty()) {
            // Maximal clique found
            if (r.size() > largestClique.size()) {
                largestClique.clear();
                largestClique.addAll(r);
            }
            return;
        }

        // Iterate over a copy to prevent modification errors
        final Set<String> pCopy = new HashSet<>(p);
        for (final String v : pCopy) {
            final Set<String> neighbors = graph.getOrDefault(v, Collections.emptySet());
            bronKerbosch(
                    union(r, v),
                    intersection(p, neighbors),
                    intersection(x, neighbors),
                    graph,
                    largestClique
            );
            p.remove(v);
            x.add(v);
        }
    }

    private static Set<String> union(
            final Set<String> set,
            final String element
    ) {
        final Set<String> result = new HashSet<>(set);
        result.add(element);
        return result;
    }

    private static Set<String> intersection(
            final Set<String> set1,
            final Set<String> set2
    ) {
        final Set<String> result = new HashSet<>(set1);
        result.retainAll(set2);
        return result;
    }

    private static Set<Set<String>> findTriangles(final Map<String, Set<String>> graph) {
        final Set<Set<String>> triangles = new HashSet<>();

        // Iterate over all nodes in the graph
        for (final String node : graph.keySet()) {
            final Set<String> neighbors = graph.get(node);
            for (final String neighbor1 : neighbors) {
                if (neighbor1.compareTo(node) > 0) { // Avoid duplicates by ordering
                    for (final String neighbor2 : neighbors) {
                        if (neighbor2.compareTo(neighbor1) > 0 && graph.get(neighbor1).contains(neighbor2)) {
                            // Form a triangle
                            triangles.add(new TreeSet<>(Arrays.asList(node, neighbor1, neighbor2)));
                        }
                    }
                }
            }
        }

        return triangles;
    }

    private static Map<String, Set<String>> buildGraph(final List<String> connections) {
        final Map<String, Set<String>> graph = new HashMap<>();
        for (final String connection : connections) {
            final String[] nodes = connection.split("-");
            graph.computeIfAbsent(nodes[0], k -> new HashSet<>()).add(nodes[1]);
            graph.computeIfAbsent(nodes[1], k -> new HashSet<>()).add(nodes[0]);
        }
        return graph;
    }

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }


}
