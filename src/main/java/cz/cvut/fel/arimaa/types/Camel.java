package cz.cvut.fel.arimaa.types;

class Camel extends Piece {

    Camel(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'M' : 'm';
    }
}
