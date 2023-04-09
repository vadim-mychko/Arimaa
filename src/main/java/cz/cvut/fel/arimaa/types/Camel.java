package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

/**
 * Class for representing camel piece on a board.
 */
class Camel extends Piece {

    private static final Camel goldCamel = new Camel(Color.GOLD);
    private static final Camel silverCamel = new Camel(Color.SILVER);

    private Camel(Color color) {
        super(color);
    }

    /**
     * Get the (unique) instance of Camel class based on the given color.
     *
     * @param color Color of the camel.
     * @return the (unique) instance of Camel class based on the given color
     */
    static Camel getInstance(Color color) {
        return color == Color.GOLD ? goldCamel : silverCamel;
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'M' : 'm';
    }

    @Override
    protected int getStrength() {
        return 4;
    }

    @Override
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
