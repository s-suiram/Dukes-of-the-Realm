package game.view;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collection;

/**
 * Represent a view with an hitbox
 */
public abstract class HitboxedGroup extends Group {
    /**
     * The group which contains this view
     */
    protected final Group parentRef;
    /**
     * The hitbox of the view
     */
    protected final Rectangle hitbox;
    /**
     * Is the view job done
     */
    protected boolean killed = false;

    /**
     * Build a Hitboxed Group
     *
     * @param parentRef the group which contains this view
     * @param hitbox    the hitbox of this view
     */
    public HitboxedGroup(Group parentRef, Rectangle hitbox) {
        this.parentRef = parentRef;
        this.addAllNodes(hitbox);
        hitbox.setStroke(Color.BLACK);
        hitbox.setFill(Color.TRANSPARENT);
        parentRef.getChildren().add(this);
        hitbox.toFront();
        this.hitbox = hitbox;
    }

    /**
     * Returns the state of this view
     *
     * @return the state of this view
     */
    public boolean getKilled() {
        return killed;
    }

    /**
     * Add a node to this view
     *
     * @param n the node to add
     */
    public void addNode(Node n) {
        this.getChildren().add(n);
    }

    /**
     * Remove a node from this view
     *
     * @param n the node to remove
     */
    public void removeNode(Node n) {
        this.getChildren().remove(n);
    }

    /**
     * Add multiple node to this view
     *
     * @param nodes the nodes to add
     */
    public void addAllNodes(Node... nodes) {
        this.getChildren().addAll(nodes);
    }

    /**
     * Add multiple node to this view
     *
     * @param n the nodes to add
     */
    public void addAllNodes(Collection<Node> n) {
        n.forEach(this::addNode);
    }

    /**
     * Remove all nodes from this view
     */
    public void clearNodes() {
        this.getChildren().clear();
    }

    /**
     * Check whether this view contains a node
     *
     * @param n the node
     * @return true if n belongs to this view, false otherwise
     */
    public boolean containsNode(Node n) {
        return this.getChildren().contains(n);
    }

    /**
     * A method which will be called in a loop
     *
     * @param cam the camera
     */
    public final void draw(Point2D cam) {
        drawImpl(cam);
    }

    /**
     * Remove this view from his parent
     */
    public void exitParent() {
        this.parentRef.getChildren().remove(this);
    }

    /**
     * THis method must be implemented and will be called by draw
     *
     * @param cam the camera
     */
    protected abstract void drawImpl(Point2D cam);
}
