package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public abstract class Piece {

    public final Color color;

    protected Piece(Color color) {
        this.color = color;
    }

    public static Piece fromRepr(char repr) {
        Color color = Character.isUpperCase(repr) ? Color.GOLD : Color.SILVER;

        return switch (Character.toLowerCase(repr)) {
            case 'e' -> Elephant.getInstance(color);
            case 'm' -> Camel.getInstance(color);
            case 'h' -> Horse.getInstance(color);
            case 'd' -> Dog.getInstance(color);
            case 'c' -> Cat.getInstance(color);
            case 'r' -> Rabbit.getInstance(color);
            default -> null;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRepr());
    }

    public abstract char getRepr();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return getRepr() == piece.getRepr();
    }

    protected Set<Step> getValidSteps(Board board, Square from,
                                      Direction[] directions) {

        Set<Step> steps = new HashSet<>();
        addSimpleSteps(board, from, directions, steps);
        addPushSteps(board, from, directions, steps);
        addPullSteps(board, from, directions, steps);

        return steps;
    }

    private void addSimpleSteps(Board board, Square from,
                                Direction[] directions, Set<Step> steps) {

        if (board.isFrozenAt(from)) {
            return;
        }

        Step previousStep = board.getPreviousStep();
        if (previousStep != null
                && previousStep.type == StepType.PUSH
                && from.isAdjacentTo(previousStep.from)) {

            Direction direction = Direction.getDirection(from, previousStep.from);
            steps.add(new Step(this, from, direction, false, StepType.SIMPLE));

            return;
        }

        for (Direction direction : directions) {
            Square shifted = direction.shift(from);
            if (shifted == null) {
                continue;
            }

            if (!board.isPieceAt(shifted)) {
                steps.add(new Step(this, from, direction, false, StepType.SIMPLE));
            }
        }
    }

    private void addPushSteps(Board board, Square from,
                              Direction[] directions, Set<Step> steps) {

        Step previousStep = board.getPreviousStep();
        if (board.isFrozenAt(from)
                || (previousStep != null
                && board.getPreviousStep().type == StepType.PUSH)) {
            return;
        }

        for (Direction direction : directions) {
            Square shifted = direction.shift(from);
            Piece enemy = board.getPieceAt(shifted);

            if (enemy == null || color == enemy.color || !isStronger(enemy)) {
                continue;
            }

            for (Direction pushingDirection : directions) {
                Square pushingPos = direction.shift(shifted);
                if (pushingPos == null || board.isPieceAt(pushingPos)) {
                    continue;
                }

                steps.add(new Step(enemy, shifted, pushingDirection, false, StepType.PUSH));
            }
        }
    }

    public boolean isStronger(Piece piece) {
        return getStrength() > piece.getStrength();
    }

    protected abstract int getStrength();

    private void addPullSteps(Board board, Square from,
                              Direction[] directions, Set<Step> steps) {

        Step previousStep = board.getPreviousStep();
        if (previousStep == null
                || board.isPieceAt(previousStep.from)
                || previousStep.type == StepType.PUSH
                || previousStep.type == StepType.PULL
                || !previousStep.getDestination().equals(from)) {
            return;
        }

        for (Direction direction : directions) {
            Square shifted = direction.shift(previousStep.from);
            Piece enemy = board.getPieceAt(shifted);
            if (enemy == null
                    || color == enemy.color
                    || !isStronger(enemy)) {
                continue;
            }

            Direction pullingDirection =
                    Direction.getOppositeDirection(direction);

            steps.add(new Step(enemy, shifted, pullingDirection, false, StepType.PULL));
        }
    }

    public abstract Set<Step> getValidSteps(Board board, Square from);

    @Override
    public String toString() {
        return color.toString() + getClass().getSimpleName();
    }
}
