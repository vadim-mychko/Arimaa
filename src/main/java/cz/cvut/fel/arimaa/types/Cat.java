package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

/**
 * Class for representing cat piece on a board.
 */
class Cat extends Piece {

    private static final Cat goldCat = new Cat(Color.GOLD);
    private static final Cat silverCat = new Cat(Color.SILVER);

    private Cat(Color color) {
        super(color);
    }

    /**
     * Get the (unique) instance of Cat class based on the given color.
     *
     * @param color Color of the cat.
     * @return the (unique) instance of Cat class based on the given color
     */
    static Cat getInstance(Color color) {
        return color == Color.GOLD ? goldCat : silverCat;
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
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
