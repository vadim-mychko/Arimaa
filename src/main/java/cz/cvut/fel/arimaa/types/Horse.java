package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

/**
 * Class for representing horse piece on a board.
 */
class Horse extends Piece {

    private static final Horse goldHorse = new Horse(Color.GOLD);
    private static final Horse silverHorse = new Horse(Color.SILVER);

    private Horse(Color color) {
        super(color);
    }

    /**
     * Get the (unique) instance of Horse class based on the given color.
     *
     * @param color Color of the horse.
     * @return the (unique) instance of Horse class based on the given color
     */
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
