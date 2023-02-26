package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.List;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

class Dog extends Piece {

    Dog(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'D' : 'd';
    }

    @Override
    protected int getStrength() {
        return 2;
    }

    @Override
    public List<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
