package game.controller;

import com.sun.javafx.geom.Point2D;
import game.view.WorldView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;


public class MouseEventHandler implements EventHandler<MouseEvent> {

    private static MouseEventHandler instance;

    private boolean dragged = false;
    private Point2D delta;
    private Point2D mouseDragStartPos;
    private Scene s;

    private Point2D mousePos;


    private MouseEventHandler(Scene s) {
        this.delta = new Point2D();
        this.mouseDragStartPos = new Point2D();
        s.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
        s.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
        s.addEventFilter(MouseEvent.MOUSE_MOVED, this);
        this.s = s;
    }

    public static void init(Scene s) {
        instance = new MouseEventHandler(s);
    }

    public static MouseEventHandler getInstance() {
        if (instance == null) {
            throw new NullPointerException("instance not initialized");
        } else {
            return instance;
        }
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if (!dragged) {
                handleDragStart(event);
            } else {
                handleDrag(event);
            }
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            handleDragStop();
        }

        if (event.getEventType() == MouseEvent.MOUSE_MOVED) {
            handleMouseMoved(event);
        }
    }

    private void handleMouseMoved(MouseEvent e) {
        mousePos.x = (float) e.getX();
        mousePos.y = (float) e.getY();
        handleCameraMoving(e);
    }

    private void handleCameraMoving(MouseEvent e) {

    }

    private void handleDragStart(MouseEvent e) {
        dragged = true;
        mouseDragStartPos.x = (float) e.getX();
        mouseDragStartPos.y = (float) e.getY();
        s.setCursor(Cursor.CLOSED_HAND);
    }

    private void handleDrag(MouseEvent e) {
        WorldView.getInstance().move(-(int) delta.x, -(int) delta.y);
        delta.x = (float) (e.getX() - mouseDragStartPos.x);
        delta.y = (float) (e.getY() - mouseDragStartPos.y);
        WorldView.getInstance().move((int) delta.x, (int) delta.y);
    }

    private void handleDragStop() {
        dragged = false;
        delta.setLocation(0, 0);
        s.setCursor(Cursor.DEFAULT);
    }
}
