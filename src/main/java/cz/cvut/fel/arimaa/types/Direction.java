package cz.cvut.fel.arimaa.types;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

public enum Direction {

    NORTH('n', 0, -1),
    SOUTH('s', 0, 1),
    EAST('e', 1, 0),
    WEST('w', -1, 0);

    public final char repr;
    public final int dx;
    public final int dy;

    Direction(char repr, int dx, int dy) {
        this.repr = repr;
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction getOpposingDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public Square shift(Square pos) {
        return SquareFactory.getSquare(pos.x + dx, pos.y + dy);
    }
}
