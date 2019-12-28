package game.view;

import com.sun.javafx.geom.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public abstract class HitboxedGroup extends Group {

    protected final Rectangle hitbox;
    protected final Rectangle centerPoint;
    private final Group parentRef;
    private boolean hbhidden = false;

    public HitboxedGroup(Group parentRef) {
        this.parentRef = parentRef;
        hitbox = new Rectangle(0, 0, 0, 0);
        centerPoint = new Rectangle(1, 1, 0, 0);
        centerPoint.setFill(Color.BLACK);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.BLACK);
        addAllNodes(hitbox,centerPoint);
        parentRef.getChildren().add(this);
    }

    protected void defineHitbox(){
        hitbox.setX(getLayoutBounds().getMinX());
        hitbox.setY(getLayoutBounds().getMinY());
        hitbox.setWidth(getLayoutBounds().getWidth());
        hitbox.setHeight(getLayoutBounds().getHeight());
    }

    public double getWidth(){
        return getLayoutBounds().getWidth();
    }

    public  double getHeight(){
        return getLayoutBounds().getHeight();
    }

    public double getX() {
        return getLayoutBounds().getMinX();
    }

    public double getY() {
        return getLayoutBounds().getMinY();
    }

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
        drawImpl(cam);
    }

    public void exitParent() {
        this.parentRef.getChildren().remove(this);
    }

    protected abstract void drawImpl(Point2D cam);

}
