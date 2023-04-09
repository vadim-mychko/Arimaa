package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import static cz.cvut.fel.arimaa.types.Square.getSquare;

/**
 * Class for representing board holding arimaa pieces & manipulating them.
 * Probably should have been merged with Game class...
 */
public class Board {

    /**
     * Width of the board.
     */
    public static final int WIDTH = 8;

    /**
     * Height of the board.
     */
    public static final int HEIGHT = 8;

    /**
     * Textual representation of the empty board.
     */
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

    /**
     * Textual representation of the default board.
     */
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

    private static final Logger logger = Logger.getLogger(Board.class.getName());

    private Piece[][] board;
    private ObservableList<Move> moves;

    /**
     * Constructs an instance of Board class.
     * For working properly, load() has to be called too.
     */
    Board() {
        board = new Piece[WIDTH][HEIGHT];
        moves = FXCollections.observableArrayList();
    }

    /**
     * Check if the given position on a board is trap.
     *
     * @param pos Position on the board.
     * @return true if the given position on a board is trap, false otherwise
     */
    public static boolean isTrap(Square pos) {
        return TRAPS.contains(pos);
    }

    /**
     * Sets pieces based on default board textual representation.
     */
    void load() {
        load(DEFAULT_BOARD);
    }

    /**
     * Undoes the previous step if possible.
     *
     * @return true if the previous step is undone, false otherwise
     */
    boolean undoStep() {
        Move lastMove = getLastMove();
        if (moves.size() <= 3 && lastMove.getNumberOfSteps() <= 0) {
            return false;
        }

        if (lastMove.getNumberOfSteps() <= 0) {
            moves.remove(moves.size() - 1);
            lastMove = getLastMove();
        }

        Step lastStep = lastMove.getStep(lastMove.getNumberOfSteps() - 1);

        if (lastStep.removed) {
            Square from = lastStep.from;
            board[from.x][from.y] = lastStep.piece;
            lastMove.removeLastStep();
            lastStep = lastMove.getStep(lastMove.getNumberOfSteps() - 1);
        }

        Square from = lastStep.from;
        Square to = lastStep.getDestination();
        Piece piece = board[to.x][to.y];
        board[to.x][to.y] = null;
        board[from.x][from.y] = piece;
        lastMove.removeLastStep();

        moves.set(moves.size() - 1, lastMove);

        return true;
    }

    /**
     * Sets pieces based on the given textual representation.
     *
     * @param positions Textual representation of the board to be loaded.
     * @return true if is loaded, false otherwise
     */
    boolean load(String positions) {
        reset();
        String[] lines = positions.split("\n");
        if (lines.length != 11
                || !lines[0].equals(" +-----------------+")
                || !lines[9].equals(" +-----------------+")
                || !lines[10].startsWith("   a b c d e f g h")) {
            return false;
        }

        for (int y = 0; y < HEIGHT; ++y) {
            String next = lines[y + 1];
            if (next.length() != 20
                    || !next.startsWith((HEIGHT - y) + "|")
                    || !next.endsWith(" |")) {
                return false;
            }

            for (int x = 0; x < WIDTH; ++x) {
                boolean isTrap = isTrap(getSquare(x, y));
                char space = next.charAt(2 + 2 * x);
                char repr = next.charAt(3 + 2 * x);
                Piece piece = Piece.fromRepr(repr);

                if (space != ' ' || (piece == null && repr != ' '
                        && repr != (isTrap ? 'x' : ' '))) {
                    return false;
                }

                board[x][y] = piece;
            }
        }

        setInitialPhaseSteps();

        return true;
    }

    private void setInitialPhaseSteps() {
        Move goldArrangement = new Move();
        Move silverArrangement = new Move();

        for (int y = HEIGHT - 1; y >= 0; --y) {
            for (int x = 0; x < WIDTH; ++x) {
                Square square = Square.getSquare(x, y);
                Piece piece = getPieceAt(square);
                if (piece == null) {
                    continue;
                }

                Move arrangement = piece.color == Color.GOLD
                        ? goldArrangement : silverArrangement;
                arrangement.addStep(new Step(piece, square, null, false, StepType.SIMPLE));
            }
        }

        moves.set(0, goldArrangement);
        moves.set(1, silverArrangement);
    }

