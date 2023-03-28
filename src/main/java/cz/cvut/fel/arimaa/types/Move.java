package cz.cvut.fel.arimaa.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Move {

    public static final int MAX_LEGAL_STEPS = 4;

    private List<Step> steps;

    public Move() {
        steps = new ArrayList<>();
    }

    public Move(List<Step> steps) {
        this();
        this.steps.addAll(steps);
    }

    public Step getLastStep() {
        return hasSteps() ? steps.get(steps.size() - 1) : null;
    }

    public void addStep(Step step) {
        if (step != null) {
            steps.add(step);
        }
    }

    public int getNumberOfSteps() {
        return steps.size();
    }

    public boolean hasSteps() {
        return !steps.isEmpty();
    }

    public Step getStep(int index) {
        return index >= 0 && index < steps.size() ? steps.get(index) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Move move = (Move) o;
        return steps.equals(move.steps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(steps);
    }

    @Override
    public String toString() {
        if (steps.isEmpty()) {
            return "";
        }

        int numberOfSteps = steps.size();
        StringBuilder repr = new StringBuilder(steps.get(0).toString());
        for (int i = 1; i < numberOfSteps; ++i) {
            repr.append(" ").append(steps.get(i));
        }

        return repr.toString();
    }
}
