package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

/**
 * Class for representing elephant piece on a board.
 */
class Elephant extends Piece {

    private static final Elephant goldElephant = new Elephant(Color.GOLD);
    private static final Elephant silverElephant = new Elephant(Color.SILVER);

    /**
     * Get the (unique) instance of Elephant class based on the given color.
     *
     * @param color Color of the elephant.
     * @return the (unique) instance of Elephant class based on the given color
     */
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
