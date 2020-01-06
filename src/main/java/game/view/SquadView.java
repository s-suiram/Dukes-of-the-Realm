package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Squad;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class handle the squad view
 */
public class SquadView extends Group {

    private Squad o;
    private double firstAngle;
    private final Rectangle hitbox;

    public SquadView( Squad o) {
        this.o = o;
        hitbox = new Rectangle(0,0);
        hitbox.setFill(Color.TRANSPARENT);
        hitbox.setStroke(Color.BLACK);
        o.getTroops().forEach(troop -> this.getChildren().add(new TroopView(troop)));
        this.firstAngle = o.getAngle();
       // this.getChildren().add(hitbox);

    }


    protected void draw(Point2D cam) {
        hitbox.setHeight(o.getHitbox().getHeight());
        hitbox.setWidth(o.getHitbox().getWidth());
        this.setTranslateX(o.getHitbox().x - cam.x);
        this.setTranslateY(o.getHitbox().y - cam.y);

        List<TroopView> tv = this.getChildren().stream()
                .map(t -> (TroopView)t)
                .collect(Collectors.toList());

        tv.forEach(TroopView::draw);

        if (o.dirChanged()) {
            int angle = (int) (firstAngle - o.getAngle());
            this.setRotate(angle);
            tv.forEach(t -> t.rotateTroop(-angle));
        }
    }

    public boolean isModelDead(){
        return o.isDead();
    }

}
