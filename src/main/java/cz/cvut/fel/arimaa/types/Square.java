package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Objects;

import static java.lang.Math.abs;

/**
 * Type-safe immutable class for representing position on a board
 * (instead of regular integers).
 */
public class Square {

    private static final Square[][] squares = new Square[Board.WIDTH][Board.HEIGHT];

    /**
     * X-coordinate of the position on a board.
     */
    public final int x;

    /**
     * Y-coordinate of the position on a board.
     */
    public final int y;

    private Square(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Get the (unique) square instance based on its textual representation.
     * Textual representation has to be in standard PGN format (i.e. e5).
     * Character has to be lowercase.
     *
     * @param repr Textual representation of the position on a board.
     * @return the (unique) instance of Square on success, null otherwise
     */
    public static Square getSquare(String repr) {
        if (!repr.matches("[a-h][1-8]")) {
            return null;
        }

        int x = repr.charAt(0) - 'a';
        int y = Board.HEIGHT - (repr.charAt(1) - '0');

        return getSquare(x, y);
    }

    /**
     * Get the (unique) square instance based on its coordinates.
     *
     * @param x X-coordinate of the position.
     * @param y Y-coordinate of the position.
     * @return the (unique) instance of Square on success, null otherwise
     */
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

    /**
     * Get the file (i.e. e) of the square instance.
     *
     * @return file of the square instance
     */
    public char getFile() {
        return (char) ('a' + x);
    }

    /**
     * Get the rank (i.e. 5) of the square instance.
     *
     * @return rank of the square instance
     */
    public int getRank() {
        return Board.HEIGHT - y;
    }

    /**
     * Check if this position is adjacent to another position on a board.
     *
     * @param square Square for checking adjacency.
     * @return true if this position is adjacent to another position on a board,
     * false otherwise
     */
    public boolean isAdjacentTo(Square square) {
        if (square == null) {
            return false;
        }

        return abs(x - square.x) + abs(y - square.y) == 1;
    }
}
