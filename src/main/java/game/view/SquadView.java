package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Squad;
import javafx.scene.Group;
import javafx.scene.shape.Rectangle;

import java.util.Observable;
import java.util.Observer;

/**
 * This class handle the squad view
 */
public class SquadView extends HitboxedGroup implements Observer {

    private Squad o;
    private double lastAngle;

    public SquadView(Group parentRef, Squad o) {
        super(parentRef, new Rectangle());
        this.o = o;
        o.addObserver(this);
        o.setViewDone();
        o.getTroops().forEach(troop -> {
            TroopView tv = new TroopView(troop, this);
            tv.setTranslateX(troop.getCenterPos().x);
            tv.setTranslateY(troop.getCenterPos().y);
        });
        this.lastAngle = o.getAngle();
    }

    @Override
    protected void drawImpl(Point2D cam) {

        hitbox.setHeight(o.getHitbox().getHeight());
        hitbox.setWidth(o.getHitbox().getWidth());
        this.setTranslateX(o.getHitbox().x - cam.x);
        this.setTranslateY(o.getHitbox().y - cam.y);
        getChildren().filtered(c -> c instanceof TroopView).forEach(node -> {
            TroopView t = ((TroopView) node);
            t.setTranslateX(t.getTroop().getRelativeX());
            t.setTranslateY(t.getTroop().getRelativeY());
        });

        if (o.dirChanged()) {
            this.setRotate(lastAngle - o.getAngle());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.killed = true;
    }
}
