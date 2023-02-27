package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static cz.cvut.fel.arimaa.types.SquareFactory.Square;
import static cz.cvut.fel.arimaa.types.SquareFactory.getSquare;

public class Board {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;

    static final String EMPTY_BOARD
            = """
               +-----------------+
              8|                 |
              7|                 |
              6|     x     x     |
              5|                 |
              4|                 |
              3|     x     x     |
              2|                 |
              1|                 |
               +-----------------+
                 a b c d e f g h""";

    static final String DEFAULT_BOARD
            = """
               +-----------------+
              8| r r r r r r r r |
              7| d h c e m c h d |
              6|     x     x     |
              5|                 |
              4|                 |
              3|     x     x     |
              2| D H C E M C H D |
              1| R R R R R R R R |
               +-----------------+
                 a b c d e f g h""";

    private static final List<Square> TRAPS = List.of(getSquare("c3"),
                                                      getSquare("f3"), getSquare("c6"), getSquare("f6"));

    private final Piece[][] board;
    private Step previousStep;

    Board() {
        board = new Piece[WIDTH][HEIGHT];
    }

    void load() {
        load(DEFAULT_BOARD);
    }

    boolean load(String positions) {
        reset();
        String[] lines = positions.split("\n");
        if (lines.length != 11
            || !lines[0].equals(" +-----------------+")
            || !lines[9].equals(" +-----------------+")
            || !lines[10].startsWith("   a b c d e f g h")) {
            return false;
        }

        for (int row = 0; row < HEIGHT; ++row) {
            String next = lines[row + 1];
            if (next.length() != 20
                || !next.startsWith((HEIGHT - row) + "|")
                || !next.endsWith(" |")) {
                return false;
            }

            for (int col = 0; col < WIDTH; ++col) {
                boolean isTrap = isTrap(getSquare(col, row));
                char space = next.charAt(2 + 2 * col);
                char repr = next.charAt(3 + 2 * col);
                Piece piece = Piece.fromRepr(repr);

                if (space != ' ' || (piece == null && repr != ' '
                                     && repr != (isTrap ? 'x' : ' '))) {
                    return false;
                }

                board[col][row] = piece;
            }
        }

        return true;
    }

    void reset() {
        previousStep = null;
        for (int row = 0; row < HEIGHT; ++row) {
            for (int col = 0; col < WIDTH; ++col) {
                board[col][row] = null;
            }
        }
    }

    boolean isTrap(Square pos) {
        return TRAPS.contains(pos);
    }

    public boolean isSafeAt(Square pos, Color color) {
        if (!isTrap(pos)) {
            return true;
        }

        boolean safe = false;
        for (Direction direction : Direction.values()) {
            Square shifted = direction.shift(pos);
            if (shifted == null || !isPieceAt(shifted)) {
                continue;
            }

            if (getPieceAt(shifted).color == color) {
                safe = true;
                break;
            }
        }

        return safe;
    }

    public Piece getPieceAt(Square pos) {
        return pos != null ? board[pos.x][pos.y] : null;
    }

    public boolean isPieceAt(Square pos) {
        return pos != null && board[pos.x][pos.y] != null;
    }

    public boolean isFrozenAt(Square pos) {
        if (!isPieceAt(pos)) {
            return false;
        }

        Piece piece = getPieceAt(pos);

        boolean frozen = false;
        for (Direction direction : Direction.values()) {
            Square shifted = direction.shift(pos);
            if (shifted == null || !isPieceAt(shifted)) {
                continue;
            }

            Piece adjacentPiece = getPieceAt(shifted);
            if (adjacentPiece.color == piece.color) {
                frozen = false;
                break;
            } else if (adjacentPiece.isStronger(piece)) {
                frozen = true;
            }
        }

        return frozen;
    }

    boolean makeSteps(Step[] steps) {
        for (Step step : steps) {
            if (!makeStep(step)) {
                return false;
            }
        }

        return true;
    }

    boolean makeStep(Step step) {
        if (!isValidStep(step)) {
            return false;
        }

        if (step.removed) {
            board[step.from.x][step.from.y] = null;
        } else if (step.direction == null) {
            board[step.from.x][step.from.y] = step.piece;
        } else {
            Square shifted = step.direction.shift(step.from);
            board[shifted.x][shifted.y] = board[step.from.x][step.from.y];
            board[step.from.x][step.from.y] = null;
        }

        previousStep = step;

        return true;
    }

    boolean isValidStep(Step step) {
        if (step == null || step.piece == null || step.from == null) {
            return false;
        }

        return step.type == StepType.SIMPLE
                ? isValidSimpleStep(step) : isValidNonSimpleStep(step);
    }

    private boolean isValidSimpleStep(Step step) {
        return getValidSteps(step.from).contains(step);
    }

    Set<Step> getValidSteps(Square from) {
        if (!isPieceAt(from)) {
            return Collections.emptySet();
        }

        return getPieceAt(from).getValidSteps(this, from);
    }

    private boolean isValidNonSimpleStep(Step step) {
        Set<Step> validSteps = new HashSet<>();
        Square lookAt = step.type == StepType.PULL
                ? step.getDestination() : step.from;

        for (Direction direction : Direction.values()) {
            Square shifted = direction.shift(lookAt);
            Piece enemy = getPieceAt(shifted);
            if (enemy == null
                || step.piece.color == enemy.color
                || !enemy.isStronger(step.piece)) {
                continue;
            }

            validSteps.addAll(getValidSteps(shifted));
        }

        return validSteps.contains(step);
    }

    public Step getPreviousStep() {
        return previousStep;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(EMPTY_BOARD);
        for (int row = 0; row < HEIGHT; ++row) {
            for (int col = 0; col < WIDTH; ++col) {
                if (board[col][row] == null) {
                    continue;
                }

                int index = 24 + 21 * row + 2 * col;
                builder.setCharAt(index, board[col][row].getRepr());
            }
        }

        return builder.toString();
    }
}
