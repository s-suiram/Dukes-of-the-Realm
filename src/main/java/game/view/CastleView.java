package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView {

    private static final int DOOR_WIDTH = (int) (Castle.WIDTH / 1.5);
    private static final int DOOR_HEIGHT = Castle.HEIGHT / 12;
    private static Group contextualMenu = new Group();
    private Color col;
    private Castle c;
    private Rectangle door;
    private Group group;

    public CastleView(Castle c) {
        this.c = c;
        this.group = new Group();

        Rectangle representation = new Rectangle(c.getBoundingRect().getMinX(),
                c.getBoundingRect().getMinY(),
                c.getBoundingRect().getWidth(),
                c.getBoundingRect().getHeight());

        double doorOffset = representation.getWidth() / 2 - DOOR_WIDTH / 2.0;
        representation.setStroke(c.getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
        representation.setStrokeWidth(10);
        representation.setFill(Color.TRANSPARENT);

        switch (c.getDoor()) {
            case NORTH:
                door = new Rectangle(
                        doorOffset + representation.getX(),
                        representation.getY(),
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case SOUTH:
                door = new Rectangle(
                        doorOffset + representation.getX(),
                        representation.getY() + representation.getHeight() - DOOR_HEIGHT,
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case EAST:
                door = new Rectangle(
                        representation.getX() + representation.getWidth() - DOOR_HEIGHT,
                        representation.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
            case WEST:
                door = new Rectangle(
                        representation.getX(),
                        representation.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
        }
        group.getChildren().addAll(representation, door);
        group.autosize();
        group.setPickOnBounds(true);
        group.setOnMouseClicked(event -> {
                    clearContextualMenu();
                    contextualMenu.getChildren().add(new ContextualMenuCastle(this));
                }
        );
    }

    public static Group getContextualMenu() {
        return contextualMenu;
    }

    public static void clearContextualMenu() {
        getContextualMenu().getChildren().forEach(c -> c.setVisible(false));
        getContextualMenu().getChildren().clear();
    }

    public void draw(Point2D cam) {
        group.setTranslateX(-cam.x);
        group.setTranslateY(-cam.y);
        if (!contextualMenu.getChildren().isEmpty())
            ((ContextualMenuCastle) contextualMenu.getChildren().get(0)).draw();
    }

    public Group getGroup() {
        return group;
    }

    public Castle getC() {
        return c;
    }
}
