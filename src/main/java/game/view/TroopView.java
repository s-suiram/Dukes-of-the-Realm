package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class TroopView extends HitboxedGroup implements Observer {

    private final Troop t;

    public TroopView(Troop t, Group parentRef) {
        super(parentRef , new Rectangle(-Troop.RADIUS, -Troop.RADIUS, Troop.DIAMETER,Troop.DIAMETER));
        this.t = t;
        Circle background = new Circle(t.getCenterPos().x, t.getCenterPos().y, Troop.RADIUS);
        background.setFill(t instanceof Pikeman ? Color.RED : t instanceof Onager ? Color.BLUE : Color.WHITE);
        this.getChildren().addAll(background);
        t.addObserver(this);
        t.setViewDone();
    }

    public Troop getTroop() {
        return t;
    }

    @Override
    protected void drawImpl(Point2D cam) {
        this.setTranslateX(t.getCenterPos().x - cam.x);
        this.setTranslateY(t.getCenterPos().y - cam.y);
    }

    @Override
    public void update(Observable o, Object arg) {
        this.killed = true;
    }
}
