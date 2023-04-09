package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

/**
 * Class for tracking time each player spends on making steps.
 */
class Clock {

    private final Object lock;
    private int goldTimeElapsed;
    private int silverTimeElapsed;
    private Color player;
    private boolean running;

    /**
     * Constructs an instance of clock. Starts running immediately.
     */
    Clock() {
        goldTimeElapsed = silverTimeElapsed = 0;
        player = Color.GOLD;
        lock = new Object();
        running = true;
        start();
    }

    /**
     * Get number of seconds player of the given color has spent on making steps.
     *
     * @param player Color of the player for getting time from.
     * @return number of seconds the given player has spent on making steps
     */
    int getTimeElapsed(Color player) {
        return player == Color.GOLD ? goldTimeElapsed : silverTimeElapsed;
    }

    private void start() {
        Thread thread = new Thread(() -> {
            while (running) {
                try {
                    Thread.sleep(1000);
                    synchronized (lock) {
                        if (player == Color.GOLD) {
                            ++goldTimeElapsed;
                        } else {
                            ++silverTimeElapsed;
                        }
                    }
                } catch (InterruptedException ignored) {

                }
            }
        });

        thread.setDaemon(false);
        thread.start();
    }

    /**
     * Stops counting number of seconds the current player has spent on making
     * steps and starts counting for its opponent.
     */
    void switchPlayer() {
        player = Color.getOpposingColor(player);
    }

    /**
     * Resets number of seconds each player has spent on making steps.
     */
    void reset() {
        synchronized (lock) {
            goldTimeElapsed = silverTimeElapsed = 0;
            player = Color.GOLD;
        }
    }

    /**
     * Stops clock from counting number of seconds each player has spent on
     * making steps.
     */
    void stop() {
        running = false;
    }
}
