package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.types.Square;

class GameController {

    private Game game;
    private GameView view;
    private Square previousSquare;

    GameController(GameView view) {
        this.game = new Game();
        this.view = view;
        previousSquare = null;
    }

    void onNewGameClicked() {
        game.reset();
        view.update(game.getBoard());
    }

    void onSquareClicked(Square square) {
        System.out.println("here");
        if (previousSquare == null) {
            previousSquare = square;
            return;
        }

        boolean madeStep = game.makeStep(previousSquare, square);
        if (madeStep) {
            view.update(game.getBoard());
        }

        previousSquare = null;
    }
}
