package cz.cvut.fel.arimaa.types;

class Rabbit extends Piece {

    Rabbit(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'R' : 'r';
    }
}
