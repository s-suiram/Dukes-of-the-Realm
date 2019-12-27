package game.view;

import game.logic.Castle;
import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContextualMenuCastle extends Group {

    Castle castle;

    private Label money;
    private Label level;
    private Label p_val;
    private Label k_val;
    private Label o_val;

    public ContextualMenuCastle(Castle c) {
        super();
        this.castle = c;

        money = new Label();
        level = new Label();

        p_val = new Label();
        k_val = new Label();
        o_val = new Label();

        //Troops
        HBox pikeman = new HBox(new Label("Pikeman: "), p_val);
        HBox knight = new HBox(new Label("Knight: "), k_val);
        HBox onager = new HBox(new Label("Onager: "), o_val);

        //Graphical nodes
        VBox menu = new VBox(money, level, pikeman, knight, onager);
        getChildren().addAll(menu);
        menu.setSpacing(1);
        menu.setStyle("-fx-background-color: rgba(200, 200, 200, 0.8)");
        menu.getChildren().forEach(node -> node.setStyle("-fx-font-size: 20pt"));
    }

    public void draw() {
        money.setText("Money: " + castle.getMoney());
        level.setText("Level: " + castle.getLevel());
        p_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Pikeman).count()));
        k_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Knight).count()));
        o_val.setText(String.valueOf(castle.getTroops().stream().filter(troop -> troop instanceof Onager).count()));
    }
}
