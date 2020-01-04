package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Squad;
import game.logic.troop.Troop;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.util.Observable;
import java.util.Observer;

/**
 * This class handle the troop view
 */
public class TroopView extends Group {

    private final Troop t;

    public TroopView(Troop t) {
        this.t = t;
        Circle background = new Circle(0, 0, Troop.RADIUS);
        background.setFill(t instanceof Pikeman ? Color.RED : t instanceof Onager ? Color.BLUE : Color.WHITE);
        this.getChildren().addAll(background);
    }

    public Troop getTroop() {
        return t;
    }

    protected void draw() {
        this.setTranslateX(t.getRelativeX());
        this.setTranslateY(t.getRelativeY());
    }


}
