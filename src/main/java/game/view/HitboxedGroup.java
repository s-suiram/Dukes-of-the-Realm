package game.view;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public abstract class HitboxedGroup extends Group {

    private final Rectangle hitbox;
    private final Rectangle centerPoint;
    private final Group parentRef;
    private boolean hbhidden = false;

    public HitboxedGroup(Group parentRef) {
        this.parentRef = parentRef;
        hitbox = new Rectangle(0, 0, 0, 0);
        centerPoint = new Rectangle(1, 1, 0, 0);
        centerPoint.setFill(Color.BLACK);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.BLACK);
        this.getChildren().addAll(hitbox, centerPoint);
        parentRef.getChildren().add(this);
    }

//    public void setXbyCenter(double x) {
//        this.setLayoutX(x - this.getWidth() / 2);
//    }
//
//    public void setYbyCenter(double y) {
//        this.setLayoutX(y - this.getHeight() / 2);
//    }

    public void showHitBox() {
        hitbox.setVisible(false);
        centerPoint.setVisible(false);
        hbhidden = false;
    }

    public void hideHitBox() {
        hitbox.setVisible(true);
        centerPoint.setVisible(true);
        hbhidden = true;
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

    public void clearNodes() {
        this.getChildren().clear();
    }

    public boolean containsNode(Node n) {
        return this.getChildren().contains(n);
    }

    public final void draw(Point2D cam) {
//        centerPoint.setX(this.getWidth() / 2);
//        centerPoint.setY(this.getHeight() / 2);
//        System.out.println(this.getWidth());
        drawImpl(cam);
    }

    private void updateVisibility() {
        if (hbhidden) {
            hideHitBox();
        } else {
            showHitBox();
        }
    }

    public void exitParent() {
        this.parentRef.getChildren().remove(this);
    }

    protected abstract void drawImpl(Point2D cam);

}
