package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game {

    private Board board;
    private List<Move> moves;
    private Move currentMove;
    private Color currentTurn;
    private int numberOfSteps;
    private Engine engine;
    private GameType gameType;
    private boolean running;

    public Game() {
        board = new Board();
        board.load();
        moves = new ArrayList<>();
        currentMove = new Move();
        numberOfSteps = 0;
        currentTurn = Color.GOLD;
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

    public boolean finishMakingSteps() {
        if (!running || (currentMove.hasSteps()
                && currentMove.getLast().type == StepType.PUSH)) {
            return false;
        }

        numberOfSteps = 0;
        moves.add(currentMove);
        currentMove = new Move();

        if (gameType == GameType.HUMAN_COMPUTER) {
            Color engineColor = Color.getOpposingColor(currentTurn);
            Move engineMove = engine.generateMove(board.getCopy(), engineColor);
            board.makeMove(engineMove);
            moves.add(engineMove);
        } else {
            currentTurn = Color.getOpposingColor(currentTurn);
        }

        return true;
    }

    public boolean makeStep(Square from, Square to) {
        if (!running) {
            return false;
        }

        Set<Step> validSteps = board.getValidSteps(from);
        Step nextStep = validSteps.stream()
                .filter(step -> step.getDestination().equals(to))
                .findFirst()
                .orElse(null);

        if (nextStep == null || numberOfSteps >= 4
                || (numberOfSteps == 3 && nextStep.type == StepType.PUSH)) {
            return false;
        }

        board.makeStep(nextStep);
        currentMove.steps[numberOfSteps++] = nextStep;

        return true;
    }

    public Board getBoard() {
        return board;
    }

    public GameResult getGameResult() {
        Color previousPlayer = Color.getOpposingColor(currentTurn);
        GameResult result = board.getGameResult(previousPlayer);
        if (result != GameResult.NONE) {
            running = false;
        }

        return result;
    }
}
