package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Board;
import cz.cvut.fel.arimaa.types.Piece;
import cz.cvut.fel.arimaa.types.Square;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.HashMap;
import java.util.Map;

class GameView extends BorderPane {

    private GameController controller;
    private BoardView boardView;

    GameView() {
        super();
        this.boardView = new BoardView();
        this.controller = new GameController(this);
        setCenter(boardView);
        addButtons();
    }

    private void addButtons() {
        VBox buttonPanel = new VBox();
        Button newGame = new Button("New Game");

        newGame.setOnMouseClicked(e -> controller.onNewGameClicked());

        buttonPanel.getChildren().add(newGame);
        setTop(buttonPanel);
    }

    void update(Board board) {
        boardView.update(board);
    }

    private class BoardView extends GridPane {

        private Map<String, Image> images;
        private ImageView[][] tiles;

        BoardView() {
            super();
            images = new HashMap<>();
            tiles = new ImageView[Board.WIDTH][Board.HEIGHT];
            addBackground();
            addTiles();
        }

        private void addBackground() {
            Image image = new Image(getClass().getResourceAsStream("Board.jpg"));
            BackgroundSize size = new BackgroundSize(getWidth(), getHeight(),
                    true, true, true, false);
            BackgroundImage backgroundImage = new BackgroundImage(image,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.DEFAULT, size);
            setBackground(new Background(backgroundImage));
        }

        private void addTiles() {
            for (int y = 0; y < Board.HEIGHT; ++y) {
                for (int x = 0; x < Board.WIDTH; ++x) {
                    ImageView tile = new ImageView();
                    tiles[x][y] = tile;
                    add(tile, x, y);

                    int col = x;
                    int row = y;
                    tile.setOnMouseClicked(e ->
                            controller.onSquareClicked(Square.getSquare(col, row)));
                }
            }
        }

        private Image getPieceImage(Piece piece) {
            if (piece == null) {
                return null;
            }

            String pieceName = piece.toString();
            if (!images.containsKey(pieceName)) {
                String filename = pieceName + ".png";
                images.put(pieceName,
                        new Image(getClass().getResourceAsStream(filename)));
            }

            return images.get(pieceName);
        }

        void update(Board board) {
            for (int y = 0; y < Board.HEIGHT; ++y) {
                for (int x = 0; x < Board.WIDTH; ++x) {
                    Piece piece = board.getPieceAt(Square.getSquare(x, y));
                    tiles[x][y].setImage(getPieceImage(piece));
                }
            }
        }
    }
}
