package cz.cvut.fel.arimaa.gui;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new GameView(), 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}
