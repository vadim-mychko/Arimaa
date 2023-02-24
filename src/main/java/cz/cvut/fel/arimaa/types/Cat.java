package cz.cvut.fel.arimaa.types;

class Cat extends Piece {

    Cat(Color color) {
        super(color);
    }

    @Override
    public char getRepr() {
        return color == Color.GOLD ? 'C' : 'c';
    }
}
