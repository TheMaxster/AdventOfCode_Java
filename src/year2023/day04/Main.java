package year2023.day04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import utils.ImportUtils;

public class Main {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day04/input_05_test_01.txt";
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day04/input_05.txt";

        final List<String> inputList = ImportUtils.readAsList(filePath);

        final List<LotteryCard> lotteryCardList = processLotteryCards(inputList);

        printResults(lotteryCardList);

    }

    private static List<LotteryCard> processLotteryCards(List<String> inputList) {
        return inputList.stream()
                .map(Main::processCardString)
                .collect(Collectors.toList());
    }

    private static LotteryCard processCardString(final String line) {

        // Trenne nach dem ":"
        String[] valuesSplittedAtColon = line.split(":");
        String cardName = valuesSplittedAtColon[0].trim();
        String cardValues = valuesSplittedAtColon[1].trim();

        //            System.out.println("Card Name: " + cardName);
        //            System.out.println("Card Values: " + cardValues);

        // Trenne nach dem "|"
        String[] valuesSplittedAtPipe = cardValues.split("\\|");
        String valuesBeforePipe = valuesSplittedAtPipe[0].trim();
        String valuesAfterPipe = valuesSplittedAtPipe[1].trim();

        //            System.out.println("Values Before Pipe: " + valuesBeforePipe);
        //            System.out.println("Values After Pipe: " + valuesAfterPipe);

        // Trenne jede einzelne Nummer.
        List<String> winningNumbers = Arrays.stream(valuesBeforePipe.split(" ")).map(String::trim).filter(s -> s != "").toList();
        List<String> ourNumbers = Arrays.stream(valuesAfterPipe.split(" ")).map(String::trim).filter(s -> s != "").toList();

        //                        System.out.println("Winning Numbers: " + winningNumbers);
        //                        System.out.println("Our Numbers: " + ourNumbers);

        int matches = 0;
        for (String numberToCheck : ourNumbers) {
            if (winningNumbers.contains(numberToCheck)) {
                matches++;
            }
        }

        int points = matches > 0 ? (int) Math.pow(2, matches - 1) : 0;

        System.out.println(cardName + ": " + matches + " matches => " + points + " points");

        return new LotteryCard(cardName, points, matches, 1);
    }

    private static void printResults(final List<LotteryCard> lotteryCardList) {
        int allPoints = lotteryCardList.stream().map(LotteryCard::getPoints).mapToInt(Integer::intValue).sum();
        System.out.println("Part 1: In sum we received " + allPoints + " points");

        // Part 2
        for (int i = 0; i < lotteryCardList.size(); i++) {
            LotteryCard lotteryCard = lotteryCardList.get(i);
            for (int h = 0; h < lotteryCard.getAmountOfCards(); h++) {
                for (int j = 1; j <= lotteryCard.getMatches(); j++) {
                    lotteryCardList.get(i + j).increaseAmountOfCards();
                }
            }
        }

        // Sum up all cards
        int allCards = 0;
        for (LotteryCard lotteryCard : lotteryCardList) {
            System.out.println(lotteryCard.getCardName() + ": " + lotteryCard.getAmountOfCards());
            allCards += lotteryCard.getAmountOfCards();
        }

        System.out.println("Part 2: Our total amount of cards is: " + allCards);
    }

    private static class LotteryCard {

        int matches;
        String cardName;
        int points;
        int amountOfCards;

        public LotteryCard(
                final String cardName,
                final int points,
                final int matches,
                final int amountOfCards

        ) {
            this.cardName = cardName;
            this.points = points;
            this.matches = matches;
            this.amountOfCards = amountOfCards;
        }

        public String getCardName() {
            return cardName;
        }

        public void setCardName(final String cardName) {
            this.cardName = cardName;
        }

        public int getPoints() {
            return points;
        }

        public void setPoints(final int points) {
            this.points = points;
        }

        public int getMatches() {
            return matches;
        }

        public void setMatches(final int matches) {
            this.matches = matches;
        }

        public int getAmountOfCards() {
            return amountOfCards;
        }

        public void setAmountOfCards(final int amountOfCards) {
            this.amountOfCards = amountOfCards;
        }

        public void increaseAmountOfCards() {
            amountOfCards++;
        }

    }

}
