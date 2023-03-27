package cz.cvut.fel.arimaa.types;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PieceTest {

    @Test
    public void testToString() {
        Piece elephant = Piece.fromRepr('E');
        assertEquals("GoldElephant", elephant.toString());
    }
}
