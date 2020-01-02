package game.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * A class to handle the fullscreen toggling
 */
public class FullscreenKeyboardController extends KeyboardEventHandler {

    public FullscreenKeyboardController(Scene scene) {
        super(scene);
    }

    @Override
    public void handle(Stage s) {
        isTyped(KeyCode.ENTER, () -> s.setFullScreen(!s.isFullScreen()), KeyCode.ALT);
    }
}
