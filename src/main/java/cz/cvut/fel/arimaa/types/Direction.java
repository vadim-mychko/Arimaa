package cz.cvut.fel.arimaa.types;

public enum Direction {

    NORTH('n'), SOUTH('s'), EAST('e'), WEST('w');

    public final char repr;

    Direction(char repr) {
        this.repr = repr;
    }

    public static Direction getOpposingDirection(Direction direction) {
        return switch (direction) {
            case NORTH -> SOUTH;
            case SOUTH -> NORTH;
            case EAST -> WEST;
            case WEST -> EAST;
        };
    }
}
