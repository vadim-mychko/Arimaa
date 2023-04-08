package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.types.GameType;
import cz.cvut.fel.arimaa.types.Move;
import cz.cvut.fel.arimaa.types.Square;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.logging.Logger;

class GameController {

    private static final Logger logger
            = Logger.getLogger(GameController.class.getName());

    private Game game;
    private GameView view;
    private Square previousSquare;

    GameController(GameView view) {
        this.game = new Game();
        this.view = view;
        previousSquare = null;
    }

    ObservableList<Move> getMoves() {
        return game.getMoves();
    }

    Game getGame() {
        return game;
    }

    void onNewGameClicked() {
        game.reset();
        view.update(game);
        logger.info("New game started");
    }

    void onUndoStepClicked() {
        if (game.undoStep()) {
            view.update(game);
            logger.info("Previous step is undone");
        }
    }

    void onLoadPGNChosen(File file) {
        if (file == null) {
            return;
        }

        logger.info("Loaded a game from PGN");
    }

    void onGameTypeSelected(GameType gameType) {
        game.setGameType(gameType);
        view.update(game);
        logger.info("Selected game type: " + gameType);
    }

    void onMakeMoveClicked() {
        if (game.finishMakingSteps()) {
            view.update(game);
            logger.info("Current player finished making steps");
        }
    }

    void onSquareClicked(Square square) {
        if (previousSquare == null || previousSquare.equals(square)) {
            previousSquare = square;
            return;
        }

        if (game.makeStep(previousSquare, square)) {
            view.update(game);
        }

        previousSquare = null;
    }
}
