package game.view;

import game.logic.Castle;
import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static game.view.ContextualMenuCastle.separator;

/**
 * A view which represent a squad builder
 */
public class SquadBuilderInterface extends Group {


    private static Image p, k, o;

    static {
        p = new Image("file:resources/pikeman.png");
        k = new Image("file:resources/knight.png");
        o = new Image("file:resources/onager.png");
    }

    private int pikemanOutOfCastle;
    private int knightOutOfCastle;
    private int onagerOutOfCastle;
    private int pikemanInCastle;
    private int knightInCastle;
    private int onagerInCastle;
    private Castle srcCastle;
    private Castle destCastle;
    private ContextualMenuCastle contextualMenuCastle;
    private Label p_out;
    private Label k_out;
    private Label o_out;
    private Label p_in;
    private Label k_in;
    private Label o_in;

    private String castle;
    private String squad;

    /**
     * Build a squad builder
     *
     * @param src                  the source castle
     * @param target               the target castle
     * @param contextualMenuCastle the contextual menu
     */
    public SquadBuilderInterface(Castle src, Castle target, ContextualMenuCastle contextualMenuCastle) {
        super();
        contextualMenuCastle.hidePanel();
        this.srcCastle = src;
        this.destCastle = target;
        this.contextualMenuCastle = contextualMenuCastle;

        castle = "Castle : ";
        squad = "Squad : ";

        ImageView pView = new ImageView(p);
        ImageView kView = new ImageView(k);
        ImageView oView = new ImageView(o);


        Border b = new Border(
                new BorderStroke(Color.rgb(48, 19, 0),
                        BorderStrokeStyle.SOLID, new CornerRadii(10), new BorderWidths(15))
        );

        VBox parent = new VBox();
        parent.setId("menu-border");
        parent.setBorder(b);

        GridPane ui = new GridPane();

        VBox pikeText = new VBox();
        VBox knightText = new VBox();
        VBox onagerText = new VBox();

        HBox pikeButtons = new HBox();
        HBox knightButtons = new HBox();
        HBox onagerButtons = new HBox();

        p_out = new Label();
        k_out = new Label();
        o_out = new Label();
        p_in = new Label();
        k_in = new Label();
        o_in = new Label();

        onagerOutOfCastle = 0;
        knightOutOfCastle = 0;
        pikemanOutOfCastle = 0;

        onagerInCastle = srcCastle.getOnagers().size();
        knightInCastle = srcCastle.getKnights().size();
        pikemanInCastle = srcCastle.getPikemen().size();


        Label title = new Label("Squad builder");

        Button plusOnager = new Button("+");
        Button plusKnight = new Button("+");
        Button plusPikeman = new Button("+");

        Button minusOnager = new Button("-");
        Button minusKnight = new Button("-");
        Button minusPikeman = new Button("-");


        plusOnager  .setPrefSize(40,50);
        plusKnight  .setPrefSize(40,50);
        plusPikeman .setPrefSize(40,50)  ;
        minusOnager .setPrefSize(40,50);
        minusKnight .setPrefSize(40,50);
        minusPikeman.setPrefSize(40,50);
        
        pikeButtons.getChildren().addAll(plusPikeman, minusPikeman);
        knightButtons.getChildren().addAll(plusKnight, minusKnight);
        onagerButtons.getChildren().addAll(plusOnager, minusOnager);


        pikeText.getChildren().addAll(p_in, p_out);
        knightText.getChildren().addAll(k_in, k_out);
        onagerText.getChildren().addAll(o_in, o_out);

        pikeButtons.setAlignment(Pos.CENTER);
        onagerButtons.setAlignment(Pos.CENTER);
        knightButtons.setAlignment(Pos.CENTER);

        plusOnager.setOnAction(e -> transferOnager(true));
        plusKnight.setOnAction(e -> transferKnight(true));
        plusPikeman.setOnAction(e -> transferPikeman(true));

        minusOnager.setOnAction(e -> transferOnager(false));
        minusKnight.setOnAction(e -> transferKnight(false));
        minusPikeman.setOnAction(e -> transferPikeman(false));

        Label feedback = new Label();

        Button submit = new Button("Ok");
        submit.setPrefWidth(100);
        submit.setPadding(new Insets(5));
        submit.setOnAction(e -> {
            List<Pikeman> pikemen = srcCastle.getPikemen().stream().limit(pikemanOutOfCastle).collect(Collectors.toList());
            List<Knight> knights = srcCastle.getKnights().stream().limit(knightOutOfCastle).collect(Collectors.toList());
            List<Onager> onagers = srcCastle.getOnagers().stream().limit(onagerOutOfCastle).collect(Collectors.toList());

            List<Troop> all = new ArrayList<>();

            all.addAll(pikemen);
            all.addAll(knights);
            all.addAll(onagers);

            if (all.size() > 10) {
                feedback.setText("You can't put more than " + 10 + " troops");
            } else {
                if (all.size() > 0) {
                    src.createSquad(all, target);
                    contextualMenuCastle.deleteSquadBuilder();
                } else {
                    feedback.setText("You can't build an empty squad");
                }
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setPrefWidth(100);
        cancel.setOnAction(e -> contextualMenuCastle.deleteSquadBuilder());
        cancel.setPadding(new Insets(5));

        ui.addRow(0, pView, pikeButtons, pikeText);
        ui.addRow(1, kView, knightButtons, knightText);
        ui.addRow(2, oView, onagerButtons, onagerText);
        ui.setAlignment(Pos.CENTER);
        ui.setMinWidth(200);
        ui.setHgap(10);

        ui.getChildren().forEach(node -> {
            GridPane.setHgrow(node, Priority.ALWAYS);
            GridPane.setVgrow(node, Priority.ALWAYS);
        });

        HBox quitButtons = new HBox(submit, cancel);
        quitButtons.setAlignment(Pos.CENTER);
        quitButtons.setPrefWidth(200);

      //  parent.setMinSize(300, 300);
        parent.getChildren().addAll(title,separator(220), ui, separator(220), quitButtons);
        parent.setAlignment(Pos.CENTER);
        parent.setPadding(new Insets(10));

        getChildren().addAll(parent);
    }



    private void transferPikeman(boolean plus) {
        if (plus) {
            if (pikemanInCastle > 0) {
                pikemanInCastle--;
                pikemanOutOfCastle++;
            }
        } else {
            if (pikemanOutOfCastle > 0) {
                pikemanInCastle++;
                pikemanOutOfCastle--;
            }
        }
    }

    private void transferKnight(boolean plus) {
        if (plus) {
            if (knightInCastle > 0) {
                knightInCastle--;
                knightOutOfCastle++;
            }
        } else {
            if (knightOutOfCastle > 0) {
                knightInCastle++;
                knightOutOfCastle--;
            }
        }
    }

    private void transferOnager(boolean plus) {
        if (plus) {
            if (onagerInCastle > 0) {
                onagerInCastle--;
                onagerOutOfCastle++;
            }
        } else {
            if (onagerOutOfCastle > 0) {
                onagerInCastle++;
                onagerOutOfCastle--;
            }
        }
    }

    /**
     * Method called each frame
     */
    public void draw() {
        onagerInCastle = srcCastle.getOnagers().size() - onagerOutOfCastle;
        knightInCastle = srcCastle.getKnights().size() - knightOutOfCastle;
        pikemanInCastle = srcCastle.getPikemen().size() - pikemanOutOfCastle;

        p_in.setText(castle + pikemanInCastle);
        k_in.setText(castle + knightInCastle);
        o_in.setText(castle + onagerInCastle);

        p_out.setText(squad + pikemanOutOfCastle);
        k_out.setText(squad + knightOutOfCastle);
        o_out.setText(squad + onagerOutOfCastle);
    }

    public int getHeight() {
        return (int) ((VBox) getChildren().get(0)).getHeight();
    }

    public int getWidth() {
        return (int) ((VBox) getChildren().get(0)).getWidth();
    }
}
