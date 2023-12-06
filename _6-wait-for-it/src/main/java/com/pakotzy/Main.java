package com.pakotzy;

import java.io.IOException;
import java.util.List;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) throws IOException {
        System.out.println("Task - https://adventofcode.com/2023/day/6");
        System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

        List<String> input = ConsoleReader.readToString(args, System.in);
        Solution solution = new Solution();

        long waysToRecord = solution.findWaysToWin(input);
        System.out.printf("Number of ways you can beat the record is %d%n", waysToRecord);

        long waysToRecordWithoutKerning = solution.findWaysToWinWithoutKerning(input);
        System.out.printf("Number of ways you can beat the record without kerning is %d%n", waysToRecordWithoutKerning);
    }
}