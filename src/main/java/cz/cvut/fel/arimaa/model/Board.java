package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Piece;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    private final Piece[][] board;

    Board() {
        board = new Piece[WIDTH][HEIGHT];
    }
}
