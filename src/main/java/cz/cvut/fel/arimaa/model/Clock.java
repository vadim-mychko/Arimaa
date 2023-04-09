package cz.cvut.fel.arimaa.model;

import cz.cvut.fel.arimaa.types.Color;

class Clock {

    private final Object lock;
    private int goldTimeElapsed;
    private int silverTimeElapsed;
    private Color player;
    private boolean running;

    Clock() {
        goldTimeElapsed = silverTimeElapsed = 0;
        player = Color.GOLD;
        lock = new Object();
        running = true;
        start();
    }

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

    void switchPlayer() {
        player = Color.getOpposingColor(player);
    }

    void reset() {
        synchronized (lock) {
            goldTimeElapsed = silverTimeElapsed = 0;
            player = Color.GOLD;
        }
    }

    void stop() {
        running = false;
    }
}
