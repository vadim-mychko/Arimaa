package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

interface Strategy {

    void makeMove(Board board, Color color);
}
