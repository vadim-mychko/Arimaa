package cz.cvut.fel.arimaa.types;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Type-safe immutable class for representing steps.
 */
public class Step {

    private static final Pattern STRING_PATTERN
            = Pattern.compile("([rcdhmeRCDHME])([a-h][1-8])([nsew]?)(x?)([shl]?)");

    /**
     * Instance of piece which makes the step.
     */
    public final Piece piece;

    /**
     * Instance of position on a board from where the step is made.
     */
    public final Square from;

    /**
     * Direction in which the step is made. If equals to null, denotes whether
     * the step is creation of a piece.
     */
    public final Direction direction;

    /**
     * Denotes whether the step is removal.
     */
    public final boolean removed;

    /**
     * Type of the made step. It is not a standard PGN format, but rather
     * an extension of it.
     */
    public final StepType type;

    /**
     * Constructs a new instance of step based on its parameters.
     *
     * @param piece     Instance of piece which makes the step.
     * @param from      Instance of position on a board from where the step is made.
     * @param direction Direction in which the step is made. If equals to null,
     *                  denotes whether the step is creation of a piece.
     * @param removed   Denotes whether the step is removal.
     * @param type      Type of the made step. It is not a standard PGN format,
     *                  but rather an extension of it.
     */
    public Step(Piece piece, Square from, Direction direction,
                boolean removed, StepType type) {

        this.piece = piece;
        this.from = from;
        this.direction = direction;
        this.removed = removed;
        this.type = type;
    }

    /**
     * Get an array of steps based on their textual representations in the given
     * array.
     *
     * @param strs Textual representation of steps.
     * @return an array of steps based on their textual representation
     * (an aggregation of fromString() method)
     */
    public static Step[] fromStrings(String[] strs) {
        Step[] steps = new Step[strs.length];
        for (int i = 0; i < strs.length; ++i) {
            steps[i] = fromString(strs[i]);
        }

        return steps;
    }

    /**
     * Get an instance of step based on its textual representation
     * (toString() or getRepr() methods).
     *
     * @param str Textual representation of the step.
     * @return an instance of step based on its textual representation
     * on success, null otherwise
     */
    public static Step fromString(String str) {
        Matcher matcher = STRING_PATTERN.matcher(str);
        if (!matcher.find()) {
            return null;
        }

        Piece piece = Piece.fromRepr(matcher.group(1).charAt(0));
        Square from = Square.getSquare(matcher.group(2));
        Direction direction = matcher.group(3).equals("")
                ? null : Direction.fromRepr(matcher.group(3).charAt(0));
        boolean removed = matcher.group(4).equals("x");
        StepType type = matcher.group(5).equals("")
                ? StepType.SIMPLE : StepType.fromRepr(matcher.group(5).charAt(0));

        return new Step(piece, from, direction, removed, type);
    }

    /**
     * Get the destination square of the step.
     *
     * @return the destination square of the step if is on board, null otherwise
     */
    public Square getDestination() {
        return direction.shift(from);
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, direction, removed, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return removed == step.removed
                && piece.equals(step.piece)
                && from.equals(step.from)
                && direction == step.direction
                && type == step.type;
    }

    @Override
    public String toString() {
        return getRepr() + type.repr;
    }

    /**
     * Get textual description for purpose of logging.
     *
     * @return textual description of the step
     */
    public String getDescription() {
        return type + " step from " + from
                + (direction == null ? "" : " to " + getDestination())
                + (removed ? " with removal" : "");
    }

    /**
     * Get the standard PGN textual representation of the step.
     *
     * @return standard PGN textual representation of the step
     */
    public String getRepr() {
        return "" + piece.getRepr() + from
                + (direction != null ? direction.repr : "")
                + (removed ? "x" : "");
    }
}
