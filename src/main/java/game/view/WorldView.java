package game.view;

import com.sun.javafx.geom.Point2D;
import game.App;
import game.logic.Cardinal;
import game.logic.Castle;
import game.logic.World;
import game.logic.troop.Squad;
import javafx.scene.Group;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * This singleton handle all the view
 */
public class WorldView {
    /**
     * The instance
     */
    private static WorldView instance = null;
    /**
     * The camera position
     */
    public final Point2D cameraPos;
    /**
     * The camera speed
     */
    private int cameraSpeed = 10;
    /**
     * The castle views
     */
    private Set<CastleView> castleViews;
    /**
     * The Squad view
     */
    private HashMap<Squad,SquadView> squadViewMap;
    /**
     * The parent which contains
     */
    private Group troopParent;


    /**
     * Build a world view
     *
     * @param castleParent the group which contains the castles
     * @param troopParent  the group which contains the troops
     */
    private WorldView(Group castleParent, Group troopParent) {
        this.troopParent = troopParent;
        castleViews = new HashSet<>();
        squadViewMap = new HashMap<>();
        cameraPos = new Point2D(0, 0);
        Castle.getCastles().forEach(c -> castleViews.add(new CastleView(c)));
        castleParent.getChildren().addAll(castleViews);
    }

    /**
     * Returns the instance
     *
     * @return the instance
     */
    public static WorldView getInstance() {
        if (instance == null) throw new NullPointerException("instance not initialized");
        return instance;
    }

    /**
     * Initialize the singleton
     *
     * @param castleParent the group which contains the castles
     * @param troopParent  the group which contains the troops
     */
    public static void init(Group castleParent, Group troopParent) {
        instance = new WorldView(castleParent, troopParent);
    }

    /**
     * Returns the camera speed
     *
     * @return the camera speed
     */
    public int getCameraSpeed() {
        return cameraSpeed;
    }

    /**
     * Sets the camera speed
     *
     * @param cameraSpeed the new value
     */
    public void setCameraSpeed(int cameraSpeed) {
        this.cameraSpeed = cameraSpeed;
        if (this.cameraSpeed < 1) this.cameraSpeed = 1;
    }

    /**
     * Decrement the camera speed
     */
    public void decreaseCameraSpeed() {
        setCameraSpeed(cameraSpeed - 1);
    }

    /**
     * Increment the camera speed
     */
    public void increaseCameraSpeed() {
        setCameraSpeed(cameraSpeed + 1);
    }

    /**
     * Reset the camera speed to the default value
     */
    public void resetCameraSpeed() {
        setCameraSpeed(10);
    }

    /**
     * Check that the camera doesn't go beyond the limit of the field
     */
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

    private Optional<Integer> getMaxWidthWithContextual() {
        return castleViews.stream()
                .map(CastleView::getContextualMenu)
                .filter(Group::isVisible)
                .map(c -> c.getX() + c.getWidth() + 100 + (int) cameraPos.x)
                .filter(val -> val > World.FIELD_WIDTH)
                .max(Integer::compareTo);
    }

    private Optional<Integer> getMaxHeightWithContextual() {
        return castleViews.stream()
                .map(CastleView::getContextualMenu)
                .filter(Group::isVisible)
                .map(c -> c.getY() + c.getHeight() + 100 + (int) cameraPos.y)
                .filter(val -> val > World.FIELD_HEIGHT)
                .max(Integer::compareTo);
    }

    /**
     * Move the camera in a direction
     *
     * @param direction the direction to move the camera
     */
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

    /**
     * Move the camera
     *
     * @param x x offset
     * @param y y offset
     */
    public void move(int x, int y) {
        cameraPos.y -= y;
        cameraPos.x -= x;
        checkCameraBound();
    }

    /**
     * Method called each frame
     */
    public void draw() {

        troopParent.getChildren().removeIf(node -> ((SquadView) node).isModelDead());

        Squad.getSquads().forEach(squad -> {
            if(!Squad.isAlive(squad))
                squadViewMap.remove(squad);
            if(!squadViewMap.containsKey(squad)){
                SquadView sv = new SquadView(squad);
                squadViewMap.put(squad,sv);
                troopParent.getChildren().add(sv);
            }
        });

        castleViews.forEach(c -> c.draw(cameraPos));
        troopParent.getChildren().forEach(o -> ((SquadView)o).draw(cameraPos));
    }

    /**
     * Set invisible all the contextual menus
     */
    public void clearAllContextualMenu() {
        castleViews.forEach(c -> c.setVisibleContextual(false));
    }
}
