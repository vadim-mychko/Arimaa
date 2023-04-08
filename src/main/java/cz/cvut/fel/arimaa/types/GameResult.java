package cz.cvut.fel.arimaa.types;

public enum GameResult {

    GOLD_WIN, SILVER_WIN, NONE;

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
