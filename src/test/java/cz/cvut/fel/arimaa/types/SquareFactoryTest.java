package cz.cvut.fel.arimaa.types;

import org.junit.jupiter.api.Test;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class SquareFactoryTest {

    @Test
    public void testValidCoordinatesGetSquare() {
        Square square = SquareFactory.getSquare(0, 7);
        assertEquals("a1", square.toString());
    }

    @Test
    public void testInvalidCoordinatesGetSquare() {
        assertNull(SquareFactory.getSquare(-1, 5));
    }

    @Test
    public void testValidStringGetSquare() {
        Square square = SquareFactory.getSquare("f5");
        assertEquals("f5", square.toString());
    }

    @Test
    public void testInvalidStringGetSquare() {
        assertNull(SquareFactory.getSquare("t1"));
    }
}
