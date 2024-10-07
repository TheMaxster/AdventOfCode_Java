package year2023.day07;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/7
 */
public class Day07 extends Day {

    private static final Map<String, Integer> VALUE_MAP = initializeMap();

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {

        final List<HandBidPair> handBidPairs = new ArrayList<>();
        for (final String handAndBid : input) {
            final String[] handBidSplit = handAndBid.split(" ");

            final String hand = handBidSplit[0];
            final Integer bid = Integer.valueOf(handBidSplit[1]);

            handBidPairs.add(new HandBidPair(hand, bid));
        }

        final List<HandBidPair> fiveOfAKindList = new ArrayList<>();
        final List<HandBidPair> fourOfAKindList = new ArrayList<>();
        final List<HandBidPair> fullHouseList = new ArrayList<>();
        final List<HandBidPair> threeOfAKindList = new ArrayList<>();
        final List<HandBidPair> twoPairList = new ArrayList<>();
        final List<HandBidPair> onePairList = new ArrayList<>();
        final List<HandBidPair> highCardList = new ArrayList<>();

        for (final HandBidPair handBidPair : handBidPairs) {

            final String hand = handBidPair.hand();

            final List<LetterFrequency> letterFrequencies = calculateLetterFrequencies(hand);

            // Part 2: Modify the hand to use J as Joker.
            //            final Optional<LetterFrequency> maxLetterOpt = letterFrequencies.stream().filter(l -> !Objects.equals(l.letter(), "J"))
            //                    .findFirst();
            //            if (maxLetterOpt.isPresent()) {
            //                final String modifiedHand = hand.replaceAll("J", maxLetterOpt.get().letter());
            //                letterFrequencies = calculateLetterFrequencies(modifiedHand);
            //            }

            if (checkFiveOfAKind(letterFrequencies)) {
                fiveOfAKindList.add(handBidPair);
            } else if (checkFourOfAKind(letterFrequencies)) {
                fourOfAKindList.add(handBidPair);
            } else if (checkFullHouse(letterFrequencies)) {
                fullHouseList.add(handBidPair);
            } else if (checkThreeOfAKind(letterFrequencies)) {
                threeOfAKindList.add(handBidPair);
            } else if (checkTwoPair(letterFrequencies)) {
                twoPairList.add(handBidPair);
            } else if (checkOnePair(letterFrequencies)) {
                onePairList.add(handBidPair);
            } else {
                highCardList.add(handBidPair);
            }
        }

        final FirstCardComparator comparator = new FirstCardComparator();

        fiveOfAKindList.sort(comparator);
        fourOfAKindList.sort(comparator);
        fullHouseList.sort(comparator);
        threeOfAKindList.sort(comparator);
        twoPairList.sort(comparator);
        onePairList.sort(comparator);
        highCardList.sort(comparator);

        final List<HandBidPair> theBigList = new ArrayList<>();
        theBigList.addAll(highCardList);
        theBigList.addAll(onePairList);
        theBigList.addAll(twoPairList);
        theBigList.addAll(threeOfAKindList);
        theBigList.addAll(fullHouseList);
        theBigList.addAll(fourOfAKindList);
        theBigList.addAll(fiveOfAKindList);

        int sum = 0;

        for (int i = theBigList.size() - 1; i >= 0; i--) {
            log(theBigList.get(i).hand() + " -> " + theBigList.get(i).bid().toString());

            final HandBidPair pair = theBigList.get(i);
            final int summand = (i + 1) * pair.bid();
            sum += summand;

        }

        return String.valueOf(sum);
    }

    @Override
    public String part2(final List<String> input) {
        return "";
    }

    private static List<LetterFrequency> calculateLetterFrequencies(final String hand) {

        final Map<String, Integer> frequencyMap = new HashMap<>();
        for (final String s : hand.split("")) {
            frequencyMap.put(s, frequencyMap.getOrDefault(s, 0) + 1);
        }

        final List<LetterFrequency> letterFrequencies = new ArrayList<>();

        for (final Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            final int frequency = entry.getValue();
            final String letter = entry.getKey();

            letterFrequencies.add(new LetterFrequency(letter, frequency));
        }

        letterFrequencies.sort(Comparator.comparingInt(LetterFrequency::frequency).reversed());

        return letterFrequencies;
    }

    private static boolean checkFiveOfAKind(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 5;
    }

    private static boolean checkFourOfAKind(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 4;
    }

    private static boolean checkFullHouse(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 3
                && letterFrequencies.get(1).frequency() == 2;
    }

    private static boolean checkThreeOfAKind(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 3;
    }

    private static boolean checkTwoPair(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 2
                && letterFrequencies.get(1).frequency() == 2;
    }

    private static boolean checkOnePair(final List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 2
                && letterFrequencies.get(1).frequency() != 2;
    }

    private static Map<String, Integer> initializeMap() {

        final Map<String, Integer> valueMap = new HashMap<>();
        valueMap.put("J", 1);
        valueMap.put("2", 2);
        valueMap.put("3", 3);
        valueMap.put("4", 4);
        valueMap.put("5", 5);
        valueMap.put("6", 6);
        valueMap.put("7", 7);
        valueMap.put("8", 8);
        valueMap.put("9", 9);
        valueMap.put("T", 10);
        // valueMap.put("J", 11);
        valueMap.put("Q", 12);
        valueMap.put("K", 13);
        valueMap.put("A", 14);

        return valueMap;
    }

    private record HandBidPair(String hand, Integer bid) {

    }

    private record LetterFrequency(String letter, int frequency) {

    }

    /**
     * The comparator for the comparison of the hands.
     */
    private static class FirstCardComparator implements Comparator<HandBidPair> {

        @Override
        public int compare(
                final HandBidPair o1,
                final HandBidPair o2
        ) {
            final String[] splittedHand1 = o1.hand().split("");
            final String[] splittedHand2 = o2.hand().split("");

            return compare(splittedHand1, splittedHand2);
        }

        private static int compare(
                final String[] splittedHand1,
                final String[] splittedHand2
        ) {
            for (int i = 0; i < splittedHand1.length; i++) {
                if (VALUE_MAP.get(splittedHand1[i]) > VALUE_MAP.get(splittedHand2[i])) {
                    return 1;
                } else if (VALUE_MAP.get(splittedHand1[i]) < VALUE_MAP.get(splittedHand2[i])) {
                    return -1;
                }
            }
            return 0;
        }
    }


}
