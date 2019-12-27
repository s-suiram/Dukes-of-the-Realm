package game.view;

import com.sun.javafx.geom.Point2D;
import game.controller.GameEvent;
import game.logic.Cardinal;
import game.logic.World;
import javafx.scene.Group;
import javafx.scene.layout.Pane;

import java.util.*;
import java.util.stream.Collectors;

public class WorldView extends Observable {

    private static WorldView instance = null;
    private int cameraSpeed = 5;
    private List<CastleView> castles;
    private boolean cameraMoved = false;

    public final Point2D cameraPos;

    private WorldView() {
        castles = new ArrayList<>();
        World.getInstance().getCastles().forEach(c -> castles.add(new CastleView(c)));
        cameraPos = new Point2D(0,0);
    }

    public static WorldView getInstance() {
        if (instance == null) instance = new WorldView();
        return instance;
    }

    public void addObservers(Observer... observers) {
        Arrays.asList(observers).forEach(super::addObserver);
    }

    public int getCameraSpeed() {
        return cameraSpeed;
    }

    public void setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        if (this.cameraSpeed < 1) this.cameraSpeed = 1;
        setChanged();
        notifyObservers(GameEvent.CAMERA_SPEED);
    }

    public void decreaseCameraSpeed() {
        setCameraSpeed(cameraSpeed - 1);
    }

    public void increaseCameraSpeed() {
        setCameraSpeed(cameraSpeed + 1);
    }

    public void resetCameraSpeed() {
        setCameraSpeed(5);
    }

    public List<CastleView> getCastles() {
        return castles;
    }

    public boolean checkAndRestoreCameraMoved() {
        if (cameraMoved) {
            cameraMoved = false;
            return true;
        }
        return false;
    }

    public void move(Cardinal direction) {
        cameraMoved = true;
        switch (direction) {
            case NORTH:
                cameraPos.y -= cameraSpeed;
                break;
            case SOUTH:
                cameraPos.y += cameraSpeed;
                break;
            case EAST:
                cameraPos.x += cameraSpeed;
                break;
            case WEST:
                cameraPos.x -= cameraSpeed;
                break;
        }
        if (cameraPos.x < 0) cameraPos.x = 0;
        if (cameraPos.y < 0) cameraPos.y = 0;
        if (cameraPos.x > World.FIELD_WIDTH - App.WINDOW_WIDTH) cameraPos.x = World.FIELD_WIDTH - App.WINDOW_WIDTH;
        if (cameraPos.y > World.FIELD_HEIGHT - App.WINDOW_HEIGHT) cameraPos.y = World.FIELD_HEIGHT - App.WINDOW_HEIGHT;
        setChanged();
        notifyObservers(GameEvent.CAMERA_MOVE);
    }

    public void move(int x, int y) {
        cameraMoved = true;
        cameraPos.y -= y;
        cameraPos.x -= x;
    }

    public List<Group> getTransformedCastleRects() {
        List<Group> rects = getInstance()
                .getCastles().stream()
                .map(c -> c.getRepresentation(cameraPos))
                .collect(Collectors.toList());

        rects.forEach(r -> {
            r.setTranslateX(-cameraPos.x + r.getTranslateX());
            r.setTranslateY(-cameraPos.y +  r.getTranslateY());
        });

        return rects;
    }
}
