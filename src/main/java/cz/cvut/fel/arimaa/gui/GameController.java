package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.types.Square;

import java.util.logging.Logger;

class GameController {

    private static final Logger logger = Logger.getLogger(
            GameController.class.getName());

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
        logger.info("New game started");
    }

    void onMakeMoveClicked() {
        if (!game.finishMakingSteps()) {
            return;
        }

        view.update(game.getBoard());
        logger.info("Current player finished making steps");
    }

    void onSquareClicked(Square square) {
        if (previousSquare == null) {
            previousSquare = square;
            return;
        }

        if (game.makeStep(previousSquare, square)) {
            view.update(game.getBoard());
        }

        previousSquare = null;
    }
}
