package cz.cvut.fel.arimaa.types;

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
    public String toString() {
        return "" + piece.getRepr() + from
                + (direction != null ? direction.repr : "")
                + (removed ? "x" : "");
    }
}
