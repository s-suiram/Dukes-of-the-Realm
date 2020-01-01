package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import game.logic.Player;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView extends HitboxedGroup {

    private static final int DOOR_WIDTH = (int) (Castle.WIDTH / 1.5);
    private static final int DOOR_HEIGHT = Castle.HEIGHT / 12;
    private static CastleView selected;
    private Castle model;
    private Rectangle door;
    private ContextualMenuCastle contextualMenu;
    private Rectangle rectangle;

    public CastleView(Castle c, Group parentRef) {
        super(parentRef, new Rectangle(0, 0, 0, 0));
        this.model = c;
        contextualMenu = new ContextualMenuCastle(this);
        rectangle = new Rectangle(0, 0, Castle.WIDTH, Castle.HEIGHT);

        double doorOffset = rectangle.getWidth() / 2 - DOOR_WIDTH / 2.0;
        final int thickness = 5;
        rectangle.setStrokeWidth(thickness * 2);

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

        addAllNodes(rectangle, door, contextualMenu);

        setOnMouseEntered(e -> parentRef.getScene().setCursor(Cursor.HAND));

        setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (selected == this) selected = null;
                        else selected = this;
                    }
                    if (event.getButton() == MouseButton.SECONDARY) {
                        WorldView.getInstance().clearAllContextualMenu();
                        this.toFront();
                        setVisibleContextual(true);
                    }
                }
        );
    }

    public static CastleView getSelected() {
        return selected;
    }

    @Override
    protected void drawImpl(Point2D cam) {
        if (this == getSelected()) {
            rectangle.setFill(new Color(1.0, 1.0, 1.0, 0.4));
        } else {
            rectangle.setFill(Color.TRANSPARENT);
        }
        this.setTranslateX(model.getBoundingRect().x - cam.x);
        this.setTranslateY(model.getBoundingRect().y - cam.y);
        if (getModel().getOwner() == Player.getPlayer()) {
            rectangle.setStroke(Color.GOLD);
        } else {
            rectangle.setStroke(getModel().getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
        }
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
