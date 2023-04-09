package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;
import javafx.collections.ObservableList;

/**
 * Class for playing the Arimaa game (with all rules, change of turn, clock,
 * different game types, etc.).
 * Probably should have been merged with Board class...
 */
public class Game {

    private Board board;
    private Color currentPlayer;
    private int numberOfSteps;
    private Engine engine;
    private GameType gameType;
    private Clock clock;
    private boolean isInitialPhase;
    private boolean running;

    /**
     * Constructs an instance of Game with default board.
     */
    public Game() {
        board = new Board();
        board.load();
        numberOfSteps = 0;
        currentPlayer = Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_HUMAN;
        clock = new Clock();
        isInitialPhase = true;
        running = true;
    }

    /**
     * Constructs an instance of Game with the given board.
     *
     * @param board Board state for constructing Game instance.
     */
    public Game(Board board) {
        this.board = board;
        numberOfSteps = board.getLastMove().getNumberOfNonRemovalSteps();
        currentPlayer = board.getNumberOfMoves() % 2 == 0
                ? Color.SILVER : Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_HUMAN;
        clock = new Clock();
        isInitialPhase = board.getNumberOfMoves() <= 2;
        running = true;
    }

    /**
     * Get number of steps current player has made so far.
     *
     * @return number of steps current player has made so far.
     */
    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    /**
     * Undoes previously made step.
     *
     * @return true if the previous step is undone, false otherwise
     */
    public boolean undoStep() {
        if (!board.undoStep()) {
            return false;
        }

        if (numberOfSteps <= 0) {
            numberOfSteps = board.getLastMove().getNumberOfNonRemovalSteps();
            currentPlayer = Color.getOpposingColor(currentPlayer);
            clock.switchPlayer();
        } else {
            --numberOfSteps;
        }

        return true;
    }

    /**
     * Reset the game, load the default board.
     */
    public void reset() {
        board.load();
        numberOfSteps = 0;
        currentPlayer = Color.GOLD;
        clock.reset();
        isInitialPhase = true;
        running = true;
    }

    /**
     * Get number of seconds player of the given color has spent on making
     * steps.
     *
     * @param color Color of the player for getting elapsed time.
     * @return number of seconds player of the given color has spent on making
     * steps
     */
    public int getTimeElapsed(Color color) {
        return clock.getTimeElapsed(color);
    }

    /**
     * Finish making steps for the current player. Next player is ought to make
     * steps.
     *
     * @return true if current's player turn is finished, false otherwise
     */
    public boolean finishMakingSteps() {
        if (isInitialPhase) {
            if (gameType == GameType.HUMAN_COMPUTER
                    || currentPlayer == Color.SILVER) {
                isInitialPhase = false;
            }

            if (gameType == GameType.HUMAN_HUMAN) {
                currentPlayer = Color.getOpposingColor(currentPlayer);
                clock.switchPlayer();
            }

            return true;
        }

        if (!running || numberOfSteps <= 0
                || board.getPreviousStep().type == StepType.PUSH) {
            return false;
        }

        numberOfSteps = 0;
        board.finishMakingMove();
        clock.switchPlayer();

        if (!isInitialPhase && gameType == GameType.HUMAN_COMPUTER) {
            Color engineColor = Color.getOpposingColor(currentPlayer);
            engine.makeMove(board, engineColor);
            board.finishMakingMove();
            clock.switchPlayer();
        } else {
            currentPlayer = Color.getOpposingColor(currentPlayer);
        }

        return true;
    }

    /**
     * Get game type.
     *
     * @return game type
     */
    public GameType getGameType() {
        return gameType;
    }

    /**
     * Set game type to the given one.
     *
     * @param gameType Game type to be set.
     */
    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    /**
     * Check if player has to arrange pieces.
     *
     * @return true if player has to arrange pieces, false otherwise
     */
    public boolean isInitialPhase() {
        return isInitialPhase;
    }

    /**
     * Make step from the given source to the given destination.
     *
     * @param from Source of the step.
     * @param to   Destination of the step.
     * @return true if step is made, false otherwise
     */
    public boolean makeStep(Square from, Square to) {
        if (!running) {
            return false;
        }

        if (isInitialPhase) {
            board.swapPieces(from, to, currentPlayer);
            return true;
        }

        Step nextStep = board.getValidSteps(currentPlayer).stream()
                .filter(step -> step.from.equals(from)
                        && step.getDestination().equals(to))
                .findFirst()
                .orElse(null);

        if (nextStep == null || numberOfSteps >= 4
                || (numberOfSteps == 3 && nextStep.type == StepType.PUSH)) {
            return false;
        }

        board.makeStep(nextStep);
        ++numberOfSteps;

        return true;
    }

    /**
     * Get color of the current's player.
     *
     * @return color of the current's player
     */
    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    /**
     * Get observable list of made moves.
     *
     * @return observable list of made moves
     */
    public ObservableList<Move> getMoves() {
        return board.getMoves();
    }

    /**
     * Get the underlying board.
     *
     * @return the underlying board
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Get game result based on the current board's state and color of the
     * previous player.
     *
     * @return game result based on the current board's state and color of the
     * previous player
     */
    public GameResult getGameResult() {
        Color previousPlayer = Color.getOpposingColor(currentPlayer);
        GameResult result = board.getGameResult(previousPlayer);
        if (result != GameResult.NONE) {
            numberOfSteps = 0;
            running = false;
            clock.stop();
        } else {
            running = true;
        }

        return result;
    }
}
