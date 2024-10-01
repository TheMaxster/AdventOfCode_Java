package year2023.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class SolutionPart01 {

    public static void main(String[] args) {
       // final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_01.txt";
        //  final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_02.txt";
         final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20.txt";

        List<String> input = ImportUtils.readAsList(filePath);

        List<Module> modules = new ArrayList<>();

        for (String configString : input) {
            Module module = new Module();
            String[] split = configString.split(" -> ");
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

        record QueueElement(Module lastModule, Pulse pulse, Module currentModule) {

        }

        long highPulses = 0;
        long lowPulses = 0;

        for (int i = 0; i < 1000; i++) {

            Utils.log("----------------------------------------------------");

            LinkedList<QueueElement> queue = new LinkedList<>();
            queue.push(new QueueElement(null, null, checkAndGet(map, "broadcaster")));
            lowPulses++; // Pushing the button counts as a low pulse.

            while (!queue.isEmpty()) {
                QueueElement currentElement = queue.poll();
                Module currentModule = currentElement.currentModule();

                Utils.log("send " + currentElement.pulse() + " to " + currentModule.getUid());

                switch (currentModule.getType()) {
                    case BROADCAST -> {
                        for (String dest : currentModule.getDest()) {
                            queue.addLast(new QueueElement(currentModule, Pulse.LOW, checkAndGet(map, dest)));
                            lowPulses++;
                        }
                    }
                    case FLIP_FLOP -> {
                        if (Pulse.LOW.equals(currentElement.pulse)) {
                            if (State.ON.equals(currentModule.getState())) {
                                for (String dest : currentModule.getDest()) {
                                    //    Utils.log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(currentModule, Pulse.LOW, checkAndGet(map, dest)));
                                    lowPulses++;
                                }
                                currentModule.setState(State.OFF);
                            } else if (State.OFF.equals(currentModule.getState())) {
                                for (String dest : currentModule.getDest()) {
                                    //  Utils.log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(currentModule, Pulse.HIGH, checkAndGet(map, dest)));
                                    highPulses++;
                                }
                                currentModule.setState(State.ON);
                            }
                        }
                    }
                    case CONJUNCTION -> {
                        currentModule.getMemory().put(currentElement.lastModule().getUid(), currentElement.pulse());

                        boolean remembersHighPulsesForAllInputs = !currentModule.getMemory().containsValue(Pulse.LOW);

                        for (String dest : currentModule.getDest()) {
                            // Utils.log("we add ("+dest+")");
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
