package cz.cvut.fel.arimaa.types;

import java.util.Arrays;

public class Move {

    public static final int MAX_STEPS = 4;
    public final Step[] steps;

    public Move() {
        steps = new Step[MAX_STEPS];
    }

    public Move(Step first) {
        this();
        steps[0] = first;
    }

    public Move(Step first, Step second) {
        this(first);
        steps[1] = second;
    }

    public Move(Step first, Step second, Step third) {
        this(first, second);
        steps[2] = third;
    }

    public Move(Step first, Step second, Step third, Step fourth) {
        this(first, second, third);
        steps[3] = fourth;
    }

    public Move(Step[] steps) {
        this();
        int length = Math.min(4, steps.length);
        System.arraycopy(steps, 0, this.steps, 0, length);
    }

    public boolean hasSteps() {
        return steps[0] != null;
    }

    public Step getLast() {
        for (int i = MAX_STEPS - 1; i >= 0; --i) {
            if (steps[i] != null) {
                return steps[i];
            }
        }

        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return Arrays.equals(steps, move.steps);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(steps);
    }

    @Override
    public String toString() {
        if (steps[0] == null) {
            return "";
        }

        StringBuilder str = new StringBuilder(steps[0].toString());
        for (int i = 1; i < MAX_STEPS; ++i) {
            if (steps[i] != null) {
                str.append(" ").append(steps[i]);
            }
        }

        return str.toString();
    }
}
