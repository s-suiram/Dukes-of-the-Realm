package game.view;

import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import game.logic.utils.Point;
import game.logic.utils.SingleRunHandler;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

import java.util.HashMap;

/**
 * This class handle the troop view
 */
public class TroopView extends Group {

    private static Image p, k, o;

    static {
        p = new Image("file:resources/pikeman.png");
        k = new Image("file:resources/knight.png");
        o = new Image("file:resources/onager.png");
    }

    private final Troop t;
    private Line l1, l2;
    private SingleRunHandler srh;
    private ImageView view;
    private Image i;

    public TroopView(Troop t) {
        this.t = t;
        l1 = new Line(-Troop.RADIUS, -Troop.RADIUS, Troop.DIAMETER, Troop.DIAMETER);
        l2 = new Line(-Troop.RADIUS, Troop.RADIUS, Troop.RADIUS, -Troop.RADIUS);
        l2.setStrokeWidth(3);
        l1.setStrokeWidth(3);
        i = t instanceof Pikeman ? p  : t instanceof Onager ? o : k;
        view = new ImageView(i);
        view.setFitWidth(30);
        view.setFitHeight(30);
        view.setX(-15);
        view.setY(-15);
        this.getChildren().addAll( l1, l2,view);
        l1.setVisible(false);
        l2.setVisible(false);
        srh = new SingleRunHandler();

    }

    public Troop getTroop() {
        return t;
    }

    protected void draw() {
        this.setTranslateX(t.getRelativeX());
        this.setTranslateY(t.getRelativeY());
        if (t.isDead())
            srh.doOnce(this::onDeath, "onDeath");
    }

    protected  void rotateTroop(int angle){
        view.setRotate(angle);
    }

    private void onDeath() {
        l1.setVisible(true);
        l2.setVisible(true);
        l1.toFront();
        l2.toFront();
    }


}
