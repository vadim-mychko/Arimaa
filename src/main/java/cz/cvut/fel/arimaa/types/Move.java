package cz.cvut.fel.arimaa.types;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Class for representing single move (multiple steps) on a board.
 */
public class Move {

    /**
     * Maximum number of valid steps for either player.
     */
    public static final int MAX_LEGAL_STEPS = 4;

    private List<Step> steps;

    /**
     * Constructs an instance of move with zero steps.
     */
    public Move() {
        steps = new ArrayList<>();
    }

    /**
     * Add the given step to the move.
     *
     * @param step Step to be added.
     */
    public void addStep(Step step) {
        steps.add(step);
    }

    /**
     * Remove the last step in the move if possible.
     *
     * @return true if move has any steps to remove, false otherwise
     */
    public boolean removeLastStep() {
        if (steps.isEmpty()) {
            return false;
        }

        steps.remove(steps.size() - 1);
        return true;
    }

    /**
     * Get number of non-removal steps in the move.
     *
     * @return number of non-removal steps in the move
     */
    public int getNumberOfNonRemovalSteps() {
        return (int) steps.stream().filter(step -> !step.removed).count();
    }

    /**
     * Get total number of steps in the move.
     *
     * @return total number of steps in the move
     */
    public int getNumberOfSteps() {
        return steps.size();
    }

    /**
     * Check if the move has any steps.
     *
     * @return true if the move has any steps, false otherwise
     */
    public boolean hasSteps() {
        return !steps.isEmpty();
    }

    /**
     * Get the step by the given index.
     *
     * @param index Index of the step in the move (0-indexed).
     * @return the step by the given index if index is in bounds, null otherwise
     */
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
        StringBuilder repr = new StringBuilder(steps.get(0).getRepr());
        for (int i = 1; i < numberOfSteps; ++i) {
            repr.append(" ").append(steps.get(i).getRepr());
        }

        return repr.toString();
    }
}
