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

/**
 * A view which represent a squad builder
 */
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

        plusOnager.setOnAction(e -> transferOnager(true));
        plusKnight.setOnAction(e -> transferKnight(true));
        plusPikeman.setOnAction(e -> transferPikeman(true));

        Button minusOnager = new Button("-");
        Button minusKnight = new Button("-");
        Button minusPikeman = new Button("-");

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
        HBox knightBox = new HBox(minusKnight, knightLabel, plusKnight, in2, k_in, out2, k_out);
        HBox pikemanBox = new HBox(minusPikeman, pikemanLabel, plusPikeman, in3, p_in, out3, p_out);

        Label feedback = new Label();

        Button submit = new Button("Ok");
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
        cancel.setOnAction(e -> contextualMenuCastle.deleteSquadBuilder());

        HBox quitButtons = new HBox(submit, cancel);

        VBox menu = new VBox(title, pikemanBox, knightBox, onagerBox, feedback, quitButtons);
        menu.setId("squad-builder");
        getChildren().add(menu);
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

        p_in.setText(String.valueOf(pikemanInCastle));
        k_in.setText(String.valueOf(knightInCastle));
        o_in.setText(String.valueOf(onagerInCastle));

        p_out.setText(String.valueOf(pikemanOutOfCastle));
        k_out.setText(String.valueOf(knightOutOfCastle));
        o_out.setText(String.valueOf(onagerOutOfCastle));
    }

    public int getHeight() {
        return (int) ((VBox) getChildren().get(0)).getHeight();
    }

    public int getWidth() {
        return (int) ((VBox) getChildren().get(0)).getWidth();
    }
}
