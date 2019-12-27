package game.view;

import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class ContextualMenuCastle extends Group {

    CastleView cv;
    //Graphical nodes
    private VBox menu;
    //Data nodes
    private Label money;
    private Label level;

    public ContextualMenuCastle(CastleView c) {
        super();
        this.cv = c;
        money = new Label();
        level = new Label();
        menu = new VBox(money, level);
        menu.getChildren().forEach(node -> node.setStyle("-fx-font-size: 20pt"));
        getChildren().addAll(menu);
        menu.setStyle("-fx-background-color: rgba(200, 200, 200, 0.8)");
        menu.setSpacing(1);
    }

    public void draw() {
        money.setText("Money: " + cv.getC().getMoney());
        level.setText("Level: " + cv.getC().getLevel());
    }
}
