package com.pakotzy;

import java.io.IOException;
import java.util.List;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) throws IOException {
        System.out.println("Task - https://adventofcode.com/2023/day/4");
        System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

        List<String> input = ConsoleReader.readToString(args, System.in);

        Solution solution = new Solution();
        long totalWorth = solution.calculateMatchPoints(input);
        System.out.printf("Total worth of points are is %d%n", totalWorth);
    }
}