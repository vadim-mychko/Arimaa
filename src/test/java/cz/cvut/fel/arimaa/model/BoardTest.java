package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Step;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static cz.cvut.fel.arimaa.types.Square.getSquare;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {

    @Test
    public void testLoadEmptyBoard() {
        assertEquals(Board.EMPTY_BOARD, new Board().toString());
    }

    @Test
    public void testLoadDefaultBoard() {
        Board board = new Board();
        board.load();

        assertEquals(Board.DEFAULT_BOARD, board.toString());
    }

    @Test
    public void testGettingValidSimpleSteps() {
        Board board = new Board();
        board.load();

        Set<Step> steps = board.getValidSteps(getSquare("c2"));
        Step expected = Step.fromString("Cc2ns");

        assertEquals(Set.of(expected), steps);
    }

    @Test
    public void testMakingValidSteps() {
        Board board = new Board();
        board.load();
        String[] stepStrs = {"Ed2ns", "Ed3ws", "Ec3ns", "Ec4ns", "Ec5ws",
                "Eb5ns", "Eb6ss", "hb7sl", "hb6eh", "Eb5ns"};
        Step[] steps = Step.fromStrings(stepStrs);
        for (Step step : steps) {
            board.makeStep(step);
        }

        String expected
                = """
                 +-----------------+
                8| r r r r r r r r |
                7| d   c e m c h d |
                6|   E h     x     |
                5|                 |
                4|                 |
                3|     x     x     |
                2| D H C   M C H D |
                1| R R R R R R R R |
                 +-----------------+
                   a b c d e f g h""";

        assertEquals(expected, board.toString());
    }
}
