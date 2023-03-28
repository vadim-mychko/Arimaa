package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

class Engine {

    private Strategy strategy;

    Engine(Strategy strategy) {
        this.strategy = strategy;
    }

    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    void makeMove(Board board, Color color) {
        strategy.makeMove(board, color);
    }
}
