package com.pakotzy;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EngineSchematicTest {
    @Test
    void addEnginePartWhenExampleInput() {
        EngineSchematic engineSchematic = new EngineSchematic();
        engineSchematic.addEnginePart("35", new Point(3, 2));

        assertEquals("35", engineSchematic.getEngineParts().get(new Point(3, 1)).get(0));
    }
}