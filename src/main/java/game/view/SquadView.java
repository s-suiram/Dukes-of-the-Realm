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
        hitbox.setHeight(o.getShield().height);
        hitbox.setWidth(o.getShield().width);
        getChildren().filtered(c -> c instanceof TroopView).forEach(node -> {
            TroopView t = ((TroopView) node);
            t.setTranslateX(t.getTroop().getRelativeX());
            t.setTranslateY(t.getTroop().getRelativeY());
        });
        this.setTranslateX(o.getShield().x - cam.x);
        this.setTranslateY(o.getShield().y - cam.y);

        if (o.dirChanged()) {
            this.setRotate(lastAngle - o.getAngle());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        this.killed = true;
    }
}
