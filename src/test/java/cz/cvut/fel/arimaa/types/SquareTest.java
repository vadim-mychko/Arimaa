package cz.cvut.fel.arimaa.types;

import org.junit.jupiter.api.Test;

import static cz.cvut.fel.arimaa.types.Square.getSquare;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SquareTest {

    @Test
    public void testValidCoordinatesGetSquare() {
        Square square = getSquare(0, 7);
        assertEquals("a1", square.toString());
    }

    @Test
    public void testInvalidCoordinatesGetSquare() {
        assertNull(getSquare(-1, 5));
    }

    @Test
    public void testValidStringGetSquare() {
        Square square = getSquare("f5");
        assertEquals("f5", square.toString());
    }

    @Test
    public void testInvalidStringGetSquare() {
        assertNull(getSquare("t1"));
    }
}
