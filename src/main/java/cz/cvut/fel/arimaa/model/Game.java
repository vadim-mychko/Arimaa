package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.GameType;

public class Game {

    private Board board;
    private Color currentTurn;
    private Engine engine;
    private GameType gameType;

    public Game() {
        board = new Board();
        board.load();
        currentTurn = Color.GOLD;
        engine = new Engine(new RandomStrategy());
        gameType = GameType.HUMAN_COMPUTER;
    }

    public Game(GameType gameType) {
        this();
        this.gameType = gameType;
    }
}
