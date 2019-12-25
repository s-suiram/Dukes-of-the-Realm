package game.controller;

import game.view.App;
import javafx.event.EventType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public enum GameEvent {

    //Camera moving
    CAMERA_MOVE,
    CAMERA_MOVE_RIGHT(KeyCode.D),

    CAMERA_MOVE_LEFT(KeyCode.Q),

    CAMERA_MOVE_UP(KeyCode.Z),

    CAMERA_MOVE_DOWN(KeyCode.S),

    // Camera speed
    CAMERA_SPEED,
    CAMERA_SPEED_INCREASE(KeyCode.ADD, KeyEvent.KEY_TYPED),
    CAMERA_SPEED_DECREASE(KeyCode.SUBTRACT, KeyEvent.KEY_TYPED),
    CAMERA_SPEED_RESET(KeyCode.MULTIPLY, KeyEvent.KEY_TYPED);

    private KeyCode key;
    private KeyCode combo;
    private EventType<KeyEvent> type;

    GameEvent() {
        this(null, null, null);
    }

    GameEvent(KeyCode key) {
        this(key, null, KeyEvent.KEY_PRESSED);
    }

    GameEvent(KeyCode key, EventType<KeyEvent> type) {
        this(key, null, type);
    }

    GameEvent(KeyCode key, KeyCode keyCombo, EventType<KeyEvent> type) {
        this.key = key;
        this.combo = keyCombo;
        this.type = type;
    }

    public void define(Runnable function) {
        if (type == KeyEvent.KEY_PRESSED) {
            if (App.isKeyPressed(key) && (combo == null || App.isKeyPressed(combo))) function.run();
        }

        if (type == KeyEvent.KEY_TYPED) {
            if (App.isKeyPressedAndConsume(key) && (combo == null || App.isKeyPressed(combo))) function.run();
        }
    }

    public KeyCode getKey() {
        return key;
    }

    public void setKey(KeyCode key) {
        this.key = key;
    }

    public KeyCode getCombo() {
        return combo;
    }

    public void setCombo(KeyCode combo) {
        this.combo = combo;
    }
}
