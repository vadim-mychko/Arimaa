package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;
import javafx.collections.ObservableList;

public class Game {

    private Board board;
    private Color currentPlayer;
    private int numberOfSteps;
    private Engine engine;
    private GameType gameType;
    private boolean isInitialPhase;
    private boolean running;

    public Game() {
        board = new Board();
        board.load();
        numberOfSteps = 0;
        currentPlayer = Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_HUMAN;
        isInitialPhase = true;
        running = true;
    }

    public Game(Board board) {
        this.board = board;
        numberOfSteps = board.getLastMove().getNumberOfNonRemovalSteps();
        currentPlayer = board.getNumberOfMoves() % 2 == 0
                ? Color.SILVER : Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_HUMAN;
        isInitialPhase = board.getNumberOfMoves() <= 2;
        running = true;
    }

    public int getNumberOfSteps() {
        return numberOfSteps;
    }

    public boolean undoStep() {
        if (!board.undoStep()) {
            return false;
        }

        if (numberOfSteps <= 0) {
            numberOfSteps = board.getLastMove().getNumberOfNonRemovalSteps();
            currentPlayer = Color.getOpposingColor(currentPlayer);
        } else {
            --numberOfSteps;
        }

        return true;
    }

    public void reset() {
        board.load();
        numberOfSteps = 0;
        isInitialPhase = true;
        running = true;
    }

    public boolean finishMakingSteps() {
        if (isInitialPhase) {
            if (gameType == GameType.HUMAN_COMPUTER
                    || currentPlayer == Color.SILVER) {
                isInitialPhase = false;
            }

            currentPlayer = gameType == GameType.HUMAN_HUMAN
                    ? Color.getOpposingColor(currentPlayer)
                    : currentPlayer;

            return true;
        }

        if (!running || numberOfSteps <= 0
                || board.getPreviousStep().type == StepType.PUSH) {
            return false;
        }

        numberOfSteps = 0;
        board.finishMakingMove();

        if (!isInitialPhase && gameType == GameType.HUMAN_COMPUTER) {
            Color engineColor = Color.getOpposingColor(currentPlayer);
            engine.makeMove(board, engineColor);
            board.finishMakingMove();
        } else {
            currentPlayer = Color.getOpposingColor(currentPlayer);
        }

        return true;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public boolean isInitialPhase() {
        return isInitialPhase;
    }

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

    public Color getCurrentPlayer() {
        return currentPlayer;
    }

    public ObservableList<Move> getMoves() {
        return board.getMoves();
    }

    public Board getBoard() {
        return board;
    }

    public GameResult getGameResult() {
        Color previousPlayer = Color.getOpposingColor(currentPlayer);
        GameResult result = board.getGameResult(previousPlayer);
        if (result != GameResult.NONE) {
            numberOfSteps = 0;
            board.finishMakingMove();
            running = false;
        }

        return result;
    }
}