    /**
     * Removes all pieces from the board and resets its move list.
     */
    void reset() {
        moves.clear();
        moves.addAll(new Move(), new Move(), new Move());
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                board[x][y] = null;
            }
        }
    }

    /**
     * Check if piece of the given color would be safe on the given position on
     * the board.
     *
     * @param pos   Position on the board to checked.
     * @param color Color of the piece to be checked.
     * @return true if piece of the given color would be safe on the given
     * position on the board, false otherwise
     */
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

    /**
     * Get number of recorded moves.
     *
     * @return number of recorded moves
     */
    public int getNumberOfMoves() {
        return moves.size();
    }

    /**
     * Get an instance of the piece on the given position on the board.
     *
     * @param pos Position of the piece on the board.
     * @return an instance of the piece on the given position on the board if
     * there is a piece at the given position, null otherwise
     */
    public Piece getPieceAt(Square pos) {
        return pos != null ? board[pos.x][pos.y] : null;
    }

    /**
     * Check if there is any piece at the given position on the board.
     *
     * @param pos Position to be checked.
     * @return true if there is any piece at the given position on the board,
     * false otherwise
     */
    public boolean isPieceAt(Square pos) {
        return pos != null && board[pos.x][pos.y] != null;
    }

    /**
     * Swap pieces of the same given color on the given positions on the board.
     *
     * @param from  Position of a piece to be swapped.
     * @param to    Position of a piece to be swapped.
     * @param color Color of each piece to be swapped.
     * @return true if pieces are swapped, false otherwise
     */
    boolean swapPieces(Square from, Square to, Color color) {
        Piece left = getPieceAt(from);
        Piece right = getPieceAt(to);
        if (left == null || right == null
                || left.color != color || right.color != color) {
            return false;
        }

        board[from.x][from.y] = right;
        board[to.x][to.y] = left;

        setInitialPhaseSteps();

        return true;
    }

    /**
     * Check if the piece at the given position on the board is frozen.
     *
     * @param pos Position of the piece to be checked.
     * @return true if the piece at the given position on the board is frozen,
     * false otherwise
     */
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

    /**
     * Get observable list of made moves.
     *
     * @return observable list of made moves
     */
    ObservableList<Move> getMoves() {
        return moves;
    }

    private void makeRemovedSteps() {
        for (Square trap : TRAPS) {
            Piece piece = getPieceAt(trap);
            if (piece == null) {
                continue;
            }

            if (!isSafeAt(trap, piece.color)) {
                Step step = new Step(piece, trap, null, true, StepType.SIMPLE);
                addStepToMoves(step);
                board[trap.x][trap.y] = null;
                logger.info("Made " + step.getDescription());
            }
        }
    }

    private void addStepToMoves(Step step) {
        Move lastMove = getLastMove();
        lastMove.addStep(step);
        moves.set(moves.size() - 1, lastMove);
    }

    /**
     * Make the given step to the board.
     *
     * @param step Step to be made.
     * @return true if step is made, false otherwise
     */
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

        logger.info("Made " + step.getDescription());
        addStepToMoves(step);
        makeRemovedSteps();

        return true;
    }

    /**
     * Add the given piece at the given position on the board.
     *
     * @param piece Piece to be added to the board.
     * @param pos   Position of the piece on the board.
     */
    void addPieceAt(Piece piece, Square pos) {
        board[pos.x][pos.y] = piece;
        Step step = new Step(piece, pos, null, false, StepType.SIMPLE);
        int index = piece.color == Color.GOLD ? 0 : 1;
        Move setupMove = moves.get(index);
        setupMove.addStep(step);
        moves.set(index, setupMove);
    }

    private boolean isValidStep(Step step) {
        if (step == null || step.piece == null || step.from == null) {
            return false;
        }

        if (step.type == StepType.SIMPLE && step.removed) {
            return true;
        }

        return step.type == StepType.SIMPLE
                ? isValidSimpleStep(step) : isValidNonSimpleStep(step);
    }

    private boolean isValidSimpleStep(Step step) {
        return step.direction == null && !step.removed
                || getValidSteps(step.from).contains(step);
    }

    /**
     * Get valid steps for the given pieces' color.
     *
     * @param color Color of pieces.
     * @return set of all valid steps for the given pieces' color
     */
    public Set<Step> getValidSteps(Color color) {
        Set<Step> validSteps = new HashSet<>();
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                if (board[x][y] != null && board[x][y].color == color) {
                    validSteps.addAll(getValidSteps(getSquare(x, y)));
                }
            }
        }

        Step previousStep = getPreviousStep();
        if (previousStep != null && previousStep.type == StepType.PUSH) {
            validSteps.removeIf(step ->
                    !step.getDestination().equals(previousStep.from));
        }

        return validSteps;
    }

    /**
     * Get all valid steps for the piece at the given position.
     *
     * @param from Position of the piece on the board.
     * @return set of all valid steps for the piece at the given position
     */
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

    /**
     * Finish making steps (for the purpose of distinguishing between different
     * players).
     */
    void finishMakingMove() {
        moves.add(new Move());
    }

    /**
     * Get last made move.
     *
     * @return last made move
     */
    Move getLastMove() {
        return moves.isEmpty() ? null : moves.get(moves.size() - 1);
    }

    /**
     * Get previous non-removal step.
     *
     * @return previous non-removal step if any exists, null otherwise
     */
    public Step getPreviousStep() {
        Move lastMove = getLastMove();
        for (int i = lastMove.getNumberOfSteps() - 1; i >= 0; --i) {
            Step step = lastMove.getStep(i);
            if (!step.removed) {
                return step;
            }
        }

        return null;
    }

    private boolean rabbitReachedGoal(Color color) {
        int goal = color == Color.GOLD ? 0 : HEIGHT - 1;
        char repr = color == Color.GOLD ? 'R' : 'r';
        for (int x = 0; x < WIDTH; ++x) {
            if (board[x][goal] != null && board[x][goal].getRepr() == repr) {
                return true;
            }
        }

        return false;
    }

    private boolean lostAllRabbits(Color color) {
        char repr = color == Color.GOLD ? 'R' : 'r';
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                if (board[x][y] != null && board[x][y].getRepr() == repr) {
                    return false;
                }
            }
        }

        return true;
    }

    private boolean hasPossibleSteps(Color color) {
        return !getValidSteps(color).isEmpty();
    }

    /**
     * Get game result based on the current board's state and color of the
     * previous player.
     *
     * @param player Color of previous player.
     * @return game result based on the current board's state and color of the
     * previous player
     */
    GameResult getGameResult(Color player) {
        Color opponent = Color.getOpposingColor(player);
        GameResult playerWins = GameResult.fromColor(player);
        GameResult opponentWins = GameResult.fromColor(opponent);

        if (rabbitReachedGoal(player)) {
            return playerWins;
        } else if (rabbitReachedGoal(opponent)) {
            return opponentWins;
        } else if (lostAllRabbits(opponent)) {
            return playerWins;
        } else if (lostAllRabbits(player)) {
            return opponentWins;
        } else if (!hasPossibleSteps(opponent)) {
            return playerWins;
        }

        return GameResult.NONE;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(EMPTY_BOARD);
        for (int y = 0; y < HEIGHT; ++y) {
            for (int x = 0; x < WIDTH; ++x) {
                if (board[x][y] == null) {
                    continue;
                }

                int index = 24 + 21 * y + 2 * x;
                builder.setCharAt(index, board[x][y].getRepr());
            }
        }

        return builder.toString();
    }
}
