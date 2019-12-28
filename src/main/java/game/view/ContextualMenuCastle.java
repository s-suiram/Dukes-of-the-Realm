package game.view;

import game.logic.Castle;
import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.TroopType;
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
    private Button p_add;
    private Label k_val;
    private Button k_add;
    private Label o_val;
    private Button o_add;

    private Label queue;

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
        p_add = new Button("+");
        k_val = new Label();
        k_add = new Button("+");
        o_val = new Label();
        o_add = new Button("+");

        p_add.setOnAction(e -> castle.produce(TroopType.PIKE_MAN));
        k_add.setOnAction(e -> castle.produce(TroopType.KNIGHT));
        o_add.setOnAction(e -> castle.produce(TroopType.ONAGER));

        HBox pikeman = new HBox(new Label("Pikeman: "), p_val, p_add);
        HBox knight = new HBox(new Label("Knight: "), k_val, k_add);
        HBox onager = new HBox(new Label("Onager: "), o_val, o_add);

        p_add.setFocusTraversable(false);
        k_add.setFocusTraversable(false);
        o_add.setFocusTraversable(false);

        pikeman.setSpacing(50);
        knight.setSpacing(50);
        onager.setSpacing(50);

        queue = new Label();

        VBox menu = new VBox(money, levelupBox, pikeman, knight, onager, queue);
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
        StringBuilder queueBuilder = new StringBuilder();
        castle.getProducer()
                .getQueue()
                .forEach(t -> queueBuilder
                        .append(t.getT())
                        .append("(")
                        .append(t.getRemainingTime())
                        .append(") < "));
        queue.setText("Queue: " + (queueBuilder.toString().isEmpty() ? "empty" : queueBuilder.toString().substring(0, queueBuilder.lastIndexOf("<"))));
    }
}
