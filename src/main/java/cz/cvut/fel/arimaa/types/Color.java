package cz.cvut.fel.arimaa.types;

/**
 * Class for representing color of figures on a board.
 */
public enum Color {

    GOLD('g', 'w'), SILVER('s', 'b');

    /**
     * Character representation of the color in standard PGN format.
     */
    public final char repr;

    /**
     * Old character representation of the color in standard PGN format.
     */
    public final char oldRepr;

    /**
     * Constructs an instance of Color class with the given new/gold characer
     * representation.
     *
     * @param repr    Character representation.
     * @param oldRepr Old character representation.
     */
    Color(char repr, char oldRepr) {
        this.repr = repr;
        this.oldRepr = oldRepr;
    }

    /**
     * Get the opponent's color based on the given color.
     *
     * @param color Color of the player.
     * @return opponent's color based on the given player's color
     */
    public static Color getOpposingColor(Color color) {
        return switch (color) {
            case GOLD -> SILVER;
            case SILVER -> GOLD;
        };
    }

    /**
     * Get an instance of the color based on the given character representation.
     *
     * @param repr Character representation of the color.
     * @return instance of the color on success, null otherwise
     */
    public static Color fromRepr(char repr) {
        return switch (repr) {
            case 'g', 'w' -> GOLD;
            case 's', 'b' -> SILVER;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case GOLD -> "Gold";
            case SILVER -> "Silver";
        };
    }
}
