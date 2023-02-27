package cz.cvut.fel.arimaa.types;

public enum StepType {

    SIMPLE('s'), PUSH('h'), PULL('l');

    public final char repr;

    StepType(char repr) {
        this.repr = repr;
    }

    public static StepType fromRepr(char repr) {
        return switch (repr) {
            case 's' -> SIMPLE;
            case 'h' -> PUSH;
            case 'l' -> PULL;
            default -> null;
        };
    }
}
