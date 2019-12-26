package game.view.observable;

import game.controller.GameEvent;
import game.view.WorldView;
import javafx.scene.control.Label;

import java.util.Observable;
import java.util.Observer;

public class ObserverLabel extends Label implements Observer {

    private GameEvent type;

    public ObserverLabel(GameEvent type) {
        this.type = type;
    }

    @Override
    public void update(Observable o, Object arg) {
        if (type.equals(arg)) {
            switch ((GameEvent) arg) {
                case CAMERA_MOVE:
                    setText(String.format(
                            "(%f, %f)",
                            WorldView.getInstance().cameraPos.x,
                            WorldView.getInstance().cameraPos.y));
                    break;
                case CAMERA_SPEED:
                    setText(String.valueOf(WorldView.getInstance().getCameraSpeed()));
                    break;
            }
        }
    }
}
