package cz.cvut.fel.arimaa.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.logging.Level;
import java.util.logging.Logger;

public class App extends Application {

    public static void main(String[] args) {
        boolean logsEnabled = args.length > 0 && args[0].equals("--log");
        if (logsEnabled) {
            Logger.getLogger("").setLevel(Level.INFO);
            Logger.getLogger("").info("Logging on INFO level enabled");
        } else {
            Logger.getLogger("").setLevel(Level.OFF);
        }

        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new GameView());
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            Platform.exit();
            System.exit(0);
        });
        
        stage.show();
    }
}
