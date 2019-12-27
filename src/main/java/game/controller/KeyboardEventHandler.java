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

    private Map<KeyCode, Boolean> keysPressed = new HashMap<>();
    private Map<KeyCode, Boolean> performed = new HashMap<>();
    private Map<KeyCode, Boolean> firstType = new HashMap<>();

    private KeyboardEventHandler(Scene s) {
        EnumSet.allOf(KeyCode.class).forEach(k -> keysPressed.put(k, false));
        EnumSet.allOf(KeyCode.class).forEach(k -> performed.put(k, true));
        EnumSet.allOf(KeyCode.class).forEach(k -> firstType.put(k, true));

        s.setOnKeyPressed(event -> {
            keysPressed.put(event.getCode(), true);
            if(firstType.get(event.getCode())){
                firstType.put(event.getCode(),false);
                performed.put(event.getCode(), false);
            }

            doKeyTypedAction(KeyCode.ADD, WorldView.getInstance()::increaseCameraSpeed);
            doKeyTypedAction(KeyCode.SUBTRACT, WorldView.getInstance()::decreaseCameraSpeed);
            doKeyTypedAction(KeyCode.MULTIPLY, WorldView.getInstance()::resetCameraSpeed);
        });

        s.setOnKeyReleased(event -> {
            performed.put(event.getCode(), false);
            keysPressed.put(event.getCode(), false);
        });
    }

    public static void init(Scene s) {
        instance = new KeyboardEventHandler(s);
    }

    public static KeyboardEventHandler getInstance() {
        return instance;
    }

    public void handle() {
        handleKeysPressed();
    }

    private void handleKeysPressed() {
        doKeyPressedAction(KeyCode.Z, WorldView.getInstance()::move, Cardinal.NORTH);
        doKeyPressedAction(KeyCode.Q, WorldView.getInstance()::move, Cardinal.WEST);
        doKeyPressedAction(KeyCode.S, WorldView.getInstance()::move, Cardinal.SOUTH);
        doKeyPressedAction(KeyCode.D, WorldView.getInstance()::move, Cardinal.EAST);
    }


    private void doKeyTypedAction(KeyCode key, Action action) {
        if (!performed.get(key)) {
            System.out.println(key.getName());
            action.perform();
            performed.put(key,true);
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
