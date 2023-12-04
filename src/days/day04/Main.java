package days.day04;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import utils.ImportUtils;

public class Main {

    public static void main(String[] args) {
        final String filePath = System.getProperty("user.dir") + "/resources/days/day04/input_04_test_01.txt";
        //final String filePath = System.getProperty("user.dir") + "/resources/days/day04/input_04.txt";

        final List<String> inputList = ImportUtils.readAsList(filePath);

        List<Card> cardList = new ArrayList<>();

        for (String line : inputList) {

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

            cardList.add(new Card(cardName, points, matches, 1));

        }

        int allPoints = cardList.stream().map(Card::getPoints).mapToInt(Integer::intValue).sum();
        System.out.println("Part 1: In sum we received " + allPoints + " points");

        // Part 2
        for (int i = 0; i < cardList.size(); i++) {
            Card card = cardList.get(i);
            for (int h = 0; h < card.getAmountOfCards(); h++) {
                for (int j = 1; j <= card.getMatches(); j++) {
                    cardList.get(i + j).increaseAmountOfCards();
                }
            }
        }

        // Sum up all cards
        int allCards = 0;
        for (Card card : cardList) {
            System.out.println(card.getCardName() + ": " + card.getAmountOfCards());
            allCards += card.getAmountOfCards();
        }

        System.out.println("Part 2: Our total amount of cards is: " + allCards);

    }

    private static class Card {

        int matches;
        String cardName;
        int points;
        int amountOfCards;

        public Card(
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