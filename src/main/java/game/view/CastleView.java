package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.effect.Light;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView extends HitboxedGroup {

    private static final int DOOR_WIDTH = (int) (Castle.WIDTH / 1.5);
    private static final int DOOR_HEIGHT = Castle.HEIGHT / 12;
    private Color col;
    private Castle model;
    private Rectangle door;
    private ContextualMenuCastle contextualMenu;

    public CastleView(Castle c, Group parentRef) {
        super(parentRef);
        this.model = c;
        contextualMenu = new ContextualMenuCastle(getModel());
        Rectangle rectangle = new Rectangle(c.getBoundingRect().getMinX(),
                c.getBoundingRect().getMinY(),
                c.getBoundingRect().getWidth(),
                c.getBoundingRect().getHeight());

        double doorOffset = rectangle.getWidth() / 2 - DOOR_WIDTH / 2.0;
        rectangle.setStroke(c.getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
        final int thickness = 5;

        switch (c.getDoor()) {
            case NORTH:
                door = new Rectangle(
                        doorOffset + rectangle.getX(),
                        rectangle.getY() - thickness,
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case SOUTH:
                door = new Rectangle(
                        doorOffset + rectangle.getX(),
                        rectangle.getY() + rectangle.getHeight() - DOOR_HEIGHT + thickness,
                        DOOR_WIDTH,
                        DOOR_HEIGHT
                );
                break;
            case EAST:
                door = new Rectangle(
                        rectangle.getX() + rectangle.getWidth() - DOOR_HEIGHT + thickness,
                        rectangle.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
            case WEST:
                door = new Rectangle(
                        rectangle.getX() - thickness,
                        rectangle.getY() + doorOffset,
                        DOOR_HEIGHT,
                        DOOR_WIDTH
                );
                break;
        }

        contextualMenu.setTranslateX(rectangle.getX());
        contextualMenu.setTranslateY(rectangle.getY());

        this.addAllNodes(rectangle, door, contextualMenu);

        this.setOnMouseEntered(e -> parentRef.getScene().setCursor(Cursor.HAND));

        this.setOnMouseClicked(event -> {
            WorldView.getInstance().clearAllContextualMenu();
            this.toFront();
            setVisibleContextual(true);
                }
        );


    }

    @Override
    protected void drawImpl(Point2D cam) {
        this.setTranslateX(-cam.x);
        this.setTranslateY(-cam.y);
        contextualMenu.draw();
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
