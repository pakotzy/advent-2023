package com.pakotzy;

import java.awt.*;
import java.util.List;

public class Solution {
    public long solve(EngineSchematic engineSchematic) {
        long partsSum = 0L;
        for (Point marker : engineSchematic.getMarkers()) {
            List<String> partsAtMarker = engineSchematic.getEngineParts().get(marker);
            if (partsAtMarker != null && !partsAtMarker.isEmpty()) {
                partsSum += partsAtMarker.stream().mapToInt(Integer::parseInt).sum();
            }
        }

        return partsSum;
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
                    engineSchematic.addMarker(new Point(j, lineNumber));
                }
            }
        }

        if (!partNumberBuilder.isEmpty()) {
            engineSchematic.addEnginePart(partNumberBuilder.toString(), partPoint);
        }
    }
}
