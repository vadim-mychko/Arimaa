package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.Set;

class Dog extends Piece {

    private static final Dog goldDog = new Dog(Color.GOLD);
    private static final Dog silverDog = new Dog(Color.SILVER);

    private Dog(Color color) {
        super(color);
    }

    static Dog getInstance(Color color) {
        return color == Color.GOLD ? goldDog : silverDog;
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
    public Set<Step> getValidSteps(Board board, Square from) {
        return getValidSteps(board, from, Direction.values());
    }
}
