package cz.cvut.fel.arimaa.types;

public enum Color {

    GOLD('g', 'w'), SILVER('s', 'b');

    public final char repr;
    public final char oldRepr;

    Color(char repr, char oldRepr) {
        this.repr = repr;
        this.oldRepr = oldRepr;
    }

    public static Color getOpposingColor(Color color) {
        return switch (color) {
            case GOLD -> SILVER;
            case SILVER -> GOLD;
        };
    }

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
