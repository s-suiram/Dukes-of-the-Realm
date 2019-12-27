package game.view;

import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ContextualMenuCastle extends Group {

    CastleView cv;
    private HBox menu;
    private Label money;

    public ContextualMenuCastle() {
        menu = null;
        money = null;
        cv = null;
    }

    public ContextualMenuCastle(CastleView c) {
        super();
        this.cv = c;
        money = new Label();
        menu = new HBox(money);
        getChildren().add(menu);
        Bounds b = c.getGroup().localToScene(c.getGroup().getBoundsInLocal());
        setTranslateX(b.getMinX());
        setTranslateY(b.getMinY());
        money.setText("Money : " + c.getC().getMoney());
    }

    public void draw() {
        money.setText("Money: " + cv.getC().getMoney());
    }

    public void clear() {
        money.setText("");
        setVisible(false);
    }
}
