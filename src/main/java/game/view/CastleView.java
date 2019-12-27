package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;
import game.logic.NeutralDukes;
import javafx.event.EventType;
import javafx.scene.Group;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.input.MouseEvent;



public class CastleView {

    private static final int DOOR_WIDTH = (int) (Castle.WIDTH / 1.5);
    private static final int DOOR_HEIGHT = Castle.HEIGHT / 12;

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
        //group.setPrefWidth(Castle.WIDTH);
        //group.setPrefHeight(Castle.HEIGHT);
        group.setTranslateX(c.getBoundingRect().getMinX());
        group.setTranslateY(c.getBoundingRect().getMinY());
        System.out.println(c.getBoundingRect().getMinX());
        group.autosize();
        group.setPickOnBounds(true);
       // System.out.println(group.getTranslateX() + " " + group.getTranslateY() + " " + group.getWidth() + " " + group.getHeight());
        group.setOnMouseClicked(event -> {
            System.out.println("nigga");
            System.out.println(event.getSource().getClass().toString());
        });
    }

    public Group getRepresentation(Point2D cam) {
        group.setTranslateX( c.getBoundingRect().getMinX() - cam.x);
        group.setTranslateY( c.getBoundingRect().getMinY() - cam.y);
        System.out.println(c.getBoundingRect().getMinX() + cam.x);
        return  group;
    }

    public Castle getC() {
        return c;
    }
}
