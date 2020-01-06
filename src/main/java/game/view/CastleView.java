package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import game.logic.World;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private static Image north = new Image("file:resources/castle-north.png", Castle.SIZE, Castle.SIZE, true, true);
    private static Image south = new Image("file:resources/castle-south.png", Castle.SIZE, Castle.SIZE, true, true);
    private static Image east = new Image("file:resources/castle-east.png", Castle.SIZE, Castle.SIZE, true, true);
    private static Image west = new Image("file:resources/castle-west.png", Castle.SIZE, Castle.SIZE, true, true);
    /**
     * The currently selected castle
     */
    private static CastleView selected;
    /**
     * The model attached to this view
     */
    private Castle model;
    /**
     * The contextual menu which appear on right click on a castle
     */
    private ContextualMenuCastle contextualMenu;
    /**
     * Rectangle which represent a castle
     */
    private ImageView img;

    private Rectangle rect;

    /**
     * Create a new castle view
     *
     * @param c the model attached to this view
     */
    public CastleView(Castle c) {
        this.model = c;
        contextualMenu = new ContextualMenuCastle(this);

        switch (model.getDoor()) {
            case NORTH:
                img = new ImageView(north);
                break;
            case SOUTH:
                img = new ImageView(south);
                break;
            case EAST:
                img = new ImageView(east);
                break;
            case WEST:
                img = new ImageView(west);
                break;
        }

        rect = new Rectangle(model.getBoundingRect().getWidth(), model.getBoundingRect().getHeight());

        contextualMenu.setTranslateX(img.getX() - 20);
        contextualMenu.setTranslateY(img.getY() - 20);

        this.getChildren().addAll(img, rect, contextualMenu);
        rect.setStrokeWidth(5);
        rect.setOnMouseEntered(e -> this.getScene().setCursor(Cursor.HAND));

        rect.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.PRIMARY) {
                        if (selected == this) selected = null;
                        else selected = this;
                    }
                    if (event.getButton() == MouseButton.SECONDARY) {
                        if(model.getOwner() == World.getInstance().getPlayer()) {
                            WorldView.getInstance().clearAllContextualMenu();
                            this.toFront();
                            setVisibleContextual(true);
                        }
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
            rect.setFill(new Color(1.0, 1.0, 1.0, 0.4));
        } else {
            rect.setFill(Color.TRANSPARENT);
        }
        this.setTranslateX(model.getBoundingRect().x - cam.x);
        this.setTranslateY(model.getBoundingRect().y - cam.y);
        if (getModel().getOwner() == World.getInstance().getPlayer()) {
            rect.setStroke(Color.GOLD);
        } else {
            rect.setStroke(getModel().getOwner() instanceof NeutralDukes ? Color.DARKGRAY : Color.RED);
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
