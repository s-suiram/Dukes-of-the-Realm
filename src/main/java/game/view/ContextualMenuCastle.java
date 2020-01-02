package game.view;

import game.logic.Castle;
import game.logic.troop.TroopType;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class ContextualMenuCastle extends Group {

    private Castle castle;
    private CastleView castleView;
    private boolean lvlup_enoughMoney;
    private Label money;
    private Label level;
    private Label levelUpFeedback;
    private Label levelUpPrice;
    private Label p_val;
    private Label p_feedback;
    private Label k_val;
    private Label k_feedback;
    private Label o_val;
    private Label o_feedback;
    private Label createSquad_feedback;
    private Label queue;
    private VBox menu;
    private SquadBuilderInterface squadBuilderInterface;

    public ContextualMenuCastle(CastleView c) {
        super();
        setVisible(false);
        this.castle = c.getModel();
        this.castleView = c;
        squadBuilderInterface = null;
        lvlup_enoughMoney = true;
        Label owner = new Label(castle.getOwner().getName());
        money = new Label();
        level = new Label();
        Button levelup = new Button("Level Up");
        levelUpPrice = new Label();
        levelup.setFocusTraversable(false);
        levelUpFeedback = new Label("");
        HBox levelupBox = new HBox(level, levelup, levelUpPrice, levelUpFeedback);
        levelupBox.setSpacing(50);
        levelup.setOnAction(e -> levelUpFeedback
                .setText(
                        (lvlup_enoughMoney = castle.startLevelUp())
                                ? ""
                                : "Not enough money")
        );

        p_val = new Label();
        Button p_add = new Button("+");
        p_add.setFocusTraversable(false);
        p_feedback = new Label();
        k_val = new Label();
        Button k_add = new Button("+");
        k_add.setFocusTraversable(false);
        k_feedback = new Label();
        o_val = new Label();
        Button o_add = new Button("+");
        o_add.setFocusTraversable(false);
        o_feedback = new Label();

        p_add.setOnAction(e -> p_feedback.setText(castle.produce(TroopType.PIKE_MAN) ? "" : "Not enough money"));
        k_add.setOnAction(e -> k_feedback.setText(castle.produce(TroopType.KNIGHT) ? "" : "Not enough money"));
        o_add.setOnAction(e -> o_feedback.setText(castle.produce(TroopType.ONAGER) ? "" : "Not enough money"));

        HBox pikeman = new HBox(new Label("Pikeman (Cost: " + TroopType.PIKE_MAN.getCost() + "): "), p_val, p_add, p_feedback);
        HBox knight = new HBox(new Label("Knight (Cost: " + TroopType.KNIGHT.getCost() + "): "), k_val, k_add, k_feedback);
        HBox onager = new HBox(new Label("Onager (Cost: " + TroopType.ONAGER.getCost() + "): "), o_val, o_add, o_feedback);

        p_add.setFocusTraversable(false);
        k_add.setFocusTraversable(false);
        o_add.setFocusTraversable(false);

        pikeman.setSpacing(50);
        knight.setSpacing(50);
        onager.setSpacing(50);

        queue = new Label();
        Button cancelLast = new Button("Delete first of queue");
        cancelLast.setFocusTraversable(false);
        HBox queueBox = new HBox(cancelLast, queue);
        queueBox.setSpacing(50);

        cancelLast.setOnAction(e -> castle.getProducer().cancel());
        Button createSquad = new Button("Create squad");
        createSquad.setFocusTraversable(false);
        createSquad_feedback = new Label();
        HBox squadBox = new HBox(createSquad, createSquad_feedback);
        squadBox.setSpacing(50);
        menu = new VBox(owner, money, levelupBox, pikeman, knight, onager, queueBox, squadBox);
        createSquad.setOnAction(e -> {
            if (CastleView.getSelected() == null) {
                createSquad_feedback.setText("Select a castle first");
            } else if (CastleView.getSelected() == castleView) {
                createSquad_feedback.setText("Select another castle");
            } else {
                createSquad_feedback.setText("");
                squadBuilderInterface = new SquadBuilderInterface(castle, CastleView.getSelected().getModel(), this);
                getChildren().add(squadBuilderInterface);
            }
        });
        getChildren().addAll(menu);
        menu.setSpacing(20);
        menu.setStyle("-fx-background-color: rgba(200, 200, 200, 0.8)");

        menu.getChildren().forEach(node -> node.setStyle("-fx-font-size: 14pt;"));

        setOnMouseMoved(e -> getScene().setCursor(Cursor.HAND));
    }

    public void hidePanel() {
        menu.setVisible(false);
    }

    public void showPanel() {
        menu.setVisible(true);
    }

    public void deleteSquadBuilder() {
        if (squadBuilderInterface != null) {
            getChildren().remove(squadBuilderInterface);
            squadBuilderInterface = null;
            showPanel();
        }
    }

    public void draw() {
        money.setText("Money: " + castle.getMoney());
        level.setText("Level: " + castle.getLevel());
        p_val.setText(String.valueOf(castle.getPikemen().size()));
        k_val.setText(String.valueOf(castle.getKnights().size()));
        o_val.setText(String.valueOf(castle.getOnagers().size()));
        if (lvlup_enoughMoney) {
            if (castle.getTimeToLevelUp() == -1) {
                lvlup_enoughMoney = false;
                levelUpFeedback.setText(castle.getLevel() > 1 ? String.format("You reach level %d", castle.getLevel()) : "");
            } else
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

        if (!isVisible()) {
            levelUpFeedback.setText("");
            p_feedback.setText("");
            k_feedback.setText("");
            o_feedback.setText("");
            createSquad_feedback.setText("");
            deleteSquadBuilder();
        }

        if (squadBuilderInterface != null) squadBuilderInterface.draw();
    }

    public int getX() {
        return (int) localToScene(new Point2D(getTranslateX(), getTranslateY())).getX();
    }

    public int getY() {
        return (int) localToScene(new Point2D(getTranslateX(), getTranslateY())).getY();
    }

    public int getWidth() {
        return (int) menu.getWidth();
    }

    public int getHeight() {
        return (int) menu.getHeight();
    }
}
