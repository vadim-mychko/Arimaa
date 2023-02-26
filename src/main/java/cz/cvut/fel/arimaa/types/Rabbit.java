package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.List;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

class Rabbit extends Piece {

    private static final Direction[] goldDirections
            = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST};
    private static final Direction[] silverDirections
            = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST};

    Rabbit(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'R' : 'r';
    }

    @Override
    protected int getStrength() {
        return 0;
    }

    @Override
    public List<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from,
                color == Color.GOLD ? goldDirections : silverDirections);
    }
}
