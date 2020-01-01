package game.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

public class FullscreenKeyboardController extends KeyboardEventHandler {

    public FullscreenKeyboardController(Scene scene) {
        super(scene);
    }

    @Override
    public void handle(Stage s) {
        isTyped(KeyCode.ENTER, () -> s.setFullScreen(!s.isFullScreen()), KeyCode.ALT);
    }
}
