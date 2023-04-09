package cz.cvut.fel.arimaa.types;

/**
 * Class for representing game results.
 */
public enum GameResult {

    GOLD_WIN, SILVER_WIN, NONE;

    /**
     * Get an instance of game result based on the color of the winner.
     *
     * @param color Color of the winner.
     * @return instance of game result
     */
    public static GameResult fromColor(Color color) {
        if (color == null) {
            return NONE;
        }

        return switch (color) {
            case GOLD -> GOLD_WIN;
            case SILVER -> SILVER_WIN;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case GOLD_WIN -> "Gold wins";
            case SILVER_WIN -> "Silver wins";
            case NONE -> "None";
        };
    }
}
