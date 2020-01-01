package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Cardinal;
import game.logic.Castle;
import game.logic.World;
import game.logic.troop.Onager;
import game.logic.troop.Ost;
import game.logic.troop.Troop;
import javafx.scene.Group;

import java.util.*;
import java.util.stream.Collectors;

public class WorldView {

    private static WorldView instance = null;
    public final Point2D cameraPos;
    private int cameraSpeed = 10;
    private Set<CastleView> castleViews;
    private Set<OstView> ostViews;
    private boolean cameraMoved = false;
    private Group castleParent;
    private Group troopParent;

    private WorldView(Group castleParent, Group troopParent) {
        this.castleParent = castleParent;
        this.troopParent = troopParent;
        castleViews = new HashSet<>();
        ostViews = new HashSet<>();
        cameraPos = new Point2D(0, 0);
        Castle.getCastles().forEach(c -> castleViews.add(new CastleView(c, castleParent)));

    }

    public static WorldView getInstance() {
        if (instance == null) throw new NullPointerException("instance not initialized");
        return instance;
    }

    public static void init(Group castleParent, Group troopParent) {
        instance = new WorldView(castleParent, troopParent);
    }


    public int getCameraSpeed() {
        return cameraSpeed;
    }

    public void setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        if (this.cameraSpeed < 1) this.cameraSpeed = 1;
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
    }

    public void move(int x, int y) {
        cameraMoved = true;
        cameraPos.y -= y;
        cameraPos.x -= x;
        checkCameraBound();
    }

    public void draw() {
        ostViews.removeIf(OstView::killed);
        ostViews.addAll(Ost.getOsts().stream()
                .filter(Ost::viewNotDone)
                .map(o -> new OstView(troopParent, o))
                .collect(Collectors.toSet())
        );
        ostViews.forEach(ostView ->
            ostView.getChildren().removeIf(node ->
                    node instanceof TroopView && ((TroopView) node).killed()
            ));

        castleViews.forEach(c -> c.draw(cameraPos));
        ostViews.forEach(o -> o.draw(cameraPos));
    }


    public void clearAllContextualMenu() {
        castleViews.forEach(c -> c.setVisibleContextual(false));
    }
}
