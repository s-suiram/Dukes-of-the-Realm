package game.controller;

import game.logic.Cardinal;
import game.view.WorldView;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class KeyboardEventHandler {


    private static KeyboardEventHandler instance;
    private final String PLUS = "+";
    private final String MINUS = "-";
    private final String MULTIPLY = "*";
    public Map<KeyCode, Boolean> keysPressed = new HashMap<>();
    public Map<String, Boolean> keysTyped = new HashMap<>();

    private KeyboardEventHandler(Scene s) {
        EnumSet.allOf(KeyCode.class).forEach(k -> keysPressed.put(k, false));
        keysTyped.put(PLUS, false);
        keysTyped.put(MINUS, false);
        keysTyped.put(MULTIPLY, false);

        s.setOnKeyPressed(event -> {
            keysPressed.put(event.getCode(), true);
        });
        s.setOnKeyTyped(event -> {
            System.out.println(event.getCharacter());
            keysTyped.put(event.getCharacter(), true);
            event.consume();
        });
        s.setOnKeyReleased(event -> keysPressed.put(event.getCode(), false));
    }

    public static void init(Scene s) {
        instance = new KeyboardEventHandler(s);
    }

    public static KeyboardEventHandler getInstance() {
        return instance;
    }

    public void handle() {
        handleKeysPressed();
        handleKeysTyped();
    }

    private void handleKeysPressed() {
        doKeyPressedAction(KeyCode.Z, WorldView.getInstance()::move, Cardinal.NORTH);
        doKeyPressedAction(KeyCode.Q, WorldView.getInstance()::move, Cardinal.WEST);
        doKeyPressedAction(KeyCode.S, WorldView.getInstance()::move, Cardinal.SOUTH);
        doKeyPressedAction(KeyCode.D, WorldView.getInstance()::move, Cardinal.EAST);
    }

    private void handleKeysTyped() {
        doKeyTypedAction(PLUS, WorldView.getInstance()::increaseCameraSpeed);
        doKeyTypedAction(MINUS, WorldView.getInstance()::decreaseCameraSpeed);
        doKeyTypedAction(MULTIPLY, WorldView.getInstance()::resetCameraSpeed);
    }

    private void doKeyTypedAction(String key, Action action) {
        if (keysTyped.getOrDefault(key, false)) {
            action.perform();
            keysTyped.put(key, false);
        }
    }

    private void doKeyPressedAction(KeyCode k, Consumer<Cardinal> consumer, Cardinal car) {
        if (keysPressed.get(k)) {
            consumer.accept(car);
        }
    }

    private void doKeyPressedAction(KeyCode k, Action action) {
        if (keysPressed.get(k)) {
            action.perform();
        }
    }
}
