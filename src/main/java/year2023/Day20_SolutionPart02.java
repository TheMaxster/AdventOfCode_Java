package year2023;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import application.DaySolution;
import lombok.Data;

public class Day20_SolutionPart02 extends DaySolution {

    @Override
    public Boolean getLoggingEnabled() {
        return false;
    }

    @Override
    public String calculate(final List<String> input) {
        final List<Module> modules = new ArrayList<>();

        for (final String configString : input) {
            final Module module = new Module();
            final String[] split = configString.split(" -> ");
            module.setDest(Arrays.stream(split[1].split(",")).map(String::trim).toList());

            if (configString.startsWith("broadcaster")) {
                module.setUid(split[0].trim());
                module.setType(Direction.BROADCAST);
            } else if (configString.startsWith("%")) {
                module.setUid(split[0].trim().substring(1));
                module.setType(Direction.FLIP_FLOP);
                module.setState(State.OFF);
            } else if (configString.startsWith("&")) {
                module.setUid(split[0].trim().substring(1));
                module.setType(Direction.CONJUNCTION);
                module.setMemory(new HashMap<>());
            }
            modules.add(module);
        }

        // Define the first void module.
        final Module outputModule = new Module();
        outputModule.setUid("output");
        outputModule.setType(Direction.OUTPUT);
        outputModule.setDest(new ArrayList<>());
        modules.add(outputModule);

        // Define the second void module.
        final Module outputModule2 = new Module();
        outputModule2.setUid("rx");
        outputModule2.setType(Direction.OUTPUT);
        outputModule2.setDest(new ArrayList<>());
        modules.add(outputModule2);

        // Create a model map.
        final Map<String, Module> map = modules.stream().collect(Collectors.toMap(
                a -> a.getUid(),
                a -> a
        ));

        // Update all conjunction modules.
        for (final Module module : modules) {
            if (module.getMemory() != null) {
                final List<String> allBefore = modules.stream()
                        .filter(a -> a.getDest().contains(module.getUid()))
                        .map(Module::getUid)
                        .toList();
                allBefore.forEach(a -> module.getMemory().put(a, Pulse.LOW));
            }
        }

        long highPulses = 0;
        long lowPulses = 0;

        final Map<String, List<Integer>> lastIncomingWasHigh = new HashMap<>();

        for (int i = 1; i <= 10000; i++) {

            log("----------------------------------------------------");

            final LinkedList<QueueElement> queue = new LinkedList<>();
            queue.push(new QueueElement(null, null, checkAndGet(map, "broadcaster")));
            lowPulses++; // Pushing the button counts as a low pulse.

            while (!queue.isEmpty()) {
                final QueueElement currentElement = queue.poll();
                final Module currentModule = currentElement.currentModule();

                log("send " + currentElement.pulse() + " to " + currentModule.getUid());

                modificationForPart2(currentElement, lastIncomingWasHigh, i);

                switch (currentModule.getType()) {
                    case BROADCAST -> {
                        for (final String dest : currentModule.getDest()) {
                            queue.addLast(new QueueElement(currentModule, Pulse.LOW, checkAndGet(map, dest)));
                            lowPulses++;
                        }
                    }
                    case FLIP_FLOP -> {
                        if (Pulse.LOW.equals(currentElement.pulse)) {
                            if (State.ON.equals(currentModule.getState())) {
                                for (final String dest : currentModule.getDest()) {
                                    //    log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(currentModule, Pulse.LOW, checkAndGet(map, dest)));
                                    lowPulses++;
                                }
                                currentModule.setState(State.OFF);
                            } else if (State.OFF.equals(currentModule.getState())) {
                                for (final String dest : currentModule.getDest()) {
                                    //  log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(currentModule, Pulse.HIGH, checkAndGet(map, dest)));
                                    highPulses++;
                                }
                                currentModule.setState(State.ON);
                            }
                        }
                    }
                    case CONJUNCTION -> {
                        currentModule.getMemory().put(currentElement.lastModule().getUid(), currentElement.pulse());

                        final boolean remembersHighPulsesForAllInputs = !currentModule.getMemory().containsValue(Pulse.LOW);

                        for (final String dest : currentModule.getDest()) {
                            // log("we add ("+dest+")");
                            if (remembersHighPulsesForAllInputs) {
                                queue.addLast(new QueueElement(currentModule, Pulse.LOW, checkAndGet(map, dest)));
                                lowPulses++;
                            } else {
                                queue.addLast(new QueueElement(currentModule, Pulse.HIGH, checkAndGet(map, dest)));
                                highPulses++;
                            }
                        }

                    }
                    case OUTPUT -> {
                        // Nothing
                    }
                }
            }

        }

        log("Low Pulses: " + lowPulses);
        log("High Pulses: " + highPulses);
        log("Solution for Part 1: Product: " + lowPulses * highPulses);
        log(" ");
        log("Solution for Part 2: " + lastIncomingWasHigh.toString());
        final long leastCommonMultiple = lastIncomingWasHigh.values().stream().mapToLong(list -> list.get(0)).reduce((a, b) -> a * b)
                .orElseGet(() -> 0L);
        log(
                "Comment for Part 2: The solution has to be a least common multiple of all the first inputs from the HashSet. In our case: "
                        + leastCommonMultiple);
        return String.valueOf(leastCommonMultiple);

    }

    /**
     * The solution has ot be a lest common multiple of all the first inputs from the HashSet. You can have a look here
     * https://www.youtube.com/watch?v=qjgfJuCRUCU to see why it has to be this way. However, it is not that satisfying :(
     *
     * @param currentElement
     *         the current element
     * @param lastIncomingWasHigh
     *         the map that counts the last high incoming pulses
     * @param i
     *         the i
     */
    private static void modificationForPart2(
            final QueueElement currentElement,
            final Map<String, List<Integer>> lastIncomingWasHigh,
            final int i
    ) {
        final Module currentModule = currentElement.currentModule;
        if (Pulse.HIGH.equals(currentElement.pulse) && Objects.equals(currentModule.uid, "qb")) {
            if (lastIncomingWasHigh.containsKey(currentElement.lastModule.uid)) {
                lastIncomingWasHigh.get(currentElement.lastModule.uid).add(i);
            } else {
                final List<Integer> count = new ArrayList<>();
                count.add(i);
                lastIncomingWasHigh.put(currentElement.lastModule.uid, count);
            }
        }
    }

    private static Module checkAndGet(
            final Map<String, Module> map,
            final String dest
    ) {
        if (map.containsKey(dest)) {
            return map.get(dest);
        }
        throw new IllegalStateException("Cannot find " + dest);
    }

    private enum Pulse {LOW, HIGH}

    private enum State {ON, OFF}

    private enum Direction {
        FLIP_FLOP,
        CONJUNCTION,
        BROADCAST,
        OUTPUT
    }

    @Data
    private static class Module {

        String uid;
        List<String> dest;
        Direction type;
        State state; // on and off
        Map<String, Pulse> memory;
    }

    record QueueElement(Module lastModule, Pulse pulse, Module currentModule) {

    }

}
