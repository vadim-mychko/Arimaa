package cz.cvut.fel.arimaa.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SquareTest {

    @Test
    public void testValidCoordinatesGetSquare() {
        Square square = Square.getSquare(0, 7);
        assertEquals("a1", square.toString());
    }

    @Test
    public void testInvalidCoordinatesGetSquare() {
        assertNull(Square.getSquare(-1, 5));
    }

    @Test
    public void testValidStringGetSquare() {
        Square square = Square.getSquare("f5");
        assertEquals("f5", square.toString());
    }

    @Test
    public void testInvalidStringGetSquare() {
        assertNull(Square.getSquare("t1"));
    }
}
