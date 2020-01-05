package game.controller;

import com.sun.javafx.geom.Point2D;
import game.view.WorldView;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;

/**
 * This singleton handle the mouse gestures
 */
public class MouseEventHandler implements EventHandler<MouseEvent> {
    /**
     * The instance
     */
    private static MouseEventHandler instance;
    /**
     * True if being dragging, false otherwise
     */
    private boolean dragged = false;
    private Point2D delta;
    /**
     * The source of the dragging
     */
    private Point2D mouseDragStartPos;
    /**
     * The current scene
     */
    private Scene s;
    /**
     * The current mouse position
     */
    private Point2D mousePos;

    /**
     * Build a MouseEventHandler
     *
     * @param s the current scene
     */
    private MouseEventHandler(Scene s) {
        this.delta = new Point2D();
        mousePos = new Point2D();
        this.mouseDragStartPos = new Point2D();
        s.addEventFilter(MouseEvent.MOUSE_DRAGGED, this);
        s.addEventFilter(MouseEvent.MOUSE_RELEASED, this);
        s.addEventFilter(MouseEvent.MOUSE_MOVED, this);
        this.s = s;
    }

    /**
     * Initialize the singleton
     *
     * @param s the scene for the constructor
     */
    public static void init(Scene s) {
        instance = new MouseEventHandler(s);
    }

    /**
     * Returns the instance of the singleton
     *
     * @return the instance of the singleton
     * @throws NullPointerException if init() is not called before getInstance()
     */
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

    /**
     * Returns the mouse position
     *
     * @return the mouse position
     */
    public Point2D getMousePos() {
        return mousePos;
    }

    private void handleMouseMoved(MouseEvent e) {
        mousePos.x = (float) e.getX();
        mousePos.y = (float) e.getY();
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
        s.setCursor(Cursor.OPEN_HAND);
    }
}
