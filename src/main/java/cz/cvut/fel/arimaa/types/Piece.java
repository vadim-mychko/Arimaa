package cz.cvut.fel.arimaa.types;

public abstract class Piece {

    public final Color color;

    protected Piece(Color color) {
        this.color = color;
    }

    public static Piece fromRepr(char repr) {
        Color color = Character.isUpperCase(repr) ? Color.GOLD : Color.SILVER;

        return switch(Character.toLowerCase(repr)) {
            case 'e' -> new Elephant(color);
            case 'm' -> new Camel(color);
            case 'h' -> new Horse(color);
            case 'd' -> new Dog(color);
            case 'c' -> new Cat(color);
            case 'r' -> new Rabbit(color);
            default  -> null;
        };
    }
}
