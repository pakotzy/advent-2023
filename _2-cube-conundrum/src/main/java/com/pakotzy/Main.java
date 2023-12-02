package com.pakotzy;

import java.io.IOException;
import java.util.List;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) throws IOException {
        System.out.println("Task - https://adventofcode.com/2023/day/2");
        System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

        List<String> input = ConsoleReader.readToString(args, System.in);
        Solution solution = new Solution(12, 13, 14);
        List<Game> games = solution.compileGames(input);

        int gameSum = solution.sumValidGames(games);
        System.out.printf("Sum of the ids of valid games is %d%n", gameSum);

        long sumPowerLeastValidSets = solution.sumPowerLeastValidSets(games);
        System.out.printf("Sum of the power of least valid sets is %d", sumPowerLeastValidSets);
    }
}