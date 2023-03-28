package cz.cvut.fel.arimaa.types;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Step {

    public final Piece piece;
    public final Square from;
    public final Direction direction;
    public final boolean removed;
    public final StepType type;

    public Step(Piece piece, Square from, Direction direction,
                boolean removed, StepType type) {

        this.piece = piece;
        this.from = from;
        this.direction = direction;
        this.removed = removed;
        this.type = type;
    }

    public static Step[] fromStrings(String[] strs) {
        Step[] steps = new Step[strs.length];
        for (int i = 0; i < strs.length; ++i) {
            steps[i] = fromString(strs[i]);
        }

        return steps;
    }

    public static Step fromString(String str) {
        Pattern pattern = Pattern.compile(
                "([rcdhmeRCDHME])([a-h][1-8])([nsew]?)(x?)([shl])");
        Matcher matcher = pattern.matcher(str);
        if (!matcher.find()) {
            return null;
        }

        Piece piece = Piece.fromRepr(matcher.group(1).charAt(0));
        Square from = Square.getSquare(matcher.group(2));
        Direction direction = matcher.group(3).equals("")
                ? null : Direction.fromRepr(matcher.group(3).charAt(0));
        boolean removed = matcher.group(4).equals("x");
        StepType type = StepType.fromRepr(matcher.group(5).charAt(0));

        return new Step(piece, from, direction, removed, type);
    }

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

    public String getDescription() {
        return "";
    }

    public String getRepr() {
        return "" + piece.getRepr() + from
                + (direction != null ? direction.repr : "")
                + (removed ? "x" : "");
    }
}
