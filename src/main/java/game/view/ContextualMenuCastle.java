package game.view;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

public class ContextualMenuCastle {
    private HBox menu;
    private Label money;

    public ContextualMenuCastle(CastleView c) {
        money = new Label();
        menu = new HBox(money);

        money.setText("Money : " + c.getC().getMoney());
    }

    public HBox getRepresentation() {
        return menu;
    }
}
