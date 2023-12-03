package com.pakotzy;

import java.awt.*;
import java.util.*;
import java.util.List;

public class EngineSchematic {
    private final Map<Point, List<String>> engineParts = new HashMap<>();
    private final Map<Point, Character> markers = new HashMap<>();

    public Map<Point, Character> getMarkers() {
        return markers;
    }

    public void addMarker(Point location, Character marker) {
        markers.put(location, marker);
    }

    public Map<Point, List<String>> getEngineParts() {
        return engineParts;
    }

    public void addEnginePart(String enginePart, Point endLocation) {
        int partLength = enginePart.length();

        Set<Point> partPoints = new HashSet<>();
        for (int i = 0; i < partLength; i++) {
            Point topLeft = new Point(endLocation.x - i - 1, endLocation.y - 1);
            Point topMiddle = new Point(endLocation.x - i, endLocation.y - 1);
            Point topRight = new Point(endLocation.x - i + 1, endLocation.y - 1);

            Point centerLeft = new Point(endLocation.x - i - 1, endLocation.y);
            Point centerMiddle = new Point(endLocation.x - i, endLocation.y);
            Point centerRight = new Point(endLocation.x - i + 1, endLocation.y);

            Point bottomLeft = new Point(endLocation.x - i - 1, endLocation.y + 1);
            Point bottomMiddle = new Point(endLocation.x - i, endLocation.y + 1);
            Point bottomRight = new Point(endLocation.x - i + 1, endLocation.y + 1);

            partPoints.addAll(Arrays.asList(topLeft, topMiddle, topRight, centerLeft, centerMiddle, centerRight, bottomLeft, bottomMiddle, bottomRight));
        }

        placePart(partPoints, enginePart);
    }

    private void placePart(Set<Point> points, String part) {
        for (Point point : points) {
            List<String> partsAtPoint = engineParts.get(point);
            if (partsAtPoint == null) {
                List<String> partNumbers = new ArrayList<>();
                partNumbers.add(part);

                engineParts.put(point, partNumbers);
            } else {
                partsAtPoint.add(part);
            }
        }
    }
}
