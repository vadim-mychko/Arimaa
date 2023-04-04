package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;

import java.util.Set;
import java.util.logging.Logger;

public class Game {

    private static final Logger logger = Logger.getLogger(Game.class.getName());

    private Board board;
    private Color currentPlayer;
    private int numberOfSteps;
    private Engine engine;
    private GameType gameType;
    private boolean running;
    private Set<Step> cachedSteps;

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
        cachedSteps = null;
    }

    public boolean finishMakingSteps() {
        if (!running || numberOfSteps <= 0
                || board.getPreviousStep().type == StepType.PUSH) {
            return false;
        }

        numberOfSteps = 0;
        cachedSteps = null;
        board.finishMakingMove();

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

        if (cachedSteps == null) {
            cachedSteps = board.getValidSteps(currentPlayer);
        }

        Step nextStep = cachedSteps.stream()
                .filter(step -> step.from.equals(from)
                        && step.getDestination().equals(to))
                .findFirst()
                .orElse(null);

        if (nextStep == null || numberOfSteps >= 4
                || (numberOfSteps == 3 && nextStep.type == StepType.PUSH)) {
            return false;
        }

        cachedSteps = null;
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
