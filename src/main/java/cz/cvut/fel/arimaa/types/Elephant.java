package cz.cvut.fel.arimaa.types;

class Elephant extends Piece {

    Elephant(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'E' : 'e';
    }
}
