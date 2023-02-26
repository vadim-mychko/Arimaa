package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.List;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

class Cat extends Piece {

    Cat(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'C' : 'c';
    }

    @Override
    protected int getStrength() {
        return 1;
    }

    @Override
    public List<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
