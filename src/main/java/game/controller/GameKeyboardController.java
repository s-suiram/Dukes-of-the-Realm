package game.controller;

import game.App;
import game.logic.Cardinal;
import game.view.WorldView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

/**
 * This class handle the keyboard event in the game scene
 */
public class GameKeyboardController extends FullscreenKeyboardController {
    public GameKeyboardController(Scene scene) {
        super(scene);
    }

    @Override
    public void handle(Stage s) {
        super.handle(s);
        isPressed(() -> WorldView.getInstance().move(Cardinal.NORTH), KeyCode.Z, KeyCode.UP);
        isPressed(() -> WorldView.getInstance().move(Cardinal.WEST), KeyCode.Q, KeyCode.LEFT);
        isPressed(() -> WorldView.getInstance().move(Cardinal.SOUTH), KeyCode.S, KeyCode.DOWN);
        isPressed(() -> WorldView.getInstance().move(Cardinal.EAST), KeyCode.D, KeyCode.RIGHT);

        isTyped(KeyCode.ESCAPE, () -> App.getGame().toggleMenu());
        isTyped(KeyCode.ADD, WorldView.getInstance()::increaseCameraSpeed);
        isTyped(KeyCode.SUBTRACT, WorldView.getInstance()::decreaseCameraSpeed);
        isTyped(KeyCode.MULTIPLY, WorldView.getInstance()::resetCameraSpeed);
        isTyped(KeyCode.SPACE, () -> App.getGame().togglePause());
    }
}
