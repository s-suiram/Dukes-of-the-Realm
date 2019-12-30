package game.view;

import com.sun.javafx.geom.Point2D;
import game.App;
import game.logic.Cardinal;
import game.logic.Castle;
import game.logic.World;
import game.logic.troop.Troop;
import javafx.scene.Group;

import java.util.*;

public class WorldView extends Observable {

    private static WorldView instance = null;
    public final Point2D cameraPos;
    private int cameraSpeed = 10;
    private List<CastleView> castleViews;
    private List<TroopView> troopsViews;
    private boolean cameraMoved = false;
    private Group castleParent;
    private Group troopParent;

    private WorldView(Group castleParent, Group troopParent) {
        this.castleParent = castleParent;
        this.troopParent = troopParent;
        castleViews = new ArrayList<>();
        Castle.getCastles().forEach(c -> castleViews.add(new CastleView(c, castleParent)));
        cameraPos = new Point2D(0, 0);
        troopsViews = new ArrayList<>();
    }

    public static WorldView getInstance() {
        if (instance == null) throw new NullPointerException("instance not initialized");
        return instance;
    }

    public static void init(Group castleParent, Group troopParent) {
        instance = new WorldView(castleParent, troopParent);
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

    public List<CastleView> getCastleViews() {
        return castleViews;
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
        if (cameraPos.x > World.FIELD_WIDTH - App.getGame().getWindowWidth())
            cameraPos.x = World.FIELD_WIDTH - (float) App.getGame().getWindowWidth();
        if (cameraPos.y > World.FIELD_HEIGHT - App.getGame().getWindowHeight())
            cameraPos.y = World.FIELD_HEIGHT - (float) App.getGame().getWindowHeight();
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

    public void draw() {

        for (int i = 0; i < troopsViews.size(); i++) {
            TroopView tv = troopsViews.get(i);
            if (!Troop.isAlive(tv.getTroop())) {
                tv.getTroop().kill();
                tv.exitParent();
                troopsViews.remove(i);
                i--;
            }
        }

        Troop.getTroops().forEach(troop -> {
            if (!troopsViews.contains(TroopView.getView(troop))) {
                troopsViews.add(new TroopView(troop, troopParent));
            }
        });

        troopsViews.forEach(c -> c.draw(cameraPos));
        castleViews.forEach(c -> c.draw(cameraPos));
    }

    public void clearAllContextualMenu() {
        getCastleViews().forEach(c -> c.setVisibleContextual(false));
    }

}
