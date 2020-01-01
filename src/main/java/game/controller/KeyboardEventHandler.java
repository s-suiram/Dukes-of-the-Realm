package game.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public abstract class KeyboardEventHandler {

    private Map<KeyCode, Boolean> keysPressed = new HashMap<>();
    private Map<KeyCode, Boolean> performed = new HashMap<>();
    private Map<KeyCode, Boolean> firstType = new HashMap<>();


    protected KeyboardEventHandler(Scene scene) {
        EnumSet.allOf(KeyCode.class).forEach(k -> keysPressed.put(k, false));
        EnumSet.allOf(KeyCode.class).forEach(k -> performed.put(k, true));
        EnumSet.allOf(KeyCode.class).forEach(k -> firstType.put(k, true));

        scene.setOnKeyPressed(event -> {
            keysPressed.put(event.getCode(), true);
            if (firstType.get(event.getCode())) {
                firstType.put(event.getCode(), false);
                performed.put(event.getCode(), false);
            }
        });

        scene.setOnKeyReleased(event -> {
            performed.put(event.getCode(), false);
            keysPressed.put(event.getCode(), false);
        });
    }

    public abstract void handle(Stage s);

    protected boolean isTyped(KeyCode key, Action action) {
        return isTyped(key, action, null);
    }

    protected boolean isTyped(KeyCode key, Action action, KeyCode combo) {
        if (combo != null && !keysPressed.get(combo)) return false;

        if (keysPressed.get(key) && !performed.get(key)) {
            action.perform();
            performed.put(key, true);
            return true;
        }
        return false;
    }

    protected boolean isPressed(KeyCode k, Action action) {
        if (keysPressed.get(k)) {
            action.perform();
            return true;
        }
        return false;
    }

    protected boolean isPressed(Action action, KeyCode... k) {
        for (KeyCode code : k) {
            if (isPressed(code, action))
                return true;
        }
        return false;
    }
}
