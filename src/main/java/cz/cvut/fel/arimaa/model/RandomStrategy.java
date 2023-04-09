package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;
import cz.cvut.fel.arimaa.types.Move;
import cz.cvut.fel.arimaa.types.Step;
import cz.cvut.fel.arimaa.types.StepType;

import java.util.*;

/**
 * Random strategy makes random 1-4 valid steps.
 */
class RandomStrategy implements Strategy {

    private Step getRandomStep(Set<Step> steps) {
        List<Step> list = new ArrayList<>(steps);
        Collections.shuffle(list);
        return list.get(0);
    }

    /**
     * Makes random 1-4 valid steps to the given board.
     *
     * @param board Board for making valid steps to.
     * @param color Color of engine's figures.
     */
    @Override
    public void makeMove(Board board, Color color) {
        int numberOfSteps = new Random().nextInt(Move.MAX_LEGAL_STEPS - 1) + 1;
        for (int i = 0; i < numberOfSteps; ++i) {
            Set<Step> validSteps = board.getValidSteps(color);
            boolean isLastStep = i == numberOfSteps - 1;
            if (isLastStep) {
                validSteps.removeIf(step -> step.type == StepType.PUSH);
            }

            Step step = getRandomStep(validSteps);
            board.makeStep(step);
        }
    }
}
