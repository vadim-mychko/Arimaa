package cz.cvut.fel.arimaa.types;

/**
 * Class for representing type of game (i.e. human vs. human, human vs. AI).
 */
public enum GameType {

    HUMAN_HUMAN, HUMAN_COMPUTER;

    @Override
    public String toString() {
        return switch (this) {
            case HUMAN_HUMAN -> "Human vs. Human";
            case HUMAN_COMPUTER -> "Human vs. Computer";
        };
    }
}
