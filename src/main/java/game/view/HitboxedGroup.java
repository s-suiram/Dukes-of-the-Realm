package game.view;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Collection;
import java.util.Collections;


public abstract class HitboxedGroup extends Group {

    protected final Group parentRef;
    protected final Rectangle hitbox;
    protected boolean killed = false;

    public HitboxedGroup(Group parentRef, Rectangle hitbox) {
        this.parentRef = parentRef;
        this.addAllNodes(hitbox);
        hitbox.setStroke(Color.BLACK);
        hitbox.setFill(Color.TRANSPARENT);
        parentRef.getChildren().add(this);
        hitbox.toFront();
        this.hitbox = hitbox;
    }

    public boolean killed() {
        return killed;
    }

    public void addNode(Node n) {
        this.getChildren().add(n);
    }

    public void removeNode(Node n) {
        this.getChildren().remove(n);
    }

    public void addAllNodes(Node... nodes) {
        this.getChildren().addAll(nodes);
    }

    public void addAllNodes(Collection<Node> n) {
        n.forEach(this::addNode);
    }

    public void clearNodes() {
        this.getChildren().clear();
    }

    public boolean containsNode(Node n) {
        return this.getChildren().contains(n);
    }

    public final void draw(Point2D cam) {
        drawImpl(cam);
    }

    public void exitParent() {
        this.parentRef.getChildren().remove(this);
    }

    protected abstract void drawImpl(Point2D cam);
}
