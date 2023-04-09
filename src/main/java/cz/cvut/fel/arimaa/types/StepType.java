package cz.cvut.fel.arimaa.types;

/**
 * Enumeration for tracking what type each step is. It is not standard PGN
 * format, but rather an extension of it.
 */
public enum StepType {

    SIMPLE('s'), PUSH('h'), PULL('l');

    /**
     * Character representation of step type.
     */
    public final char repr;

    /**
     * Constructs a new instance of StepType with the given character
     * representation.
     *
     * @param repr Character representation of the step type.
     */
    StepType(char repr) {
        this.repr = repr;
    }

    /**
     * Get an instance of StepType class based on the given character
     * representation.
     *
     * @param repr Character representation of the StepType class.
     * @return instance of StepType class on success, false otherwise
     */
    public static StepType fromRepr(char repr) {
        return switch (repr) {
            case 's' -> SIMPLE;
            case 'h' -> PUSH;
            case 'l' -> PULL;
            default -> null;
        };
    }

    @Override
    public String toString() {
        return name().toLowerCase();
    }
}
