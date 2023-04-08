package cz.cvut.fel.arimaa.gui;

import cz.cvut.fel.arimaa.model.Board;
import cz.cvut.fel.arimaa.model.Game;
import cz.cvut.fel.arimaa.types.*;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

class GameView extends BorderPane {

    private GameController controller;
    private BoardView boardView;
    private Text gameTypeView;
    private Text currentPlayerView;
    private Text numberOfStepsView;
    private Text gameResultView;

    GameView() {
        super();
        this.controller = new GameController(this);
        this.boardView = new BoardView();
        this.gameTypeView = new Text();
        this.currentPlayerView = new Text();
        this.numberOfStepsView = new Text();
        this.gameResultView = new Text();
        setMinSize(800, 800);
        addBoardView();
        addButtons();
        addPGNView();
        update(controller.getGame());
    }

    private void addButtons() {
        Button newGame = new Button("New Game");
        Button makeMove = new Button("Make Move");
        Button undoStep = new Button("Undo Step");
        MenuItem humanVsHuman = new MenuItem("Human|Human");
        MenuItem humanVsComputer = new MenuItem("Human|Computer");
        Button loadPGN = new Button("Load PGN");

        newGame.setOnMouseClicked(e -> controller.onNewGameClicked());
        makeMove.setOnMouseClicked(e -> controller.onMakeMoveClicked());
        undoStep.setOnMouseClicked(e -> controller.onUndoStepClicked());
        humanVsHuman.setOnAction(e ->
                controller.onGameTypeSelected(GameType.HUMAN_HUMAN));
        humanVsComputer.setOnAction(e ->
                controller.onGameTypeSelected(GameType.HUMAN_COMPUTER));

        Stage dialogStage = new Stage();
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        loadPGN.setOnMouseClicked(e ->
                controller.onLoadPGNChosen(new FileChooser().showOpenDialog(dialogStage)));

        MenuButton gameType = new MenuButton("Game Type", null,
                humanVsHuman, humanVsComputer);

        setTop(new HBox(newGame, makeMove, undoStep, gameType, loadPGN));
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

        setCenter(new VBox(gameTypeView, currentPlayerView, numberOfStepsView,
                gameResultView, moveListView));
    }

    void update(Game game) {
        gameTypeView.setText("Game type: " + game.getGameType());
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
