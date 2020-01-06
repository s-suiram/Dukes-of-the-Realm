package game.view;

import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import game.logic.utils.SingleRunHandler;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;

/**
 * This class handle the troop view
 */
public class TroopView extends Group {

    private final Troop t;
    private Circle background;
    private Line l1, l2;
    private SingleRunHandler srh;

    public TroopView(Troop t) {
        this.t = t;
        l1 = new Line(-Troop.RADIUS, -Troop.RADIUS, Troop.DIAMETER, Troop.DIAMETER);
        l2 = new Line(-Troop.RADIUS, Troop.RADIUS, Troop.RADIUS, -Troop.RADIUS);
        background = new Circle(0, 0, Troop.RADIUS);
        background.setFill(t instanceof Pikeman ? Color.RED : t instanceof Onager ? Color.BLUE : Color.WHITE);
        Text text = new Text(t instanceof Pikeman ? "P" : t instanceof Onager ? "O" : "K");
        this.getChildren().addAll(new StackPane(background, text, l1, l2));
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

    private void onDeath() {
        l1.setVisible(true);
        l2.setVisible(true);
        l1.toFront();
        l2.toFront();
    }


}
