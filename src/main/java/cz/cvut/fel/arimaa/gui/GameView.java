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
        setMinSize(800, 800);
        setLeft(boardView);
        addButtons();
    }

    private void addButtons() {
        HBox buttonPanel = new HBox();
        Button newGame = new Button("New Game");
        Button makeMove = new Button("Make Move");

        newGame.setOnMouseClicked(e -> controller.onNewGameClicked());
        makeMove.setOnMouseClicked(e -> controller.onMakeMoveClicked());

        buttonPanel.getChildren().addAll(newGame, makeMove);
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
            Image image = new Image(getClass().getResourceAsStream("Board.png"));
            double width = image.getWidth() - Board.WIDTH / 2.0;
            double height = image.getHeight() - Board.HEIGHT / 2.0;
            setMinSize(width, height);
            setMaxSize(width, height);
            BackgroundImage backgroundImage =
                    new BackgroundImage(image, null, null, null, null);
            setBackground(new Background(backgroundImage));
        }

        private void addTiles() {
            for (int y = 0; y < Board.HEIGHT; ++y) {
                for (int x = 0; x < Board.WIDTH; ++x) {
                    Square square = Square.getSquare(x, y);
                    ImageView tile = new ImageView();
                    tile.fitWidthProperty().bind(widthProperty().divide(Board.WIDTH));
                    tile.fitHeightProperty().bind(heightProperty().divide(Board.HEIGHT));
                    tile.setPickOnBounds(true);
                    tile.setOnMouseClicked(e ->
                            controller.onSquareClicked(square));

                    tiles[x][y] = tile;
                    add(tile, x, y);
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
                Image image = new Image(getClass().getResourceAsStream(filename));
                images.put(pieceName, image);
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
