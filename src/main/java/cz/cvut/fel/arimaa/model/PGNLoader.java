package cz.cvut.fel.arimaa.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PGNLoader {

    public static Game loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Game game = new Game();
            String line = reader.readLine();
            while (line != null) {
                if (!line.startsWith("1g") || !line.startsWith("1w")) {
                    continue;
                }
                
                line = reader.readLine();
            }

            return game;
        } catch (IOException e) {
            return null;
        }
    }
}
