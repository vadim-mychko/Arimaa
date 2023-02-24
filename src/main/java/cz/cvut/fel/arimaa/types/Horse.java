package cz.cvut.fel.arimaa.types;

class Horse extends Piece {

    Horse(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'H' : 'h';
    }
}
