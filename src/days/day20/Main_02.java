package days.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class Main_02 {

    public static void main(String[] args) {
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_01.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_02.txt";
        final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20.txt";

        List<String> input = ImportUtils.readAsList(filePath);

        List<Module> modules = new ArrayList<>();

        for (String configString : input) {
            Module module = new Module();
            String[] split = configString.split(" -> ");
            module.setUid(split[0].trim());
            module.setDest(Arrays.stream(split[1].split(",")).map(String::trim).toList());

            if (configString.startsWith("broadcaster")) {
                module.setType(Direction.BROADCAST);
            } else if (configString.startsWith("%")) {
                module.setType(Direction.FLIP_FLOP);
                module.setState(State.OFF);
            } else if (configString.startsWith("&")) {
                module.setType(Direction.CONJUNCTION);
                module.setMemory(new HashMap<>());
            }
            modules.add(module);
        }

        // Define the first void module.
        Module outputModule = new Module();
        outputModule.setUid("output");
        outputModule.setType(Direction.OUTPUT);
        outputModule.setDest(new ArrayList<>());
        modules.add(outputModule);

        // Define the second void module.
        Module outputModule2 = new Module();
        outputModule2.setUid("rx");
        outputModule2.setType(Direction.OUTPUT);
        outputModule2.setDest(new ArrayList<>());
        modules.add(outputModule2);

        // Create a model map.
        Map<String, Module> map = modules.stream().collect(Collectors.toMap(
                a -> a.getUid(),
                a -> a
        ));

        // Update all conjunction modules.
        for (Module module : modules) {
            if (module.getMemory() != null) {
                List<String> allBefore = modules.stream()
                        .filter(a -> a.getDest().contains(module.getUid()))
                        .map(Module::getUid)
                        .toList();
                allBefore.forEach(a -> module.getMemory().put(a, Pulse.LOW));
            }
        }

        record QueueElement(Module last, Pulse pulse, Module current) {

        }

        long highPulses = 0;
        long lowPulses = 0;

        for (int i = 0; i < 1000; i++) {

            Utils.log("----------------------------------------------------");

            LinkedList<QueueElement> queue = new LinkedList<>();
            queue.push(new QueueElement(null, null, map.get("broadcaster")));
            lowPulses++; // Pushing the button counts as a low pulse.

            while (!queue.isEmpty()) {
                QueueElement currentElement = queue.poll();
                Module current = currentElement.current();

                Utils.log("send " + currentElement.pulse() + " to " + current.getUid());

                switch (current.getType()) {
                    case BROADCAST -> {
                        for (String dest : current.getDest()) {
                            queue.addLast(new QueueElement(current, Pulse.LOW, checkAndGet(map, dest)));
                            lowPulses++;
                        }
                    }
                    case FLIP_FLOP -> {
                        if (Pulse.LOW.equals(currentElement.pulse)) {
                            if (State.ON.equals(current.getState())) {
                                for (String dest : current.getDest()) {
                                    //    Utils.log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(current, Pulse.LOW, checkAndGet(map, dest)));
                                    lowPulses++;
                                }
                                current.setState(State.OFF);
                            } else if (State.OFF.equals(current.getState())) {
                                for (String dest : current.getDest()) {
                                    //  Utils.log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(current, Pulse.HIGH, checkAndGet(map, dest)));
                                    highPulses++;
                                }
                                current.setState(State.ON);
                            }
                        }
                    }
                    case CONJUNCTION -> {
                        current.getMemory().put(currentElement.last().getUid(), currentElement.pulse());

                        boolean remembersHighPulsesForAllInputs = !current.getMemory().containsValue(Pulse.LOW);

                        for (String dest : current.getDest()) {
                            // Utils.log("we add ("+dest+")");
                            if (remembersHighPulsesForAllInputs) {
                                queue.addLast(new QueueElement(current, Pulse.LOW, checkAndGet(map, dest)));
                                lowPulses++;
                            } else {
                                queue.addLast(new QueueElement(current, Pulse.HIGH, checkAndGet(map, dest)));
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

        Utils.log("Low Pulses: " + lowPulses);
        Utils.log("High Pulses: " + highPulses);
        Utils.log("Product: " + lowPulses * highPulses);

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

    private enum Pulse{LOW, HIGH}

    private enum State{ON, OFF}

    private enum Direction {
        FLIP_FLOP,
        CONJUNCTION,
        BROADCAST,
        OUTPUT;
    }

    private static class Module {

        String uid;

        public String getUid() {
            return uid;
        }

        public void setUid(final String uid) {
            this.uid = uid;
        }

        public List<String> getDest() {
            return dest;
        }

        public void setDest(final List<String> dest) {
            this.dest = dest;
        }

        public Direction getType() {
            return type;
        }

        public void setType(final Direction type) {
            this.type = type;
        }

        public State getState() {
            return state;
        }

        public void setState(final State state) {
            this.state = state;
        }

        public Map<String, Pulse> getMemory() {
            return memory;
        }

        public void setMemory(final Map<String, Pulse> memory) {
            this.memory = memory;
        }

        List<String> dest;
        Direction type;
        State state; // on and off
        Map<String, Pulse> memory;
    }

}