package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView {

    private static final int DOOR_WIDTH = (int) (Castle.WIDTH / 1.5);
    private static final int DOOR_HEIGHT = Castle.HEIGHT / 12;
    private Color col;
    private Castle model;
    private Rectangle door;
    private Group group;
    private ContextualMenuCastle contextualMenu;

    public CastleView(Castle c, Scene s) {
        this.model = c;
        this.group = new Group();
        contextualMenu = new ContextualMenuCastle(getModel());
        Rectangle rectangle = new Rectangle(c.getBoundingRect().getMinX(),
                c.getBoundingRect().getMinY(),
                c.getBoundingRect().getWidth(),
                c.getBoundingRect().getHeight());

        double doorOffset = rectangle.getWidth() / 2 - DOOR_WIDTH / 2.0;
        rectangle.setStroke(c.getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
        rectangle.setStrokeWidth(10);
        rectangle.setFill(Color.TRANSPARENT);

        switch (c.getDoor()) {
            case NORTH:
                door = new Rectangle(
                        doorOffset + rectangle.getX(),
                        rectangle.getY(),
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case SOUTH:
                door = new Rectangle(
                        doorOffset + rectangle.getX(),
                        rectangle.getY() + rectangle.getHeight() - DOOR_HEIGHT,
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case EAST:
                door = new Rectangle(
                        rectangle.getX() + rectangle.getWidth() - DOOR_HEIGHT,
                        rectangle.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
            case WEST:
                door = new Rectangle(
                        rectangle.getX(),
                        rectangle.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
        }

        contextualMenu.setTranslateX(rectangle.getX());
        contextualMenu.setTranslateY(rectangle.getY());

        group.getChildren().addAll(rectangle, door, contextualMenu);

        group.setOnMouseEntered(e -> s.setCursor(Cursor.HAND));

        group.setOnMouseClicked(event -> {
                    WorldView.getInstance().clearAllContextualMenu();
                    setVisibleContextual(true);
                }
        );
    }

    public void draw(Point2D cam) {
        group.setTranslateX(-cam.x);
        group.setTranslateY(-cam.y);
        contextualMenu.draw();
    }

    public Group getGroup() {
        return group;
    }

    public Castle getModel() {
        return model;
    }

    public ContextualMenuCastle getContextualMenu() {
        return contextualMenu;
    }

    public void setVisibleContextual(boolean b) {
        contextualMenu.setVisible(b);
    }
}
