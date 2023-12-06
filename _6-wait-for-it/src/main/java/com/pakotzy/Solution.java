package com.pakotzy;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Solution {
    public long findWaysToWin(List<String> input) {
        List<Integer> times = getNumbersFromInput(input.get(0));
        List<Integer> distances = getNumbersFromInput(input.get(1));

        long totalWaysToWin = 1;
        for (int i = 0, timesSize = times.size(); i < timesSize; i++) {
            totalWaysToWin *= waysToWin(times.get(i), distances.get(i));
        }

        return totalWaysToWin;
    }

    public long findWaysToWinWithoutKerning(List<String> input) {
        Long time = getNumbersFromInputWithoutKerning(input.get(0));
        Long distance = getNumbersFromInputWithoutKerning(input.get(1));

        return waysToWin(time, distance);
    }

    private long waysToWin(long time, long distance) {
        long timeWaysToWin = 0;

        long holdTime = 1;
        while (holdTime < time) {
            long travelTime = time - holdTime;
            float requiredSpeed = (float) distance / travelTime;
            float speedAtTime = time - travelTime;
            if (requiredSpeed < speedAtTime) {
                timeWaysToWin++;
            }

            holdTime++;
        }

        return timeWaysToWin;
    }

    private List<Integer> getNumbersFromInput(String input) {
        Predicate<String> checkIfInteger = s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        };
        return Arrays.stream(input.split(" ")).filter(checkIfInteger).mapToInt(Integer::parseInt).boxed().toList();
    }

    private Long getNumbersFromInputWithoutKerning(String input) {
        Predicate<String> checkIfInteger = s -> {
            try {
                Integer.parseInt(s);
                return true;
            } catch (NumberFormatException e) {
                return false;
            }
        };
        return Long.parseLong(Arrays.stream(input.split(" ")).filter(checkIfInteger).collect(Collectors.joining("")));
    }
}
