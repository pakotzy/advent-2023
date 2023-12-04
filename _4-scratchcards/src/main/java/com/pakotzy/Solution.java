package com.pakotzy;

import java.util.*;

public class Solution {
    public long calculateMatchPoints(List<String> input) {
        long combinedWinnings = 0L;
        for (String card : input) {
            int winsAmount = getWinsAmount(card);
            if (winsAmount > 1) {
                combinedWinnings += 1L << winsAmount - 1;
            } else if (winsAmount == 1) {
                combinedWinnings += 1;
            }
        }

        return combinedWinnings;
    }

    private int getWinsAmount(String card) {
        Set<String> winnings = new HashSet<>();
        int winsEmount = 0;
        boolean isWinnings = true;
        StringBuilder reverseNumberBuilder = new StringBuilder();

        char[] symbols = card.toCharArray();
        for (int i = symbols.length - 1; i >= 0; i--) {
            char symbol = symbols[i];
            if (Character.isDigit(symbol)) {
                reverseNumberBuilder.append(Character.getNumericValue(symbol));
            } else if (symbol == ' ' || symbol == '|') {
                String currentNumber = reverseNumberBuilder.reverse().toString();
                if (isWinnings && !currentNumber.isEmpty()) {
                    winnings.add(currentNumber);
                } else if (winnings.contains(currentNumber)) {
                    winsEmount++;
                }

                reverseNumberBuilder = new StringBuilder();
                isWinnings = isWinnings && symbol != '|';
            } else if (symbol == ':') {
                break;
            }
        }

        return winsEmount;
    }

    public int calculateTotalCardsMath(List<String> input) {
        int totalCards = 0;
        Map<Integer, Integer> deck = new HashMap<>();
        for (int i = 0; i < input.size(); i++) {
            String card = input.get(i);
            int winsAmount = getWinsAmount(card);
            placeCard(deck, i, 1);

            if (winsAmount > 0) {
                totalCards += deck.get(i);
                for (int j = i + 1; j <= i + winsAmount; j++) {
                    placeCard(deck, j, deck.get(i));
                }
            } else {
                totalCards += deck.get(i);
            }
        }

        return totalCards;
    }

    private void placeCard(Map<Integer, Integer> deck, int card, int amount) {
        deck.merge(card, amount, Integer::sum);
    }

    public int calculateTotalCardsRecursion(List<String> input) {
        Map<Integer, Card> deck = new HashMap<>();
        for (int i = input.size() - 1; i >= 0; i--) {
            String line = input.get(i);
            int lineWorth = getWinsAmount(line);

            deck.put(i, new Card(lineWorth, 1));
            addChildCards(deck, i);
        }

        return deck.values().stream().mapToInt(Card::getAmount).sum();
    }

    private void addChildCards(Map<Integer, Card> deck, int cardId) {
        Card card = deck.get(cardId);
        for (int i = cardId + 1; i <= cardId + card.worth; i++) {
            deck.get(i).increaseAmount();
            addChildCards(deck, i);
        }
    }

    private static class Card {
        public final int worth;
        private int amount;

        public Card(int worth, int amount) {
            this.worth = worth;
            this.amount = amount;
        }

        public int getAmount() {
            return amount;
        }

        public void increaseAmount() {
            amount++;
        }
    }
}
