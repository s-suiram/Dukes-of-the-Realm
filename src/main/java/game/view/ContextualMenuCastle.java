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
    boolean lvlup_enoughMoney;
    private Label money;
    private Label level;
    private Label levelUpFeedback;
    private Label levelUpPrice;
    private Label p_val;
    private Button p_add;
    private Label p_feedback;
    private boolean p_enoughMoney;
    private Label k_val;
    private Button k_add;
    private Label k_feedback;
    private boolean k_enoughMoney;
    private Label o_val;
    private Button o_add;
    private Label o_feedback;
    private boolean o_enoughMoney;

    private Label queue;

    public ContextualMenuCastle(Castle c) {
        super();
        this.castle = c;
        lvlup_enoughMoney = true;
        money = new Label();
        level = new Label();
        Button levelup = new Button("Level Up");
        levelUpPrice = new Label();
        levelup.setFocusTraversable(false);
        levelUpFeedback = new Label("");
        HBox levelupBox = new HBox(level, levelup, levelUpPrice, levelUpFeedback);
        levelupBox.setSpacing(50);
        levelup.setOnAction(e -> lvlup_enoughMoney = c.startLevelUp());

        p_val = new Label();
        p_add = new Button("+");
        p_feedback = new Label();
        p_enoughMoney = true;
        k_val = new Label();
        k_add = new Button("+");
        k_feedback = new Label();
        k_enoughMoney = true;
        o_val = new Label();
        o_add = new Button("+");
        o_feedback = new Label();
        o_enoughMoney = true;

        p_add.setOnAction(e -> p_enoughMoney = castle.produce(TroopType.PIKE_MAN));
        k_add.setOnAction(e -> k_enoughMoney = castle.produce(TroopType.KNIGHT));
        o_add.setOnAction(e -> o_enoughMoney = castle.produce(TroopType.ONAGER));

        HBox pikeman = new HBox(new Label("Pikeman: "), p_val, p_add, p_feedback);
        HBox knight = new HBox(new Label("Knight: "), k_val, k_add, k_feedback);
        HBox onager = new HBox(new Label("Onager: "), o_val, o_add, o_feedback);

        p_add.setFocusTraversable(false);
        k_add.setFocusTraversable(false);
        o_add.setFocusTraversable(false);

        pikeman.setSpacing(50);
        knight.setSpacing(50);
        onager.setSpacing(50);

        queue = new Label();
        Button cancelLast = new Button("Delete first of queue");
        HBox queueBox = new HBox(cancelLast, queue);

        cancelLast.setFocusTraversable(false);
        cancelLast.setOnAction(e -> c.getProducer().cancel());

        VBox menu = new VBox(money, levelupBox, pikeman, knight, onager, queueBox);
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
        if (!lvlup_enoughMoney) {
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
                        .append(" left) < "));
        queue.setText("Queue: " + (queueBuilder.toString().isEmpty() ? "empty" : queueBuilder.toString().substring(0, queueBuilder.lastIndexOf("<"))));
        p_feedback.setText(p_enoughMoney ? "" : "Not enough money");
        k_feedback.setText(k_enoughMoney ? "" : "Not enough money");
        o_feedback.setText(o_enoughMoney ? "" : "Not enough money");
    }
}
