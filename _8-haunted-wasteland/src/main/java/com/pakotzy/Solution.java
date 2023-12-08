package com.pakotzy;

import org.apache.commons.math3.util.ArithmeticUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Solution {
    public int traverseAsHuman(List<String> input) {
        char[] steps = getDirections(input);
        Map<String, Map.Entry<String, String>> map = createMap(input);

        String currentNode = "AAA";
        String targetNode = "ZZZ";
        int step = 0;
        int totalSteps = 0;
        while (!currentNode.equals(targetNode)) {
            currentNode = getNextDirection(steps, map, step, currentNode);
            step = getNextStep(steps, step);
            totalSteps++;
        }

        return totalSteps;
    }

    public int traverseAsGhost(List<String> input) {
        char[] steps = getDirections(input);
        Map<String, Map.Entry<String, String>> map = createMap(input);
        List<String> ghosts = findGhosts(map);

        boolean allArrived = false;
        int maxSteps = 0;
        int step = 0;
        while (!allArrived) {
            allArrived = true;

            List<String> nextNodes = new ArrayList<>();
            for (String currentNode : ghosts) {
                String nextNode = getNextDirection(steps, map, step, currentNode);
                if (!nextNode.endsWith("Z")) {
                    allArrived = false;
                }
                nextNodes.add(nextNode);
            }

            step = getNextStep(steps, step);
            maxSteps++;
            ghosts = nextNodes;
        }

        return maxSteps;
    }

    public long traverseAsGhostLCM(List<String> input) {
        char[] steps = getDirections(input);
        Map<String, Map.Entry<String, String>> map = createMap(input);
        List<String> ghosts = findGhosts(map);

        long lcm = 1L;
        int step = 0;
        for (String node : ghosts) {
            String currentNode = node;
            long stepsMade = 0L;
            while (!currentNode.endsWith("Z")) {
                currentNode = getNextDirection(steps, map, step, currentNode);
                step = getNextStep(steps, step);
                stepsMade++;
            }

            lcm = ArithmeticUtils.lcm(lcm, stepsMade);
        }

        return lcm;
    }

    private String getNextDirection(char[] steps, Map<String, Map.Entry<String, String>> map, int step, String currentNode) {
        String nextNode = currentNode;
        switch (steps[step]) {
            case 'R' -> nextNode = map.get(currentNode).getValue();
            case 'L' -> nextNode = map.get(currentNode).getKey();
            default -> { /*not going to happen*/}
        }

        return nextNode;
    }

    private int getNextStep(char[] steps, int currentStep) {
        return currentStep < steps.length-1 ? ++currentStep : 0;
    }

    private char[] getDirections(List<String> input) {
        return input.getFirst().toCharArray();
    }

    private Map<String, Map.Entry<String, String>> createMap(List<String> input) {
        Map<String, Map.Entry<String, String>> map = new HashMap<>();
        for (int i = 1, inputSize = input.size(); i < inputSize; i++) {
            String[] line = input.get(i).split(" = ");
            String node = line[0].substring(0, 3);
            Map.Entry<String, String> connections = Map.entry(line[1].substring(1, 4), line[1].substring(6, 9));
            map.put(node, connections);
        }

        return map;
    }

    private List<String> findGhosts(Map<String, Map.Entry<String, String>> map) {
        return map.keySet().stream().filter(n -> n.endsWith("A")).toList();
    }
}
