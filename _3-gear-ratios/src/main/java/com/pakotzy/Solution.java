package com.pakotzy;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class Solution {
    public long calculateParts(EngineSchematic engineSchematic) {
        long partsSum = 0L;
        for (Map.Entry<Point, Character> marker : engineSchematic.getMarkers().entrySet()) {
            List<String> partsAtMarker = engineSchematic.getEngineParts().get(marker.getKey());
            if (partsAtMarker != null && !partsAtMarker.isEmpty()) {
                    partsSum += partsAtMarker.stream().mapToInt(Integer::parseInt).sum();

            }
        }

        return partsSum;
    }

    public long calculateGears(EngineSchematic engineSchematic) {
        long gearsRatio = 0L;
        for (Map.Entry<Point, Character> marker : engineSchematic.getMarkers().entrySet()) {
            List<String> partsAtMarker = engineSchematic.getEngineParts().get(marker.getKey());
            if (partsAtMarker != null && marker.getValue() == '*' && partsAtMarker.size() == 2) {
                    gearsRatio += partsAtMarker.stream().mapToLong(Long::parseLong).reduce(1L, (left, right) -> left * right);

            }
        }

        return gearsRatio;
    }

    public EngineSchematic mapEngine(List<String> input) {
        EngineSchematic engineSchematic = new EngineSchematic();
        for (int i = 0; i < input.size(); i++) {
            processLine(i, input.get(i), engineSchematic);
        }

        return engineSchematic;
    }

    private void processLine(int lineNumber, String line, EngineSchematic engineSchematic) {
        StringBuilder partNumberBuilder = new StringBuilder();
        Point partPoint = new Point(0, lineNumber);

        char[] charArray = line.toCharArray();
        for (int j = 0; j < charArray.length; j++) {
            char symbol = charArray[j];

            if (Character.isDigit(symbol)) {
                partNumberBuilder.append(Character.getNumericValue(symbol));
                partPoint = new Point(j, lineNumber);
            } else {
                if (!partNumberBuilder.isEmpty()) {
                    engineSchematic.addEnginePart(partNumberBuilder.toString(), partPoint);
                    partNumberBuilder = new StringBuilder();
                }

                if (symbol != '.') {
                    engineSchematic.addMarker(new Point(j, lineNumber), symbol);
                }
            }
        }

        if (!partNumberBuilder.isEmpty()) {
            engineSchematic.addEnginePart(partNumberBuilder.toString(), partPoint);
        }
    }
}
