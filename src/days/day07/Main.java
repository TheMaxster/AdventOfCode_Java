package days.day07;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import utils.ImportUtils;
import utils.Utils;

public class Main {

    private static final Map<String, Integer> VALUE_MAP = initializeMap();

    public static void main(String[] args) {
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day07/input_07_test_01.txt";
       final String filePath = System.getProperty("user.dir") + "/resources/days/day07/input_07.txt";

        List<String> dataList = ImportUtils.readAsList(filePath);

        List<HandBidPair> handBidPairs = new ArrayList<>();
        for (String handAndBid : dataList) {
            String[] handBidSplit = handAndBid.split(" ");

            String hand = handBidSplit[0];
            Integer bid = Integer.valueOf(handBidSplit[1]);

            handBidPairs.add(new HandBidPair(hand, bid));
        }

        List<HandBidPair> fiveOfAKindList = new ArrayList<>();
        List<HandBidPair> fourOfAKindList = new ArrayList<>();
        List<HandBidPair> fullHouseList = new ArrayList<>();
        List<HandBidPair> threeOfAKindList = new ArrayList<>();
        List<HandBidPair> twoPairList = new ArrayList<>();
        List<HandBidPair> onePairList = new ArrayList<>();
        List<HandBidPair> highCardList = new ArrayList<>();

        for (HandBidPair handBidPair : handBidPairs) {

            String hand = handBidPair.hand();



            List<LetterFrequency> letterFrequencies = calculateLetterFrequencies(hand);

            // hand = modifyHandWithJokers(hand);


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

        FirstCardComparator comparator = new FirstCardComparator();

        fiveOfAKindList.sort(comparator);
        fourOfAKindList.sort(comparator);
        fullHouseList.sort(comparator);
        threeOfAKindList.sort(comparator);
        twoPairList.sort(comparator);
        onePairList.sort(comparator);
        highCardList.sort(comparator);

        List<HandBidPair> theBigList = new ArrayList<>();
        theBigList.addAll(highCardList);
        theBigList.addAll(onePairList);
        theBigList.addAll(twoPairList);
        theBigList.addAll(threeOfAKindList);
        theBigList.addAll(fullHouseList);
        theBigList.addAll(fourOfAKindList);
        theBigList.addAll(fiveOfAKindList);

        int sum = 0;

        for (int i = 0; i < theBigList.size(); i++) {
            Utils.log(theBigList.get(i).toString());

            HandBidPair pair = theBigList.get(i);
            int summand = (i + 1) * pair.bid();
            sum += summand;

        }

        Utils.log("Solution Part 1: " + sum);

    }

//    private static String modifyHandWithJokers(final String hand) {
//        String[] handArray = hand.split("");
//        String highestCard = handArray[0];
//        for (int i = 1; i< handArray.length; i++) {
//            if (VALUE_MAP.get(handArray[i]) > VALUE_MAP.get(highestCard)) {
//                highestCard = handArray[i];
//            }
//        }
//
//        return hand.replaceAll("J", highestCard);
//    }


    private record HandBidPair(String hand, Integer bid) {

    }

    private static boolean checkFiveOfAKind(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 5;
    }

    private static boolean checkFourOfAKind(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 4;
    }

    private static boolean checkThreeOfAKind(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 3;
    }

    private static boolean checkFullHouse(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 3
                && letterFrequencies.get(1).frequency() == 2;
    }

    private static boolean checkTwoPair(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 2
                && letterFrequencies.get(1).frequency() == 2;
    }

    private static boolean checkOnePair(List<LetterFrequency> letterFrequencies) {
        return letterFrequencies.get(0).frequency() == 2
                && letterFrequencies.get(1).frequency() != 2;
    }

    //    private static  int getMaxFrequency(List<LetterFrequency> letterFrequencies){
    //        int max = 0;
    //        for (LetterFrequency letterFrequency : letterFrequencies) {
    //            if (letterFrequency.frequency()>max){
    //                max = letterFrequency.frequency();
    //            }
    //        }
    //
    //        return max;
    //    }

    public static List<LetterFrequency> calculateLetterFrequencies(String hand) {

        Map<String, Integer> frequencyMap = new HashMap<>();
        for (String s : hand.split("")) {
            frequencyMap.put(s, frequencyMap.getOrDefault(s, 0) + 1);
        }

        List<LetterFrequency> letterFrequencies = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : frequencyMap.entrySet()) {
            int frequency = entry.getValue();
            String letter = entry.getKey();

            letterFrequencies.add(new LetterFrequency(letter, frequency));
        }

        Collections.sort(letterFrequencies, Comparator.comparingInt(LetterFrequency::frequency).reversed());


        // ---------------------------------------------------------------------

        String maxLetter = letterFrequencies.get(0).maxLetter();
//        if(letterFrequencies.get(0).frequency()>1) {
            String modifiedHand = hand.replaceAll("J", maxLetter);

            Map<String, Integer> modifiedFrequencyMap = new HashMap<>();
            for (String s : modifiedHand.split("")) {
                modifiedFrequencyMap.put(s, modifiedFrequencyMap.getOrDefault(s, 0) + 1);
            }

            letterFrequencies = new ArrayList<>();

            for (Map.Entry<String, Integer> entry : modifiedFrequencyMap.entrySet()) {
                int frequency = entry.getValue();
                String letter = entry.getKey();

                letterFrequencies.add(new LetterFrequency(letter, frequency));
            }

            Collections.sort(letterFrequencies, Comparator.comparingInt(LetterFrequency::frequency).reversed());

//        }

        return letterFrequencies;
    }

    private record LetterFrequency(String maxLetter, int frequency) {

    }

    private static Map<String, Integer> initializeMap() {

        Map<String, Integer> valueMap = new HashMap<>();
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

    public static class FirstCardComparator implements Comparator<HandBidPair> {

        @Override
        public int compare(
                HandBidPair o1,
                HandBidPair o2
        ) {
            String[] hand1 = o1.hand().split("");
            String[] hand2 = o2.hand().split("");

            return compare(hand1, hand2);


        }

        private static int compare(
                final String[] hand1,
                final String[] hand2
        ) {
            for (int i = 0; i < hand1.length; i++) {
                if (VALUE_MAP.get(hand1[i]) > VALUE_MAP.get(hand2[i])) {
                    return 1;
                } else if (VALUE_MAP.get(hand1[i]) < VALUE_MAP.get(hand2[i])) {
                    return -1;
                }
            }
            return 0;
        }
    }


}