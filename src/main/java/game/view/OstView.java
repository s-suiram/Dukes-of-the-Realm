package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Ost;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Observable;
import java.util.Observer;

public class OstView extends HitboxedGroup implements Observer {

    private Ost o;

    public OstView(Group parentRef, Ost o) {
        super(parentRef, new Rectangle());
        this.o = o;
        Point2D p = o.getCenter();
        o.addObserver(this);
        o.setViewDone();
    }

    @Override
    protected void drawImpl(Point2D cam) {
        hitbox.setStrokeWidth(3);
        hitbox.setStroke(Color.BLACK);
        super.hitbox.setHeight(o.getShield().height);
        super.hitbox.setWidth(o.getShield().width);
        this.setTranslateX(o.getShield().x - cam.x);
        this.setTranslateY(o.getShield().y - cam.y);

    }

    @Override
    public void update(Observable o, Object arg) {
        this.killed = true;
    }
}
