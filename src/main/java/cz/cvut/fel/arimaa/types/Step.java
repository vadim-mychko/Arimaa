package cz.cvut.fel.arimaa.types;

import java.util.Objects;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

public class Step {

    public final Piece piece;
    public final Square from;
    public final Direction direction;
    public final boolean removed;

    public Step(Piece piece, Square from, Direction direction,
                boolean removed) {

        this.piece = piece;
        this.from = from;
        this.direction = direction;
        this.removed = removed;
    }

    @Override
    public int hashCode() {
        return Objects.hash(piece, from, direction, removed);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Step step = (Step) o;
        return removed == step.removed
               && piece.equals(step.piece)
               && from.equals(step.from)
               && direction == step.direction;
    }

    @Override
    public String toString() {
        return "" + piece.getRepr() + from
               + (direction != null ? direction.repr : "")
               + (removed ? "x" : "");
    }
}
