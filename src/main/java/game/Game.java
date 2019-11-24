package game;

import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.input.KeyCode;

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

    public void stopGameLoop() {
        gameLoop.stop();
    }
}
