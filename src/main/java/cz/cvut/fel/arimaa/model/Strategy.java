package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

/**
 * Interface for creating engine's strategies.
 */
interface Strategy {

    /**
     * Strategy determines which valid steps it makes to the given board.
     *
     * @param board Board for making valid steps to.
     * @param color Color of engine's figures.
     */
    void makeMove(Board board, Color color);
}
