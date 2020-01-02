package game.view;

import com.sun.javafx.geom.Point2D;
import game.App;
import game.logic.Cardinal;
import game.logic.Castle;
import game.logic.World;
import game.logic.troop.Squad;
import javafx.scene.Group;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class WorldView {

    private static WorldView instance = null;

    public final Point2D cameraPos;
    private int cameraSpeed = 10;
    private Set<CastleView> castleViews;
    private Set<SquadView> squadViews;
    private Group troopParent;

    private WorldView(Group castleParent, Group troopParent) {
        this.troopParent = troopParent;
        castleViews = new HashSet<>();
        squadViews = new HashSet<>();
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

    private void checkCameraBound() {
        int maxWidth = World.FIELD_WIDTH;
        int maxHeight = World.FIELD_HEIGHT;

        if (getMaxWidthWithContextual().isPresent()) {
            maxWidth = getMaxWidthWithContextual().get();
        }

        if (getMaxHeightWithContextual().isPresent()) {
            maxHeight = getMaxHeightWithContextual().get();
        }

        if (cameraPos.x < 0) cameraPos.x = 0;
        if (cameraPos.y < 0) cameraPos.y = 0;
        if (cameraPos.x > maxWidth - App.getGame().getWindowWidth())
            cameraPos.x = maxWidth - (float) App.getGame().getWindowWidth();
        if (cameraPos.y > maxHeight - App.getGame().getWindowHeight())
            cameraPos.y = maxHeight - (float) App.getGame().getWindowHeight();
    }

    public Optional<Integer> getMaxWidthWithContextual() {
        return castleViews.stream()
                .map(CastleView::getContextualMenu)
                .filter(Group::isVisible)
                .map(c -> c.getX() + c.getWidth() + 100 + (int) cameraPos.x)
                .filter(val -> val > World.FIELD_WIDTH)
                .max(Integer::compareTo);
    }

    public Optional<Integer> getMaxHeightWithContextual() {
        return castleViews.stream()
                .map(CastleView::getContextualMenu)
                .filter(Group::isVisible)
                .map(c -> c.getY() + c.getHeight() + 100 + (int) cameraPos.y)
                .filter(val -> val > World.FIELD_HEIGHT)
                .max(Integer::compareTo);
    }

    public void move(Cardinal direction) {
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
        cameraPos.y -= y;
        cameraPos.x -= x;
        checkCameraBound();
    }

    public void draw() {
        squadViews.removeIf(SquadView::killed);
        squadViews.addAll(Squad.getSquads().stream()
                .filter(Squad::viewNotDone)
                .map(o -> new SquadView(troopParent, o))
                .collect(Collectors.toSet())
        );
        troopParent.getChildren().retainAll(squadViews);
        castleViews.forEach(c -> c.draw(cameraPos));
        squadViews.forEach(o -> o.draw(cameraPos));
    }


    public void clearAllContextualMenu() {
        castleViews.forEach(c -> c.setVisibleContextual(false));
    }
}
