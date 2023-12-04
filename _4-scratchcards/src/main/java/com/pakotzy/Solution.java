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
}
