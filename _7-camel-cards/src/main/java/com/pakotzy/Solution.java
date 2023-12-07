package com.pakotzy;

import java.util.*;

public class Solution {
    private static final Map<Character, Integer> FACE_CARDS = Map.of('A', 14, 'K', 13, 'Q', 12, 'J', 11, 'T', 10);
    private static final Map<Character, Integer> FACE_CARDS_W_JOKER = Map.of('A', 14, 'K', 13, 'Q', 12, 'J', 1, 'T', 10);

    public int calculateTotalWinnings(List<String> input) {
        TreeSet<Hand> table = new TreeSet<>();
        for (String line : input) {
            String[] lineParts = line.split(" ");
            Hand hand = calculateHand(lineParts[0].toCharArray(), Integer.parseInt(lineParts[1]));
            table.add(hand);
        }

        return countTotalWinnings(table);
    }


    public int calculateTotalWinningsWithJokers(List<String> input) {
        TreeSet<Hand> table = new TreeSet<>();
        for (String line : input) {
            String[] lineParts = line.split(" ");
            Hand hand = calculateHandWithJoker(lineParts[0].toCharArray(), Integer.parseInt(lineParts[1]));
            table.add(hand);
        }

        return countTotalWinnings(table);
    }

    private int countTotalWinnings(Set<Hand> table) {
        int i = 1;
        int winnings = 0;
        for (Hand hand : table) {
            winnings += hand.stake() * i;
            i++;
        }

        return winnings;
    }

    private Hand calculateHand(char[] cards, int stake) {
        Map<Integer, Integer> groupedCards = new HashMap<>();
        int[] hand = new int[5];
        boolean noDuplicates = true;
        for (int i = 0, cardsLength = cards.length; i < cardsLength; i++) {
            char card = cards[i];
            int cardValue;
            if (Character.isAlphabetic(card)) {
                cardValue = FACE_CARDS.get(card);
            } else {
                cardValue = Character.getNumericValue(card);
            }

            Integer cardQuantity = groupedCards.get(cardValue);
            if (cardQuantity == null) {
                groupedCards.put(cardValue, 1);
            } else {
                noDuplicates = false;
                groupedCards.put(cardValue, ++cardQuantity);
            }

            hand[i] = cardValue;
        }

        int handWorth;
        if (noDuplicates) {
            handWorth = 1;
        } else {
            handWorth = calculateHandWorth(groupedCards.values());
        }

        return new Hand(hand, stake, handWorth);
    }

    private Hand calculateHandWithJoker(char[] cards, int stake) {
        Map<Integer, Integer> groupedCards = new HashMap<>();
        int[] hand = new int[5];
        boolean noDuplicates = true;
        int jokers = 0;
        for (int i = 0, cardsLength = cards.length; i < cardsLength; i++) {
            char card = cards[i];
            int cardValue;
            if (Character.isAlphabetic(card)) {
                cardValue = FACE_CARDS_W_JOKER.get(card);
            } else {
                cardValue = Character.getNumericValue(card);
            }

            if (cardValue == FACE_CARDS_W_JOKER.get('J')) {
                jokers++;
            } else {
                Integer cardQuantity = groupedCards.get(cardValue);
                if (cardQuantity == null) {
                    groupedCards.put(cardValue, 1);
                } else {
                    noDuplicates = false;
                    groupedCards.put(cardValue, ++cardQuantity);
                }
            }

            hand[i] = cardValue;
        }

        return new Hand(hand, stake, calculateHandWorthWithJoker(groupedCards, noDuplicates, jokers));
    }

    private int calculateHandWorthWithJoker(Map<Integer, Integer> groupedCards, boolean noDuplicates, int jokers) {
        int handWorth;
        if (noDuplicates && jokers == 0) {
            handWorth = 1;
        } else if (jokers > 0) {
            Optional<Map.Entry<Integer, Integer>> optimalForJoker = findOptimalForJoker(groupedCards);
            if (optimalForJoker.isEmpty()) {
                groupedCards.put(FACE_CARDS_W_JOKER.get('A'), jokers);
            } else {
                groupedCards.put(optimalForJoker.get().getKey(), optimalForJoker.get().getValue() + jokers);
            }

            handWorth = calculateHandWorth(groupedCards.values());
        } else {
            handWorth = calculateHandWorth(groupedCards.values());
        }

        return handWorth;
    }

    private Optional<Map.Entry<Integer, Integer>> findOptimalForJoker(Map<Integer, Integer> groupedCards) {
        if (groupedCards.isEmpty()) {
            return Optional.empty();
        }

        Optional<Map.Entry<Integer, Integer>> opPlacement = groupedCards.entrySet().stream().filter(e -> e.getValue() >= 2).findFirst();
        return opPlacement.isPresent() ? opPlacement : groupedCards.entrySet().stream().filter(e -> !e.getKey().equals(FACE_CARDS_W_JOKER.get('J'))).findFirst();
    }

    private int calculateHandWorth(Collection<Integer> groupedCardsAmounts) {
        int worth = 0;
        for (Integer quantity : groupedCardsAmounts) {
            switch (quantity) {
                case 2 -> worth += 2;
                case 3 -> worth += 5;
                case 4 -> worth += 8;
                case 5 -> worth += 9;
                default -> {/* nothing */}
            }
        }
        return worth;
    }

    private record Hand(int[] cards, int stake, int worth) implements Comparable<Hand> {
        @Override
        public int compareTo(Hand o) {
            int comparison = worth - o.worth;
            if (comparison != 0) {
                return comparison;
            } else {
                int i = 0;
                while (comparison == 0 && i < cards.length) {
                    int targetCard = cards[i];
                    int oppositeCard = o.cards()[i];

                    comparison = targetCard - oppositeCard;
                    i++;
                }
            }

            return comparison;
        }
    }
}
