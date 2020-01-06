package game.view;

import game.logic.Castle;
import game.logic.troop.TroopType;
import javafx.animation.AnimationTimer;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.stream.Collectors;

/**
 * The contextual menu which appear when right clicking on a castle
 */
public class ContextualMenuCastle extends Group {

    private static Image p, k, o;

    static {
        p = new Image("file:resources/pikeman.png");
        k = new Image("file:resources/knight.png");
        o = new Image("file:resources/onager.png");
    }

    HBox queue;
    /**
     * The model
     */
    private Castle castle;
    /**
     * The view
     */
    private CastleView castleView;
    /**
     * The squad builder interface
     */
    private SquadBuilderInterface squadBuilderInterface;
    private VBox pane;
    private Label florinValue;
    private ProgressBar levelUpProgress;
    private Label name;
    private Label lvl;
    private Label nextValue;
    private Label pVal;
    private Label kVal;
    private Label oVal;

    /**
     * Build and initialize contextual menu
     *
     * @param c the castle view
     */
    public ContextualMenuCastle(CastleView c) {
        setVisible(false);
        this.castle = c.getModel();
        this.castleView = c;

        name = new Label(castle.getOwner().getName());
        name.setId("name");
        name.getStyleClass().add("min-width");

        florinValue = new Label(String.valueOf(castle.getFlorin()));
        florinValue.setId("florin");
        florinValue.getStyleClass().add("min-width");

        ImageView coin = new ImageView();
        coin.getStyleClass().add("coin");

        HBox top = new HBox(name, florinValue, new StackPane(coin));
        top.getStyleClass().add("hbox");

        Label lvlLabel = new Label("LVL: ");
        lvlLabel.setId("lvl-label");
        lvl = new Label(String.valueOf(castle.getLevel()));
        lvl.setId("lvl-value");
        Button lvl_up = new Button();
        lvl_up.setId("lvl-button");
        lvl_up.getStyleClass().add("max-width");

        lvl_up.setOnAction(e -> {
            levelUpProgress.setProgress(0.0);
            levelUpProgress.setManaged(true);
            levelUpProgress.setVisible(true);
            castle.startLevelUp().ifPresent(error -> new AnimationTimer() {
                long last = 0;
                int blinked = 0;

                @Override
                public void handle(long now) {
                    if (now - last < 120000000) return;
                    blinked++;
                    switch (error) {
                        case NOT_ENOUGH_MONEY:
                            if (florinValue.getTextFill() == Color.RED)
                                florinValue.setTextFill(Color.WHITE);
                            else
                                florinValue.setTextFill(Color.RED);
                            break;
                        case ONGOING_PROCESS:
                            if (levelUpProgress.getStyleClass().contains("red-bar")) {
                                levelUpProgress.getStyleClass().remove("red-bar");
                                levelUpProgress.getStyleClass().add("blue-bar");
                            } else {
                                levelUpProgress.getStyleClass().add("red-bar");
                                levelUpProgress.getStyleClass().remove("blue-bar");
                            }
                            break;
                    }

                    last = now;
                    if (blinked == 6) {
                        stop();
                        levelUpProgress.getStyleClass().removeAll("red-bar", "blue-bar");
                        levelUpProgress.getStyleClass().add("blue-bar");
                        florinValue.setTextFill(Color.WHITE);
                    }
                }
            }.start());
        });

        HBox firstLvlUp = new HBox(lvlLabel, lvl);
        firstLvlUp.setId("first-lvl");
        firstLvlUp.getStyleClass().add("hbox");

        Label nextLabel = new Label("Next: ");
        nextValue = new Label(String.valueOf(castle.levelUpPrice()));
        ImageView coin2 = new ImageView();
        coin2.getStyleClass().add("coin");

        HBox secondLvl = new HBox(nextLabel, nextValue, new StackPane(coin2));
        secondLvl.setId("second-lvl");
        secondLvl.getStyleClass().add("hbox");

        levelUpProgress = new ProgressBar();
        levelUpProgress.setId("progress-lvl");
        levelUpProgress.getStyleClass().add("blue-bar");

        VBox lvlInfo = new VBox(firstLvlUp, secondLvl);
        lvlInfo.getStyleClass().add("vbox");

        HBox lvlUp = new HBox(lvlInfo, lvl_up, levelUpProgress);
        lvlUp.getStyleClass().add("hbox");

        GridPane troopsLayering = new GridPane();

        ImageView pView = new ImageView(p);
        ImageView kView = new ImageView(k);
        ImageView oView = new ImageView(o);

        troopsLayering.addColumn(0, pView, kView, oView);

        pVal = new Label();
        kVal = new Label();
        oVal = new Label();

        troopsLayering.addColumn(1, pVal, kVal, oVal);

        Button p1p = new Button("+1");
        p1p.setOnAction(e -> {
            if (!castle.produce(TroopType.PIKE_MAN)) {
                makeMoneyBlink();
            }
        });
        Button p1k = new Button("+1");
        p1k.setOnAction(e -> {
            if (!castle.produce(TroopType.KNIGHT)) {
                makeMoneyBlink();
            }
        });
        Button p1o = new Button("+1");
        p1o.setOnAction(e -> {
            if (!castle.produce(TroopType.ONAGER)) {
                makeMoneyBlink();
            }
        });

        Button p10p = new Button("+10");
        p10p.setOnAction(e -> {
            if (!castle.produce(TroopType.PIKE_MAN, 10)) {
                makeMoneyBlink();
            }
        });
        Button p10k = new Button("+10");
        p10k.setOnAction(e -> {
            if (!castle.produce(TroopType.KNIGHT, 10)) {
                makeMoneyBlink();
            }
        });
        Button p10o = new Button("+10");
        p10o.setOnAction(e -> {
            if (!castle.produce(TroopType.ONAGER, 10)) {
                makeMoneyBlink();
            }
        });
        troopsLayering.addColumn(2, p1p, p1k, p1o);
        troopsLayering.addColumn(3, p10p, p10k, p10o);

        queue = new HBox();
        queue.getStyleClass().add("hbox");

        queue.setId("queue");

        Button createSquad = new Button("Create Squad");
        Label squadFeedback = new Label();
        createSquad.setOnAction(e -> {
            if (CastleView.getSelected() == null)
                squadFeedback.setText("Select a castle");
            else if (CastleView.getSelected().getModel() == castle)
                squadFeedback.setText("Select another castle");
            else {
                squadBuilderInterface = new SquadBuilderInterface(castle, CastleView.getSelected().getModel(), this);
                getChildren().add(squadBuilderInterface);
            }
        });

        pane = new VBox(top, lvlUp, troopsLayering, queue, createSquad, squadFeedback);
        pane.getStyleClass().add("vbox");
        pane.setId("menu-border");
        getChildren().addAll(pane);
    }

