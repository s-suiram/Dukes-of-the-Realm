package game.controller;

import com.sun.javafx.geom.Point2D;
import game.view.WorldView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;


public class MouseEventHandler implements EventHandler<MouseEvent> {

    private static  MouseEventHandler instance;

    private boolean dragged = false;
    private Point2D delta;
    private Point2D mouseDragStartPos;
    private Scene s;


    private MouseEventHandler(Scene s) {
        this.delta = new Point2D();
        this.mouseDragStartPos = new Point2D();
        s.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
        s.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
        this.s = s;
    }

    public static void init(Scene s) {
        instance = new MouseEventHandler(s);
    }

    public static MouseEventHandler getInstance() {
        if( instance == null){
            throw  new NullPointerException("instance not initialized");
        } else {
            return  instance;
        }
    }

    @Override
    public void handle(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
            if( !dragged){
                handleDragStart(event);
            } else {
                handleDrag(event);
            }
        }

        if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
            handleDragStop();
        }
    }

    private void handleDragStart(MouseEvent e) {
        dragged = true;
        mouseDragStartPos.x = (float) e.getX();
        mouseDragStartPos.y = (float) e.getY();
        s.setCursor(Cursor.CLOSED_HAND);
    }

    private void handleDrag(MouseEvent e) {
        WorldView.getInstance().move(-(int)delta.x, -(int)delta.y);
        delta.x = (float) (e.getX() - mouseDragStartPos.x);
        delta.y = (float) (e.getY() - mouseDragStartPos.y);
        WorldView.getInstance().move((int)delta.x, (int)delta.y);
    }

    private void handleDragStop() {
        dragged = false;
        delta.setLocation(0,0);
        s.setCursor(Cursor.DEFAULT);
    }
}
