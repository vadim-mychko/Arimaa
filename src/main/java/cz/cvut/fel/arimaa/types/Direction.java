package cz.cvut.fel.arimaa.types;

/**
 * Class for representing direction of movement of figures on a board.
 */
public enum Direction {

    NORTH('n', 0, -1),
    SOUTH('s', 0, 1),
    EAST('e', 1, 0),
    WEST('w', -1, 0);

    /**
     * Textual representation of the direction in PGN format.
     */
    public final char repr;
    private final int dx;
    private final int dy;

    /**
     * Constructs an instance of Direction with the given textual representation
     * and shifts.
     *
     * @param repr Textual representation of the direction.
     * @param dx   X-coordinate shift.
     * @param dy   Y-coordinate shift.
     */
    Direction(char repr, int dx, int dy) {
        this.repr = repr;
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Get the opposite direction of the given direction.
     *
     * @param direction Direction for getting opposite direction to.
     * @return opposite direction
     */
    public static Direction getOppositeDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }

    /**
     * Get the direction for getting from the given source to the given
     * destination square.
     *
     * @param from Source of the movement.
     * @param to   Destination of the movement.
     * @return direction for getting from the given source to the given
     * destination square on success, null otherwise.
     */
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

    /**
     * Get an instance of the Direction based on its textual representation.
     *
     * @param repr Textual representation of the direction.
     * @return an instance of the Direction based on its textual representation
     * on success, null otherwise
     */
    public static Direction fromRepr(char repr) {
        return switch (repr) {
            case 'n' -> NORTH;
            case 's' -> SOUTH;
            case 'e' -> EAST;
            case 'w' -> WEST;
            default -> null;
        };
    }

    /**
     * Make a step in the direction from the given source square and get a
     * destination square.
     *
     * @param pos Source square of the movement.
     * @return destination square on success, null otherwise (i.e. if square is
     * not on board anymore)
     */
    public Square shift(Square pos) {
        return Square.getSquare(pos.x + dx, pos.y + dy);
    }
}
