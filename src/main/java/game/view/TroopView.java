package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.HashMap;

public class TroopView extends Group {

    public static final HashMap<Troop, TroopView> troopToView = new HashMap<>();

    private final Troop t;

    public TroopView(Troop t) {
        this.t = t;
        Circle background = new Circle(Troop.SIZE / 2.0, Troop.SIZE / 2.0, Troop.SIZE / 2.0);
        background.setFill(t instanceof Pikeman ? Color.RED : t instanceof Onager ? Color.BLUE : Color.BLACK);
        this.getChildren().add(background);
        troopToView.put(t, this);
    }

    public static TroopView getView(Troop t) {
        return troopToView.get(t);
    }

    public void draw(Point2D cam) {
        this.setTranslateX(t.getPos().x - Troop.SIZE / 2.0 - cam.x);
        this.setTranslateY(t.getPos().y - Troop.SIZE / 2.0 - cam.y);
    }

}
