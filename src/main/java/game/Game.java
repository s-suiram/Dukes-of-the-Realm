package game;

import javafx.animation.AnimationTimer;

public class Game {

    public static int WINDOW_WIDTH = 800;
    public static int WINDOW_HEIGHT = 600;

    private AnimationTimer gameLoop;

    public void runGameLoop() {
        gameLoop = new AnimationTimer() {
            long frames = 0;

            @Override
            public void handle(long now) {

                frames++;
            }
        };
        gameLoop.start();
    }
}
