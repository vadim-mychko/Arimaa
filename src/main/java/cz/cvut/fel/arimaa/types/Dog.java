package cz.cvut.fel.arimaa.types;

class Dog extends Piece {

    Dog(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'D' : 'd';
    }
}
