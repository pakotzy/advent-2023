package com.pakotzy;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SolutionTest {
    private final Solution solution = new Solution();

    @Test
    void findWaysToWinWhenExampleInput() {
        assertEquals(288, solution.findWaysToWin(exampleInput()));
    }

    @Test
    void findWaysToWinWithoutKerningWhenExampleInput() {
        assertEquals(71503, solution.findWaysToWinWithoutKerning(exampleInput()));
    }

    @Test
    void findWaysToWinWhenProblemInput() {
        assertEquals(1312850, solution.findWaysToWin(problemInput()));
    }

    @Test
    void findWaysToWinWithoutKerningWhenProblemInput() {
        assertEquals(36749103, solution.findWaysToWinWithoutKerning(problemInput()));
    }

    private List<String> exampleInput() {
        return Arrays.asList("""
                Time:      7  15   30
                Distance:  9  40  200""".split("\n"));
    }

    private List<String> problemInput() {
        return Arrays.asList("""
                Time:        48     93     84     66
                Distance:   261   1192   1019   1063""".split("\n"));
    }
}