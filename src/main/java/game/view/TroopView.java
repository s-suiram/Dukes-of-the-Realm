package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;

public class TroopView extends HitboxedGroup {

    public static final HashMap<Troop, TroopView> troopToView = new HashMap<>();

    private final Troop t;

    public TroopView(Troop t, Group parentRef) {
        super(parentRef);
        this.t = t;
        Circle background = new Circle(0, 0, Troop.SIZE / 2.0);
        Text name = new Text(t instanceof Pikeman ? "P" : t instanceof Onager ? "O" : "K");
        name.setStroke(Color.BLACK);
        //name.setTranslateY(Troop.SIZE-1);
        //name.setTranslateX(2);
        name.setTextAlignment(TextAlignment.CENTER);
        background.setFill(t instanceof Pikeman ? Color.RED : t instanceof Onager ? Color.BLUE : Color.WHITE);
        this.getChildren().addAll(background, name);
        //this.setAlignment(Pos.CENTER);
        troopToView.put(t, this);
    }

    public static TroopView getView(Troop t) {
        return troopToView.get(t);
    }

    public Troop getTroop() {
        return t;
    }

    @Override
    protected void drawImpl(Point2D cam) {
        this.setTranslateX(t.getPos().x - Troop.SIZE / 2.0 - cam.x);
        this.setTranslateY(t.getPos().y - Troop.SIZE - cam.y);
    }


}
