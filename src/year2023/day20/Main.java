package year2023.day20;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    private static final String LOW = "low";
    private static final String HIGH = "high";

    private static final String ON = "on";
    private static final String OFF = "off";

    private enum Direction {
        FLIP_FLOP,
        CONJUNCTION,
        BROADCAST,
        OUTPUT;
    }

    private static Map<String, Direction> DIRECTION_MAP = Map.of(
            "%", Direction.FLIP_FLOP,
            "&", Direction.CONJUNCTION,
            "broadcast", Direction.BROADCAST
    );

    public static class Module {

        String start;

        public String getStart() {
            return start;
        }

        public void setStart(final String start) {
            this.start = start;
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

        public String getState() {
            return state;
        }

        public void setState(final String state) {
            this.state = state;
        }

        public List<String> getMemory() {
            return memory;
        }

        public void setMemory(final List<String> memory) {
            this.memory = memory;
        }

        List<String> dest;
        Direction type;
        String state; // on and off
        List<String> memory;
    }

    public static void main(String[] args) {
      //  final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_01.txt";
      // final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20_test_02.txt";
      final String filePath = System.getProperty("user.dir") + "/resources/days/day20/input_20.txt";

        List<String> input = ImportUtils.readAsList(filePath);

        List<Module> modules = new ArrayList<>();

        for (String configString : input) {
            Module module = new Module();
            if (configString.startsWith("broadcaster")) {
                String[] split = configString.split(" -> ");

                module.setStart(split[0].trim());
                module.setDest(Arrays.stream(split[1].split(",")).map(String::trim).toList());
                module.setType(Direction.BROADCAST);

            } else if (configString.startsWith("%")) {
                String[] split = configString.substring(1).split(" -> ");

                module.setStart(split[0].trim());
                module.setDest(Arrays.stream(split[1].split(",")).map(String::trim).toList());
                module.setType(Direction.FLIP_FLOP);
                module.setState("off");
            } else if (configString.startsWith("&")) {
                String[] split = configString.substring(1).split(" -> ");

                module.setStart(split[0].trim());
                module.setDest(Arrays.stream(split[1].split(",")).map(String::trim).toList());
                module.setType(Direction.CONJUNCTION);
              //  List<String> memory = new ArrayList<>();
               // memory.add(LOW);
               // module.setMemory(memory);
                module.setMemory(new ArrayList<>());
            }
            modules.add(module);
        }

        Module outputModule = new Module();
        outputModule.setStart("output");
        outputModule.setType(Direction.OUTPUT);
        outputModule.setDest(new ArrayList<>());
        modules.add(outputModule);

        Module outputModule2 = new Module();
        outputModule2.setStart("rx");
        outputModule2.setType(Direction.OUTPUT);
        outputModule2.setDest(new ArrayList<>());
        modules.add(outputModule2);

        Map<String, Module> map = modules.stream().collect(Collectors.toMap(
                a -> a.getStart(),
                a -> a
        ));

        record QueueElement(String pulse, Module module) {

        }

        long highPulses = 0;
        long lowPulses = 0;

        for(int i = 0; i<1000; i++) {

            Utils.log("----------------------------------------------------");

            LinkedList<QueueElement> queue = new LinkedList<>();
            queue.push(new QueueElement(null, map.get("broadcaster")));
            lowPulses++; // Pushing the button counts as a low pulse.

            while (!queue.isEmpty()) {
                QueueElement currentElement = queue.poll();
                Module current = currentElement.module();

                Utils.log("send " + currentElement.pulse() + " to " + current.getStart());

                switch (current.getType()) {
                    case BROADCAST -> {
                        for (String dest : current.getDest()) {
                            queue.addLast(new QueueElement(LOW, checkAndGet(map, dest)));
                            lowPulses++;
                        }
                    }
                    case FLIP_FLOP -> {
                        if (LOW.equals(currentElement.pulse)) {
                            if (ON.equals(current.getState())) {
                                for (String dest : current.getDest()) {
                                    //    Utils.log("we add ("+dest+")");
                                    queue.addLast(new QueueElement(LOW, checkAndGet(map, dest)));
                                    lowPulses++;
                                }
                                current.setState(OFF);
                            } else if (OFF.equals(current.getState())) {
                                for (String dest : current.getDest()) {
                                    //  Utils.log("we add ("+dest+")");
                                        queue.addLast(new QueueElement(HIGH, checkAndGet(map, dest)));
                                        highPulses++;
                                }
                                current.setState(ON);
                            }
                        }
                    }
                    case CONJUNCTION -> {
                        //   current.getMemory().add(currentElement.pulse);
                        //    if ((current.getMemory().size() > 2 && current.getMemory().contains(LOW)) || current.getMemory().isEmpty()) {

                        boolean remembersHighPulsesForAllInputs = modules.stream().filter(a -> a.getDest().contains(current.getStart()))
                                .allMatch(a -> ON.equals(a.getState()));

                        for (String dest : current.getDest()) {
                            // Utils.log("we add ("+dest+")");
                            if(remembersHighPulsesForAllInputs) {
                                queue.addLast(new QueueElement(LOW, checkAndGet(map, dest)));
                                lowPulses++;
                            }else{
                                queue.addLast(new QueueElement(HIGH, checkAndGet(map, dest)));
                                highPulses++;
                            }
                        }
                        //                    } else {
                        //                        for (String dest : current.getDest()) {
                        //                            //     Utils.log("we add ("+dest+")");
                        //                            queue.addLast(new QueueElement(LOW, checkAndGet(map, dest)));
                        //                        }
                        //                    }

                    }
                    case OUTPUT -> {
                        // Nothing
                    }
                }
            }

        }

        Utils.log("Low Pulses: "+lowPulses);
        Utils.log("High Pulses: "+highPulses);
        Utils.log("Product: "+lowPulses*highPulses);

    }

    private static Module checkAndGet(
            final Map<String, Module> map,
            final String dest
    ) {
        if(map.containsKey(dest)){
            return map.get(dest);
        }
        throw new IllegalStateException("Cannot find "+dest);
    }

}
