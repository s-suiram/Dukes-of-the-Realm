package game.view;

import com.sun.javafx.geom.Point2D;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import game.logic.Cardinal;
import game.logic.World;
import game.logic.troop.Onager;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.Scene;

import java.util.*;
import java.util.stream.Collectors;

public class WorldView extends Observable {

    private static WorldView instance = null;
    public final Point2D cameraPos;
    private int cameraSpeed = 10;
    private List<CastleView> castles;
    private List<TroopView> troops;
    private boolean cameraMoved = false;
    private Scene s;

    private WorldView(Scene s) {
        this.s =s;
        castles = new ArrayList<>();
        World.getInstance().getCastles().forEach(c -> castles.add(new CastleView(c, s)));
        cameraPos = new Point2D(0, 0);
        troops = new ArrayList<>();
        Troop test = new Onager();
        test.pos.setLocation(100,100);
        troops.add( new TroopView(test));
    }

    public static WorldView getInstance() {
        if (instance == null) throw new NullPointerException("instance not initialized");
        return instance;
    }

    public static void init(Scene s) {
        instance = new WorldView(s);
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
    }

    public void decreaseCameraSpeed() {
        setCameraSpeed(cameraSpeed - 1);
    }

    public void increaseCameraSpeed() {
        setCameraSpeed(cameraSpeed + 1);
    }

    public void resetCameraSpeed() {
        setCameraSpeed(10);
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

    private void checkCameraBound() {
        if (cameraPos.x < 0) cameraPos.x = 0;
        if (cameraPos.y < 0) cameraPos.y = 0;
        if (cameraPos.x > World.FIELD_WIDTH - App.WINDOW_WIDTH) cameraPos.x = World.FIELD_WIDTH - App.WINDOW_WIDTH;
        if (cameraPos.y > World.FIELD_HEIGHT - App.WINDOW_HEIGHT) cameraPos.y = World.FIELD_HEIGHT - App.WINDOW_HEIGHT;
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
        checkCameraBound();
        setChanged();
    }

    public void move(int x, int y) {
        cameraMoved = true;
        cameraPos.y -= y;
        cameraPos.x -= x;
        checkCameraBound();
        setChanged();
    }

    public void draw(Group troopsGroup) {
        getCastles().forEach(c -> c.draw(cameraPos));
        troopsGroup.getChildren().removeAll(troops);
        troops.clear();
        troops.addAll(World.getInstance()
                .getTroops()
                .stream()
                .map(t -> TroopView.troopToView.getOrDefault(t, new TroopView(t)))
                .collect(Collectors.toList())
        );
        troopsGroup.getChildren().addAll(troops);


        troops.forEach(c -> c.draw(cameraPos));
    }

    public void clearAllContextualMenu() {
        getCastles().forEach(c -> c.setVisibleContextual(false));
    }

}
