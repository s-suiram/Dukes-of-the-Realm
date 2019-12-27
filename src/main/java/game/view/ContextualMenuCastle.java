package game.view;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ContextualMenuCastle extends Group {
    private HBox menu;
    private Label money;
    private boolean consumed;

    public ContextualMenuCastle(CastleView c) {
        super();
        consumed = false;
        money = new Label();
        menu = new HBox(money);
        getChildren().add(menu);
        setTranslateX(50);
        setTranslateY(50);
        money.setText("Money : " + c.getC().getMoney());
    }

    public HBox getRepresentation() {
        return menu;
    }

    public void consume() {
        consumed = true;
    }

    public boolean isConsumed() {
        return consumed;
    }
}
