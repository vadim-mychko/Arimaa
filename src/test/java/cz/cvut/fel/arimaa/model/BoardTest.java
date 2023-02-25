package cz.cvut.fel.arimaa.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    @Test
    public void testLoadEmptyBoard() {
        assertEquals(Board.EMPTY_BOARD, new Board().toString());
    }

    @Test
    public void testLoadDefaultBoard() {
        Board board = new Board();
        board.load();

        assertEquals(Board.DEFAULT_BOARD, board.toString());
    }
}
