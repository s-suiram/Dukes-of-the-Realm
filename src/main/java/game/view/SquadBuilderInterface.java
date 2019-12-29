package game.view;

import game.logic.Castle;
import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Troop;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SquadBuilderInterface extends Group {

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

    public SquadBuilderInterface(Castle src, Castle dest, ContextualMenuCastle contextualMenuCastle) {
        super();
        setTranslateX(-15);
        setTranslateY(-15);
        contextualMenuCastle.hidePanel();
        this.srcCastle = src;
        this.destCastle = dest;
        this.contextualMenuCastle = contextualMenuCastle;

        onagerOutOfCastle = 0;
        knightOutOfCastle = 0;
        pikemanOutOfCastle = 0;

        onagerInCastle = srcCastle.getOnagers().size();
        knightInCastle = srcCastle.getKnights().size();
        pikemanInCastle = srcCastle.getPikemen().size();

        Label title = new Label("Squad builder");

        Button plusOnager = new Button("+");
        plusOnager.setFocusTraversable(false);
        Button plusKnight = new Button("+");
        plusKnight.setFocusTraversable(false);
        Button plusPikeman = new Button("+");
        plusPikeman.setFocusTraversable(false);

        plusOnager.setOnAction(e -> transferOnager(true));
        plusKnight.setOnAction(e -> transferKnight(true));
        plusPikeman.setOnAction(e -> transferPikeman(true));

        Button minusOnager = new Button("-");
        minusOnager.setFocusTraversable(false);
        Button minusKnight = new Button("-");
        minusKnight.setFocusTraversable(false);
        Button minusPikeman = new Button("-");
        minusPikeman.setFocusTraversable(false);

        minusOnager.setOnAction(e -> transferOnager(false));
        minusKnight.setOnAction(e -> transferKnight(false));
        minusPikeman.setOnAction(e -> transferPikeman(false));

        Label onagerLabel = new Label("Onager");
        Label knightLabel = new Label("Knight");
        Label pikemanLabel = new Label("Pikeman");

        Label in1 = new Label("Castle: ");
        Label in2 = new Label("Castle: ");
        Label in3 = new Label("Castle: ");

        p_in = new Label();
        k_in = new Label();
        o_in = new Label();

        Label out1 = new Label("Squad: ");
        Label out2 = new Label("Squad: ");
        Label out3 = new Label("Squad: ");

        p_out = new Label();
        k_out = new Label();
        o_out = new Label();

        HBox onagerBox = new HBox(minusOnager, onagerLabel, plusOnager, in1, o_in, out1, o_out);
        onagerBox.setSpacing(50);
        HBox knightBox = new HBox(minusKnight, knightLabel, plusKnight, in2, k_in, out2, k_out);
        knightBox.setSpacing(50);
        HBox pikemanBox = new HBox(minusPikeman, pikemanLabel, plusPikeman, in3, p_in, out3, p_out);
        pikemanBox.setSpacing(50);

        Label feedback = new Label();

        Button submit = new Button("Ok");
        submit.setFocusTraversable(false);
        submit.setStyle("-fx-background-color: #548045");
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
                    src.addOst(all, dest);
                    contextualMenuCastle.deleteSquadBuilder();
                } else {
                    feedback.setText("You can't build an empty squad");
                }
            }
        });

        Button cancel = new Button("Cancel");
        cancel.setFocusTraversable(false);
        cancel.setStyle("-fx-background-color: #801c20");
        cancel.setOnAction(e -> contextualMenuCastle.deleteSquadBuilder());

        HBox quitButtons = new HBox(submit, cancel);
        quitButtons.setSpacing(50);

        VBox menu = new VBox(title, pikemanBox, knightBox, onagerBox, feedback, quitButtons);
        menu.setSpacing(20);
        menu.getChildren().forEach(n -> n.setStyle("-fx-font-size: 20pt"));
        menu.setStyle("-fx-background-color: rgba(83,124,156,0.79)");
        getChildren().add(menu);
        title.setStyle("-fx-font-size: 40pt");
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

    public void draw() {
        onagerInCastle = srcCastle.getOnagers().size() - onagerOutOfCastle;
        knightInCastle = srcCastle.getKnights().size() - knightOutOfCastle;
        pikemanInCastle = srcCastle.getPikemen().size() - pikemanOutOfCastle;

        p_in.setText(String.valueOf(pikemanInCastle));
        k_in.setText(String.valueOf(knightInCastle));
        o_in.setText(String.valueOf(onagerInCastle));

        p_out.setText(String.valueOf(pikemanOutOfCastle));
        k_out.setText(String.valueOf(knightOutOfCastle));
        o_out.setText(String.valueOf(onagerOutOfCastle));
    }
}
