package game.controller;

import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * This class is an encapsulation of the javafx event handling which is not really adapted to a real time game
 * This class provide the pressed and the typed event :
 * Pressed: The action bound to the event is executed each frame
 * Typed: The action bound to the event is executed once
 */
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

    /**
     * The method to override which should be called in the main loop
     *
     * @param s the stage
     */
    public abstract void handle(Stage s);

    /**
     * Execute an action if the key is typed
     *
     * @param key    the key to check
     * @param action the action to execute
     * @return true if the action is executed, false otherwise
     */
    protected boolean isTyped(KeyCode key, Runnable action) {
        return isTyped(key, action, null);
    }

    /**
     * Execute an action if both key and combo are being typed
     *
     * @param key    the key to be pressed
     * @param action the action to execute
     * @param combo  the second key which must be pressed
     * @return true if the action is executed, false otherwise
     */
    protected boolean isTyped(KeyCode key, Runnable action, KeyCode combo) {
        if (combo != null && !keysPressed.get(combo)) return false;

        if (keysPressed.get(key) && !performed.get(key)) {
            action.run();
            performed.put(key, true);
            return true;
        }
        return false;
    }

    /**
     * Execute an action if a key is being pressed
     *
     * @param k      the key to check
     * @param action the action to execute
     * @return true if the action is executed, false otherwise
     */
    protected boolean isPressed(KeyCode k, Runnable action) {
        if (keysPressed.get(k)) {
            action.run();
            return true;
        }
        return false;
    }

    /**
     * Execute an action if any of the given key is being pressed.
     * If multiple key of k array are pressed, then the action will be triggered only once
     *
     * @param action the action to execute
     * @param k      the keys to check
     * @return true if the action is executed, false otherwise
     */
    protected boolean isPressed(Runnable action, KeyCode... k) {
        for (KeyCode code : k) {
            if (isPressed(code, action))
                return true;
        }
        return false;
    }
}
