package cz.cvut.fel.arimaa.types;

public enum Color {

    GOLD('g'), SILVER('s');

    public final char repr;

    Color(char repr) {
        this.repr = repr;
    }

    public static Color getOpposingColor(Color color) {
        return switch (color) {
            case GOLD -> SILVER;
            case SILVER -> GOLD;
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
