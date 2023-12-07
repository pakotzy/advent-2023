package com.pakotzy;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Task - https://adventofcode.com/2023/day/7");
        System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

        List<String> input = ConsoleReader.readToString(args, System.in);
        Solution solution = new Solution();

        System.out.printf("Total winnings are %d%n", solution.calculateTotalWinnings(input));
        System.out.printf("Total winnings with Jokers are %d", solution.calculateTotalWinningsWithJokers(input));
    }
}