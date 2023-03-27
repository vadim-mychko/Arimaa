package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.Move;

class Engine {

    private Strategy strategy;

    Engine(Strategy strategy) {
        this.strategy = strategy;
    }

    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    Move generateMove(Board board, Color color) {
        return strategy.generateMove(board, color);
    }
}
