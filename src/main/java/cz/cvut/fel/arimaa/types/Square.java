package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Objects;

import static java.lang.Math.abs;

public class Square {

    private static final Square[][] squares = new Square[Board.WIDTH][Board.HEIGHT];

    public final int x;
    public final int y;

    private Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public static Square getSquare(String repr) {
        if (!repr.matches("[a-h][1-8]")) {
            return null;
        }

        int x = repr.charAt(0) - 'a';
        int y = Board.HEIGHT - (repr.charAt(1) - '0');

        return getSquare(x, y);
    }

    public static Square getSquare(int x, int y) {
        if (x < 0 || x >= Board.WIDTH || y < 0 || y >= Board.HEIGHT) {
            return null;
        }

        if (squares[x][y] == null) {
            squares[x][y] = new Square(x, y);
        }

        return squares[x][y];
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Square square = (Square) o;
        return x == square.x && y == square.y;
    }

    @Override
    public String toString() {
        return getFile() + "" + getRank();
    }

    public char getFile() {
        return (char) ('a' + x);
    }

    public int getRank() {
        return Board.HEIGHT - y;
    }

    public boolean isAdjacentTo(Square square) {
        if (square == null) {
            return false;
        }

        return abs(x - square.x) + abs(y - square.y) == 1;
    }
}