    /**
     * This method is called each frame
     */
    public void draw() {
        florinValue.setText(String.valueOf(castle.getFlorin()));
        name.setText(castle.getOwner().getName());
        lvl.setText(String.valueOf(castle.getLevel()));
        nextValue.setText(String.valueOf(castle.levelUpPrice()));
        pVal.setText(String.valueOf(castle.getPikemen().size()));
        kVal.setText(String.valueOf(castle.getKnights().size()));
        oVal.setText(String.valueOf(castle.getOnagers().size()));
        if (castle.getTimeToLevelUp() == -1) {
            levelUpProgress.setManaged(false);
            levelUpProgress.setVisible(false);
        } else {
            levelUpProgress.setProgress(castle.getTimeToLevelUp() / (double) castle.nbTurnToLevelup());
        }
        queue.getChildren().clear();
        queue.getChildren().addAll(castle.getProducer().getQueue().stream().limit(5).map(t -> {
            ImageView v = new ImageView(t.getTroopType() == TroopType.PIKE_MAN ? p : t.getTroopType() == TroopType.KNIGHT ? k : o);
            return new VBox(v, new StackPane(new Label(String.valueOf(t.getRemainingTime()))));
        }).collect(Collectors.toList()));
        if (queue.getChildren().size() == 5)
            queue.getChildren().add(new Label("...+" + (castle.getProducer().getQueue().size() - 5)));
        if (squadBuilderInterface != null)
            squadBuilderInterface.draw();
    }

    void makeMoneyBlink() {
        new AnimationTimer() {
            long last = 0;
            int blinked = 0;

            @Override
            public void handle(long now) {
                if (now - last < 120000000) return;
                blinked++;
                if (florinValue.getTextFill() == Color.RED)
                    florinValue.setTextFill(Color.WHITE);
                else
                    florinValue.setTextFill(Color.RED);
                last = now;
                if (blinked == 6) {
                    stop();
                    florinValue.setTextFill(Color.WHITE);
                }
            }
        }.start();
    }

    /**
     * Hide contextual menu
     */
    public void hidePanel() {
        pane.setVisible(false);
    }

    /**
     * Show contextual menu
     */
    public void showPanel() {
        pane.setVisible(true);
    }

    /**
     * Remove the view of the squad builder
     */
    public void deleteSquadBuilder() {
        if (squadBuilderInterface != null) {
            getChildren().remove(squadBuilderInterface);
            squadBuilderInterface = null;
            showPanel();
        }
    }

    /**
     * Returns the x position of the contextual menu
     *
     * @return the x position of the contextual menu
     */
    public int getX() {
        return (int) localToScene(new Point2D(getTranslateX(), getTranslateY())).getX();
    }

    /**
     * Returns the y position of the contextual menu
     *
     * @return the y position of the contextual menu
     */
    public int getY() {
        return (int) localToScene(new Point2D(getTranslateX(), getTranslateY())).getY();
    }

    /**
     * Returns the contextual menu width
     *
     * @return the contextual menu width
     */
    public int getWidth() {
        if (squadBuilderInterface != null)
            return squadBuilderInterface.getWidth();
        return (int) pane.getWidth();
    }

    /**
     * Returns the contextual menu height
     *
     * @return the contextual menu height
     */
    public int getHeight() {
        if (squadBuilderInterface != null)
            return squadBuilderInterface.getHeight();
        return (int) pane.getHeight();
    }
}
