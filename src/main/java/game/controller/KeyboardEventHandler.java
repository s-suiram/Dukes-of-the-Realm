package game.controller;

import game.logic.Cardinal;
import game.view.WorldView;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

public class KeyboardEventHandler {


    private static KeyboardEventHandler instance;

    private Scene s;
    public static Map<KeyCode, Boolean> keys = new HashMap<>();


    private KeyboardEventHandler(Scene s) {
        this.s = s;
        EnumSet.allOf(KeyCode.class).forEach(k -> keys.put(k, false));
    }

    public static void init(Scene s) {
        instance = new KeyboardEventHandler(s);
        s.setOnKeyPressed(event -> keys.put(event.getCode(), true));
        s.setOnKeyReleased(event -> keys.put(event.getCode(), false));
    }

    public static KeyboardEventHandler getInstance() {
        return instance;
    }

    public void handle() {

        if (keys.get(KeyCode.Z)) {
            WorldView.getInstance().move(Cardinal.NORTH);
        }
        if (keys.get(KeyCode.Q)) {
            WorldView.getInstance().move(Cardinal.WEST);
        }
        if (keys.get(KeyCode.S)) {
            WorldView.getInstance().move(Cardinal.SOUTH);
        }
        if (keys.get(KeyCode.D)) {
            WorldView.getInstance().move(Cardinal.EAST);
        }
        if (keys.get(KeyCode.ADD)) {
            WorldView.getInstance().increaseCameraSpeed();
        }
        if (keys.get(KeyCode.SUBTRACT)) {
            WorldView.getInstance().decreaseCameraSpeed();
        }
        if (keys.get(KeyCode.MULTIPLY)) {
            WorldView.getInstance().resetCameraSpeed();
        }


    }
}
