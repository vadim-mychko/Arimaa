package cz.cvut.fel.arimaa.types;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

public enum Direction {

    NORTH('n', 0, -1),
    SOUTH('s', 0, 1),
    EAST('e', 1, 0),
    WEST('w', -1, 0);

    public final char repr;
    private final int dx;
    private final int dy;

    Direction(char repr, int dx, int dy) {
        this.repr = repr;
        this.dx = dx;
        this.dy = dy;
    }

    public static Direction getOppositeDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    public static Direction getDirection(Square from, Square to) {
        if (from == null || to == null || !from.isAdjacentTo(to)) {
            return null;
        }

        return fromDisplacement(to.x - from.x, to.y - from.y);
    }

    private static Direction fromDisplacement(int dx, int dy) {
        return dx == 0
                ? (dy == 1 ? SOUTH : NORTH)
                : (dx == 1 ? EAST : WEST);
    }

    public static Direction fromRepr(char repr) {
        return switch (repr) {
            case 'n' -> NORTH;
            case 's' -> SOUTH;
            case 'e' -> EAST;
            case 'w' -> WEST;
            default -> null;
        };
    }

    public Square shift(Square pos) {
        return SquareFactory.getSquare(pos.x + dx, pos.y + dy);
    }
}
