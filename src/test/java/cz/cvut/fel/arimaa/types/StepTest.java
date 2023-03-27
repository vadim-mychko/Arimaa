package cz.cvut.fel.arimaa.types;

import org.junit.jupiter.api.Test;

import static cz.cvut.fel.arimaa.types.Square.getSquare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StepTest {

    @Test
    public void testFromString() {
        Step expected = new Step(Piece.fromRepr('E'), getSquare("a1"),
                Direction.NORTH, false, StepType.SIMPLE);
        assertEquals(expected, Step.fromString("Ea1ns"));
    }
}
