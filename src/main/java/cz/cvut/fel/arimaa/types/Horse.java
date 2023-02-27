package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

class Horse extends Piece {

    private static final Horse goldHorse = new Horse(Color.GOLD);
    private static final Horse silverHorse = new Horse(Color.SILVER);

    private Horse(Color color) {
        super(color);
    }

    static Horse getInstance(Color color) {
        return color == Color.GOLD ? goldHorse : silverHorse;
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'H' : 'h';
    }

    @Override
    protected int getStrength() {
        return 3;
    }

    @Override
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
