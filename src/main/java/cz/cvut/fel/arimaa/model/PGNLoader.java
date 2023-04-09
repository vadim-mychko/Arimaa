package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.Move;
import cz.cvut.fel.arimaa.types.Step;

import java.io.*;
import java.util.List;

/**
 * Class with static methods for loading/saving games in PGN format.
 */
public class PGNLoader {

    private PGNLoader() {

    }

    /**
     * Save the given game's content to the given file in PGN format.
     *
     * @param game Game to be saved into the given file.
     * @param file File to be saved into.
     * @return true on success, false otherwise
     */
    public static boolean saveToFile(Game game, File file) {
        List<Move> moves = game.getMoves();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            int numberOfTurn = 1;
            Color player = Color.GOLD;
            for (Move move : moves) {
                String line = Integer.toString(numberOfTurn) + player.repr + " " + move;
                writer.write(line);
                writer.newLine();

                player = Color.getOpposingColor(player);
                if (player == Color.GOLD) {
                    ++numberOfTurn;
                }
            }
        } catch (IOException e) {
            return false;
        }

        return true;
    }

    /**
     * Constructs a new logic model based on steps in PGN format from the given
     * file. Tags are ignored and not maintained.
     *
     * @param file File of game moves in PGN format.
     * @return instance of Game based on PGN content from the given file on
     * success, null otherwise (if parsing fails)
     */
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

                boolean hasParsedAnyStep = false;
                for (String stepString : stepStrings) {
                    if (stepString == null || stepString.equals("")) {
                        continue;
                    }

                    hasParsedAnyStep = true;

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

                if (!isInitialPhase && hasParsedAnyStep) {
                    board.finishMakingMove();
                }
            }

            return new Game(board);
        } catch (IOException e) {
            return null;
        }
    }
}
