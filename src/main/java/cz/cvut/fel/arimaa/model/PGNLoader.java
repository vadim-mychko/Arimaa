package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.Step;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class PGNLoader {

    public static Game loadFromFile(File file) {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // skip tags
            String line = reader.readLine();
            while (line != null
                    && !line.startsWith("1g ") && !line.startsWith("1w ")) {
                line = reader.readLine();
            }

            Board board = new Board();
            board.load(Board.EMPTY_BOARD);
            int numberOfTurn = 1;
            Color player = Color.GOLD;

            while (line != null && !line.equals("")) {
                String prefix = Integer.toString(numberOfTurn) + player.repr;
                String oldPrefix = Integer.toString(numberOfTurn) + player.oldRepr;
                if (!line.startsWith(prefix) && !line.startsWith(oldPrefix)) {
                    return null;
                }

                boolean isInitialPhase = line.startsWith("1g") || line.startsWith("1w")
                        || line.startsWith("1s") || line.startsWith("1b");
                String[] stepStrings = line.substring(prefix.length()).trim().split(" ");
                if (isInitialPhase && stepStrings.length != Board.WIDTH * 2) {
                    return null;
                }

                for (String stepString : stepStrings) {
                    if (stepString == null || stepString.equals("")) {
                        continue;
                    }

                    Step step = Step.fromString(stepString);
                    if (step == null) {
                        return null;
                    } else if (step.removed) {
                        continue;
                    }

                    if (isInitialPhase) {
                        board.addPieceAt(step.piece, step.from);
                    } else if (!board.makeStep(board.getValidSteps(player).stream()
                            .filter(s -> s.piece == step.piece
                                    && s.from.equals(step.from)
                                    && s.getDestination().equals(step.getDestination())
                                    && s.direction == step.direction)
                            .findFirst()
                            .orElse(null))) {
                        return null;
                    }
                }

                player = Color.getOpposingColor(player);
                line = reader.readLine();
                if (player == Color.GOLD) {
                    ++numberOfTurn;
                }

                if (!isInitialPhase && line != null && !line.equals("")) {
                    board.finishMakingMove();
                }
            }

            return new Game(board);
        } catch (IOException e) {
            return null;
        }
    }
}
