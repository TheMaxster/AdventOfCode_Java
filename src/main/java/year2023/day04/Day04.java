package year2023.day04;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import application.Day;

/**
 * See https://adventofcode.com/2023/day/4
 */
public class Day04 extends Day {

    @Override
    public Boolean getLoggingEnabled() {
        return true;
    }

    @Override
    public String part1(final List<String> input) {
        final List<LotteryCard> lotteryCardList = processLotteryCards(input);
        final Integer allPoints = lotteryCardList.stream().map(LotteryCard::getPoints).mapToInt(Integer::intValue).sum();
        return allPoints.toString();
    }

    @Override
    public String part2(final List<String> input) {
        final List<LotteryCard> lotteryCardList = processLotteryCards(input);

        for (int i = 0; i < lotteryCardList.size(); i++) {
            final LotteryCard lotteryCard = lotteryCardList.get(i);
            for (int h = 0; h < lotteryCard.getAmountOfCards(); h++) {
                for (int j = 1; j <= lotteryCard.getMatches(); j++) {
                    lotteryCardList.get(i + j).increaseAmountOfCards();
                }
            }
        }

        // Sum up all cards
        int allCards = 0;
        for (final LotteryCard lotteryCard : lotteryCardList) {
            log(lotteryCard.getCardName() + ": " + lotteryCard.getAmountOfCards());
            allCards += lotteryCard.getAmountOfCards();
        }

        return String.valueOf(allCards);
    }

    private List<LotteryCard> processLotteryCards(final List<String> inputList) {
        return inputList.stream()
                .map(this::processCardString)
                .collect(Collectors.toList());
    }

    private LotteryCard processCardString(final String line) {

        // Trenne nach dem ":"
        final String[] valuesSplittedAtColon = line.split(":");
        final String cardName = valuesSplittedAtColon[0].trim();
        final String cardValues = valuesSplittedAtColon[1].trim();

        //            log("Card Name: " + cardName);
        //            log("Card Values: " + cardValues);

        // Trenne nach dem "|"
        final String[] valuesSplittedAtPipe = cardValues.split("\\|");
        final String valuesBeforePipe = valuesSplittedAtPipe[0].trim();
        final String valuesAfterPipe = valuesSplittedAtPipe[1].trim();

        //            log("Values Before Pipe: " + valuesBeforePipe);
        //            log("Values After Pipe: " + valuesAfterPipe);

        // Trenne jede einzelne Nummer.
        final List<String> winningNumbers = Arrays.stream(valuesBeforePipe.split(" ")).map(String::trim).filter(s -> s != "").toList();
        final List<String> ourNumbers = Arrays.stream(valuesAfterPipe.split(" ")).map(String::trim).filter(s -> s != "").toList();

        //                        log("Winning Numbers: " + winningNumbers);
        //                        log("Our Numbers: " + ourNumbers);

        int matches = 0;
        for (final String numberToCheck : ourNumbers) {
            if (winningNumbers.contains(numberToCheck)) {
                matches++;
            }
        }

        final int points = matches > 0 ? (int) Math.pow(2, matches - 1) : 0;

        log(cardName + ": " + matches + " matches => " + points + " points");

        return new LotteryCard(cardName, points, matches, 1);
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
