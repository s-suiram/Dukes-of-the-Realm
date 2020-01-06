package game.view;

import game.logic.Castle;
import game.logic.troop.TroopType;
import javafx.animation.AnimationTimer;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

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



        Label lvlLabel = new Label("LVL   : ");
        lvlLabel.setId("lvl-label");
        lvl = new Label(String.valueOf(castle.getLevel()));
        lvl.setId("lvl-value");
        lvl.setPrefWidth(50);
        lvl.setAlignment(Pos.CENTER);
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

        Label nextLabel = new Label("Next : ");
        nextValue = new Label(String.valueOf(castle.levelUpPrice()));
        nextValue.setMinWidth(69);
        nextValue.setAlignment(Pos.CENTER_RIGHT);
        ImageView coin2 = new ImageView();
        coin2.getStyleClass().add("coin");
        StackPane s = new StackPane(coin2);
        s.setMinWidth(20);
        HBox secondLvl = new HBox(nextLabel, nextValue,s );
        secondLvl.setId("second-lvl");
        secondLvl.getStyleClass().add("hbox");


     /*   lvlLabel.setBorder(b);
        lvl.setBorder(b);
        nextLabel.setBorder(b);
        nextValue.setBorder(b);
*/
        levelUpProgress = new ProgressBar();
        levelUpProgress.setId("progress-lvl");
        levelUpProgress.getStyleClass().add("blue-bar");
        levelUpProgress.setPrefWidth(200);

        HBox top = new HBox(name,florinValue,new StackPane(coin) );
        top.getStyleClass().add("hbox");

        VBox lvlInfo = new VBox(firstLvlUp, secondLvl);
        lvlInfo.getStyleClass().add("vbox");
        HBox test = new HBox(lvl_up);
        test.setAlignment(Pos.CENTER_RIGHT);
        test.setMinWidth(72);
        HBox lvlUp = new HBox(lvlInfo, test);
        lvl_up.setPadding(new Insets(10));
        lvlUp.setFillHeight(true);
        lvlUp.getStyleClass().add("hbox");

        GridPane troopsLayering = new GridPane();

        ImageView pView = new ImageView(p);
        ImageView kView = new ImageView(k);
        ImageView oView = new ImageView(o);

        troopsLayering.addColumn(0, pView, kView, oView);

        pVal = new Label();
        kVal = new Label();
        oVal = new Label();
        pVal.setAlignment(Pos.CENTER);
        oVal.setAlignment(Pos.CENTER);
        kVal.setAlignment(Pos.CENTER);
        oVal.setMinWidth(30);
        kVal.setMinWidth(30);
        pVal.setMinWidth(30);

        troopsLayering.setHgap(14);

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

        Button createSquad = new Button("Attack !");
        createSquad.setPrefSize(220,100);
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



        pane = new VBox(top, separator(), levelUpProgress, lvlUp, separator(),
                troopsLayering, separator(), queue, separator(), createSquad, separator(), squadFeedback);
        pane.getStyleClass().add("vbox");
        pane.setId("menu-border");
        pane.setPadding(new Insets(5));
        Border b = new Border(
                new BorderStroke(Color.rgb(48, 19, 0), BorderStrokeStyle.SOLID ,new CornerRadii(10), new BorderWidths(15) )
        );

        pane.setBorder(b);

        getChildren().addAll(pane);
    }

    private HBox separator(){
        Line sep = new Line(0,0,200,0);
        sep.setStroke(Color.rgb(48, 19, 0));
        sep.setStrokeWidth(3);
        HBox g = new HBox();
        g.setPadding(new Insets(5));
        g.getChildren().add(sep);
        return g;
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
        queue.setMaxHeight(50);
        queue.getChildren().addAll(castle.getProducer().getQueue().stream().limit(4).map(t -> {
            ImageView v = new ImageView(t.getTroopType() == TroopType.PIKE_MAN ? p : t.getTroopType() == TroopType.KNIGHT ? k : o);
            return new VBox(v, new StackPane(new Label(String.valueOf(t.getRemainingTime()))));
        }).collect(Collectors.toList()));
        if (queue.getChildren().size() == 4)
            queue.getChildren().add(new Label("...+" + (castle.getProducer().getQueue().size() - 4)));
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
