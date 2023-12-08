package com.pakotzy;

import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Task - https://adventofcode.com/2023/day/8");
        System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

        List<String> input = ConsoleReader.readToString(args, System.in);

        Solution solution = new Solution();
        System.out.printf("Amount of steps required to reach ZZZ as Human is %d%n", solution.traverseAsHuman(input));
        System.out.printf("Amount of steps required to reach ..Z as Ghost is %d%n", solution.traverseAsGhostLCM(input));
    }
}