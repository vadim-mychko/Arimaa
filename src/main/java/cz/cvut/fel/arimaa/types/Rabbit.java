package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

/**
 * Class for representing rabbit piece on a board.
 */
class Rabbit extends Piece {

    private static final Direction[] goldDirections
            = new Direction[]{Direction.NORTH, Direction.EAST, Direction.WEST};
    private static final Direction[] silverDirections
            = new Direction[]{Direction.SOUTH, Direction.EAST, Direction.WEST};

    private static final Rabbit goldRabbit = new Rabbit(Color.GOLD);
    private static final Rabbit silverRabbit = new Rabbit(Color.SILVER);

    private Rabbit(Color color) {
        super(color);
    }

    /**
     * Get the (unique) instance of Rabbit class based on the given color.
     *
     * @param color Color of the rabbit.
     * @return the (unique) instance of Rabbit class based on the given color
     */
    static Rabbit getInstance(Color color) {
        return color == Color.GOLD ? goldRabbit : silverRabbit;
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
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from,
                color == Color.GOLD ? goldDirections : silverDirections);
    }
}
