package game.view;

import game.logic.Castle;
import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContextualMenuCastle extends Group {

    Castle castle;
    boolean enoughMoney;
    private Label money;
    private Label level;
    private Label levelUpFeedback;
    private Label levelUpPrice;
    private Label p_val;
    private Label k_val;
    private Label o_val;

    public ContextualMenuCastle(Castle c) {
        super();
        this.castle = c;
        enoughMoney = true;
        money = new Label();

        level = new Label();
        Button levelup = new Button("Level Up");
        levelUpPrice = new Label();
        levelup.setFocusTraversable(false);
        levelUpFeedback = new Label("");
        HBox levelupBox = new HBox(level, levelup, levelUpPrice, levelUpFeedback);
        levelupBox.setSpacing(50);
        levelup.setOnAction(e -> enoughMoney = c.startLevelUp());

        p_val = new Label();
        k_val = new Label();
        o_val = new Label();

        HBox pikeman = new HBox(new Label("Pikeman: "), p_val);
        HBox knight = new HBox(new Label("Knight: "), k_val);
        HBox onager = new HBox(new Label("Onager: "), o_val);

        VBox menu = new VBox(money, levelupBox, pikeman, knight, onager);
        getChildren().addAll(menu);
        menu.setSpacing(20);
        menu.setStyle("-fx-background-color: rgba(200, 200, 200, 0.8)");

        menu.getChildren().forEach(node -> node.setStyle("-fx-font-size: 20pt"));
    }

    public void draw() {
        money.setText("Money: " + castle.getMoney());
        level.setText("Level: " + castle.getLevel());
        p_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Pikeman).count()));
        k_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Knight).count()));
        o_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Onager).count()));
        if (!enoughMoney) {
            levelUpFeedback.setText("Not enough money");
        } else {
            if (castle.getTimeToLevelUp() == -1)
                levelUpFeedback.setText(castle.getLevel() > 1 ? String.format("You reach level %d", castle.getLevel()) : "");
            else
                levelUpFeedback.setText(String.format("%d remaining", castle.getTimeToLevelUp()));
        }
        levelUpPrice.setText(String.format("Level up cost: %d", castle.levelUpPrice()));
    }
}
