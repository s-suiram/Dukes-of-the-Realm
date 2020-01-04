package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.World;
import game.logic.troop.Squad;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

/**
 * This class handle the squad view
 */
public class SquadView extends Group {

    private Squad o;
    private double lastAngle;
    private final Rectangle hitbox;

    public SquadView( Squad o) {
        this.o = o;
        hitbox = new Rectangle(0,0);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.BLACK);
        o.getTroops().forEach(troop -> {
            TroopView tv = new TroopView(troop);
            this.getChildren().add(tv);
            tv.setTranslateX(troop.getRelativeX());
            tv.setTranslateY(troop.getRelativeY());
        });
        this.lastAngle = o.getAngle();
        this.getChildren().add(hitbox);

    }


    protected void draw(Point2D cam) {
        hitbox.setHeight(o.getHitbox().getHeight());
        hitbox.setWidth(o.getHitbox().getWidth());
        this.setTranslateX(o.getHitbox().x - cam.x);
        this.setTranslateY(o.getHitbox().y - cam.y);

        this.getChildren().filtered(n -> n instanceof TroopView)
                .forEach(node -> ((TroopView) node).draw());

        if (o.dirChanged()) {
            this.setRotate(lastAngle - o.getAngle());
        }
    }

    public boolean isModelDead(){
        return !Squad.isAlive(o);
    }

}
