package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

class Elephant extends Piece {

    private static final Elephant goldElephant = new Elephant(Color.GOLD);
    private static final Elephant silverElephant = new Elephant(Color.SILVER);

    private Elephant(Color color) {
        super(color);
    }

    static Elephant getInstance(Color color) {
        return color == Color.GOLD ? goldElephant : silverElephant;
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'E' : 'e';
    }

    @Override
    protected int getStrength() {
        return 5;
    }

    @Override
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
