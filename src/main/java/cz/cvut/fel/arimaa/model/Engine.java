package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

/**
 * Engine for making AI generated moves based on a certain strategy.
 */
class Engine {

    private Strategy strategy;

    /**
     * Creates a new instance of Engine with the given strategy to follow.
     *
     * @param strategy Strategy to follow for making valid steps.
     */
    Engine(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Set the engine's strategy to the given one.
     *
     * @param strategy New strategy to follow.
     */
    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    /**
     * Ask the engine to make valid steps to the given board based on its
     * underlying strategy.
     *
     * @param board Board for making valid steps to.
     * @param color Color of engine's figures.
     */
    void makeMove(Board board, Color color) {
        strategy.makeMove(board, color);
    }
}
