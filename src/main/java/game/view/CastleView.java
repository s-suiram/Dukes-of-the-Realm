package game.view;

import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class CastleView {

    private static final int DOOR_WIDTH = 100;
    private static final int DOOR_HEIGHT = 30;

    private Color col;
    private Rectangle representation;
    private Castle c;
    private Rectangle door;
    private Group group;

    public CastleView(Castle c) {
        this.c = c;
        this.group = new Group();
        representation = new Rectangle(c.getBoundingRect().getMinX(),
                c.getBoundingRect().getMinY(),
                c.getBoundingRect().getWidth(),
                c.getBoundingRect().getHeight());

        double doorOffset = representation.getWidth() / 2 - DOOR_WIDTH / 2.0;
        representation.setFill(c.getOwner() instanceof NeutralDukes ? Color.GREY : Color.RED);
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
        group.getChildren().addAll(representation,door);
        door.toFront();
    }

    public Group getRepresentation() {
        return  group;
    }
}
