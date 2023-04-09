package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.model.PGNLoader;
import cz.cvut.fel.arimaa.types.GameType;
import cz.cvut.fel.arimaa.types.Move;
import cz.cvut.fel.arimaa.types.Square;
import javafx.collections.ObservableList;

import java.io.File;
import java.util.logging.Logger;

/**
 * Class between view (GameView) and logic model (Game).
 * Manipulates the logic model based on input from GUI.
 * If logic model changes, updates the view.
 */
class GameController {

    private static final Logger logger
            = Logger.getLogger(GameController.class.getName());

    private Game game;
    private GameView view;
    private Square previousSquare;

    /**
     * Constructs a new GameController instance with the given view to update.
     * Also constructs a new logic model to manipulate.
     *
     * @param view View to be updated when logic model changes.
     */
    GameController(GameView view) {
        this.game = new Game();
        this.view = view;
        previousSquare = null;
    }

    /**
     * Get observable list of moves in the logic model.
     *
     * @return observable list of moves in the logic model
     */
    ObservableList<Move> getMoves() {
        return game.getMoves();
    }

    /**
     * Get the underlying logic model.
     *
     * @return underlying logic model
     */
    Game getGame() {
        return game;
    }

    /**
     * Resets the underlying logic model for starting a new game.
     * Updates the view accordingly.
     */
    void onNewGameClicked() {
        game.reset();
        view.update(game);
        logger.info("New game started");
    }

    /**
     * Undoes the last step for either player in the underlying logic model.
     * Updates the view accordingly.
     */
    void onUndoStepClicked() {
        if (game.undoStep()) {
            view.update(game);
            logger.info("Previous step is undone");
        }
    }

    /**
     * Saves the moves in the underlying logic model in PGN format to the given
     * file.
     *
     * @param file File for the logic model to be saved to.
     */
    void onSavePGNChosen(File file) {
        if (file == null) {
            return;
        }

        if (PGNLoader.saveToFile(game, file)) {
            logger.info("Saved the game to the given file");
        }
    }

    /**
     * Constructs a new logic model based on the given PGN file.
     * If successful, substitutes controller's underlying logic model.
     * Updates the view accordingly.
     *
     * @param file File for a logic model to be loaded from.
     */
    void onLoadPGNChosen(File file) {
        if (file == null) {
            return;
        }

        Game loadedGame = PGNLoader.loadFromFile(file);
        if (loadedGame == null) {
            return;
        }

        this.game = loadedGame;
        view.setMoveListView(game.getMoves());
        view.update(game);
        logger.info("Loaded a game from the given file");
    }

    /**
     * Changes the game type of the underlying logic model.
     * Updates the view accordingly.
     *
     * @param gameType Game type to be set.
     */
    void onGameTypeSelected(GameType gameType) {
        game.setGameType(gameType);
        view.update(game);
        logger.info("Selected game type: " + gameType);
    }

    /**
     * Finishes making steps for the current player in the underlying logic
     * model.
     * Updates the view accordingly.
     */
    void onMakeMoveClicked() {
        if (game.finishMakingSteps()) {
            view.update(game);
            logger.info("Current player finished making steps");
        }
    }

    /**
     * Allows to manipulate the arimaa figures by clicking on squares in the GUI.
     * Manipulates the underlying logic model based on previously clicked square
     * and the current one. If it is a valid move, logic model makes a step.
     * In that case the view is updated.
     *
     * @param square Square clicked on.
     */
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
