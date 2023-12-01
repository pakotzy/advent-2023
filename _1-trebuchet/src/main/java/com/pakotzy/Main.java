package com.pakotzy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class Main {
    @SuppressWarnings("java:S106")
    public static void main(String[] args) throws IOException {
        InputStream inputStream;
        if (args.length > 0) {
            inputStream = new ByteArrayInputStream(args[0].getBytes());
        } else {
            System.out.println("Task - https://adventofcode.com/2023/day/1");
            System.out.println("Copy paste or type the input. Double 'Enter' will stop the input!");

            inputStream = System.in;
        }

        long calibrationValue = new Solution().calculateCalibration(inputStream);
        System.out.printf("Sum of all the calibration values is %d", calibrationValue);
    }
}