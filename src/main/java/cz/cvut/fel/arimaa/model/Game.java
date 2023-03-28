package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;

import java.util.Set;

public class Game {

    private Board board;
    private Color currentPlayer;
    private int numberOfSteps;
    private Engine engine;
    private GameType gameType;
    private boolean running;

    public Game() {
        board = new Board();
        board.load(Board.EMPTY_BOARD);
        numberOfSteps = 0;
        currentPlayer = Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_COMPUTER;
        running = true;
    }

    public Game(GameType gameType) {
        this();
        this.gameType = gameType;
    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public void reset() {
        board.load();
        numberOfSteps = 0;
        running = true;
    }

    public boolean finishMakingSteps() {
        if (!running || numberOfSteps <= 0
                || board.getPreviousStep().type == StepType.PUSH) {
            return false;
        }

        numberOfSteps = 0;

        if (gameType == GameType.HUMAN_COMPUTER) {
            Color engineColor = Color.getOpposingColor(currentPlayer);
            engine.makeMove(board, engineColor);
        } else {
            currentPlayer = Color.getOpposingColor(currentPlayer);
        }

        return true;
    }

    public boolean makeStep(Square from, Square to) {
        if (!running) {
            return false;
        }

        Set<Step> validSteps = board.getValidSteps(from);
        Step nextStep = validSteps.stream()
                .filter(step -> board.getPieceAt(step.from).color == currentPlayer)
                .filter(step -> step.getDestination().equals(to))
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

    public Board getBoard() {
        return board;
    }

    public GameResult getGameResult() {
        Color previousPlayer = Color.getOpposingColor(currentPlayer);
        GameResult result = board.getGameResult(previousPlayer);
        if (result != GameResult.NONE) {
            running = false;
        }

        return result;
    }
}
