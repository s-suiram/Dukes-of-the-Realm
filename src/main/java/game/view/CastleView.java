package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import game.logic.Player;
import game.logic.World;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Handle the view of a castle
 */
public class CastleView extends Group {
    /**
     * Width of the door
     */
    private static final int DOOR_WIDTH = (int) (Castle.SIZE / 1.5);
    /**
     * Height of the door
     */
    private static final int DOOR_HEIGHT = Castle.SIZE / 12;
    /**
     * The currently selected castle
     */
    private static CastleView selected;
    /**
     * The model attached to this view
     */
    private Castle model;
    /**
     * Representation of the door
     */
    private Rectangle door;
    /**
     * The contextual menu which appear on right click on a castle
     */
    private ContextualMenuCastle contextualMenu;
    /**
     * Rectangle which represent a castle
     */
    private Rectangle rectangle;

    /**
     * Create a new castle view
     *
     * @param c         the model attached to this view
     */
    public CastleView(Castle c) {
        this.model = c;
        contextualMenu = new ContextualMenuCastle(this);
        rectangle = new Rectangle(0, 0, Castle.SIZE, Castle.SIZE);

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

        contextualMenu.setTranslateX(rectangle.getX() - 20);
        contextualMenu.setTranslateY(rectangle.getY() - 20);

        this.getChildren().addAll(rectangle, door, contextualMenu);

        rectangle.setOnMouseEntered(e -> this.getScene().setCursor(Cursor.HAND));

        rectangle.setOnMouseClicked(event -> {
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

    /**
     * Returns the selected castle
     *
     * @return the selected castle
     */
    public static CastleView getSelected() {
        return selected;
    }

    protected void draw(Point2D cam) {
        if (this == getSelected()) {
            rectangle.setFill(new Color(1.0, 1.0, 1.0, 0.4));
        } else {
            rectangle.setFill(Color.TRANSPARENT);
        }
        this.setTranslateX(model.getBoundingRect().x - cam.x);
        this.setTranslateY(model.getBoundingRect().y - cam.y);
        if (getModel().getOwner() == World.getInstance().getPlayer()) {
            rectangle.setStroke(Color.GOLD);
        } else {
            rectangle.setStroke(getModel().getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
        }
        contextualMenu.draw();
    }

    /**
     * Returns the model
     *
     * @return the model
     */
    public Castle getModel() {
        return model;
    }

    /**
     * Returns the contextual menu
     *
     * @return the contextual menu
     */
    public ContextualMenuCastle getContextualMenu() {
        return contextualMenu;
    }

    /**
     * Set the visibility of the contextual menu
     *
     * @param b the value of visibility
     */
    public void setVisibleContextual(boolean b) {
        contextualMenu.setVisible(b);
    }

}
