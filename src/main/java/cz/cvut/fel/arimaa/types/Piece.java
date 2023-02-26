package cz.cvut.fel.arimaa.types;

import cz.cvut.fel.arimaa.model.Board;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;

public abstract class Piece {

    public final Color color;

    protected Piece(Color color) {
        this.color = color;
    }

    public static Piece fromRepr(char repr) {
        Color color = Character.isUpperCase(repr) ? Color.GOLD : Color.SILVER;

        return switch (Character.toLowerCase(repr)) {
            case 'e' -> new Elephant(color);
            case 'm' -> new Camel(color);
            case 'h' -> new Horse(color);
            case 'd' -> new Dog(color);
            case 'c' -> new Cat(color);
            case 'r' -> new Rabbit(color);
            default -> null;
        };
    }

    @Override
    public int hashCode() {
        return Objects.hash(getRepr());
    }

    public abstract char getRepr();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Piece piece = (Piece) o;
        return getRepr() == piece.getRepr();
    }

    public boolean isStronger(Piece piece) {
        return getStrength() > piece.getStrength();
    }

    protected abstract int getStrength();

    protected List<Step> getValidSteps(Board board, Square from,
                                       Direction[] directions) {
        
        List<Step> steps = new ArrayList<>();
        return steps;
    }

    public abstract List<Step> getValidSteps(Board board, Square from);
}
