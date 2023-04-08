package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Board;
import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.Move;
import cz.cvut.fel.arimaa.types.Piece;
import cz.cvut.fel.arimaa.types.Square;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.util.HashMap;
import java.util.Map;

class GameView extends BorderPane {

    private GameController controller;
    private BoardView boardView;
    private Text currentPlayerView;
    private Text numberOfStepsView;
    private Text gameResultView;

    GameView() {
        super();
        this.controller = new GameController(this);
        this.boardView = new BoardView();
        this.currentPlayerView = new Text();
        this.numberOfStepsView = new Text();
        this.gameResultView = new Text();
        setMinSize(800, 800);
        addBoardView();
        addButtons();
        addPGNView();
    }

    private void addButtons() {
        Button newGame = new Button("New Game");
        Button makeMove = new Button("Make Move");

        newGame.setOnMouseClicked(e -> controller.onNewGameClicked());
        makeMove.setOnMouseClicked(e -> controller.onMakeMoveClicked());

        setTop(new HBox(newGame, makeMove));
    }

    private void addBoardView() {
        setLeft(boardView);
    }

    private void addPGNView() {
        ListView<Move> moveListView = new ListView<>(controller.getMoves());
        moveListView.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Move item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    int index = getIndex();
                    int turn = index / 2 + 1;
                    Color color = index % 2 == 0 ? Color.GOLD : Color.SILVER;
                    setText(turn + "" + color.repr + " " + item);
                }
            }
        });

        setCenter(new VBox(currentPlayerView, numberOfStepsView, gameResultView,
                moveListView));
    }

    void update(Game game) {
        currentPlayerView.setText("Current player: " + game.getCurrentPlayer());
        numberOfStepsView.setText("Number of steps: " + game.getNumberOfSteps());
        gameResultView.setText("Game result: " + game.getGameResult());
        boardView.update(game.getBoard());
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
