package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Abstract class for representing different pieces (figures) on a board.
 */
public abstract class Piece {

    /**
     * Color of the piece.
     */
    public final Color color;

    /**
     * Constructs an instance of Piece class with the given color.
     *
     * @param color Color of the piece.
     */
    protected Piece(Color color) {
        this.color = color;
    }

    /**
     * Returns an instance of the piece based on its PGN character representation.
     *
     * @param repr PGN character representation.
     * @return instance of the piece based on its PGN character representation
     * on success, null otherwise
     */
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

    /**
     * Get character representation of the piece in PGN format.
     *
     * @return character representation of the piece in PGN format
     */
    public abstract char getRepr();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return getRepr() == piece.getRepr();
    }

    /**
     * Get all valid steps for the piece from the given position on the board
     * in all given directions.
     *
     * @param board      State of the board for getting valid steps.
     * @param from       Position of the piece on the board.
     * @param directions Valid directions for the given piece.
     * @return set of all valid steps for the piece from the given position
     * on the board
     */
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
                Square pushingPos = pushingDirection.shift(shifted);
                if (pushingPos == null || board.isPieceAt(pushingPos)) {
                    continue;
                }

                steps.add(new Step(enemy, shifted, pushingDirection, false, StepType.PUSH));
            }
        }
    }

    /**
     * Check whether this piece is stronger than the given piece.
     *
     * @param piece Piece to be checked for strength.
     * @return true if this piece is stronger than the given piece,
     * false otherwise
     */
    public boolean isStronger(Piece piece) {
        return getStrength() > piece.getStrength();
    }

    /**
     * Get the strength of the piece.
     *
     * @return strength of the piece (the larger, the stronger)
     */
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

    /**
     * Get valid steps based on the given state of the board and position of the
     * piece.
     *
     * @param board State of the board for getting valid steps.
     * @param from  Position of the piece on the board.
     * @return set of all valid steps for the piece from the given position
     * on the board
     */
    public abstract Set<Step> getValidSteps(Board board, Square from);

    @Override
    public String toString() {
        return color.toString() + getClass().getSimpleName();
    }
}
