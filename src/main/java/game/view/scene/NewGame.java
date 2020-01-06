package game.view.scene;

import game.App;
import game.FirstNameDico;
import game.controller.FullscreenKeyboardController;
import game.controller.KeyboardEventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * New game scene
 */
public class NewGame extends CustomScene {
    private GridPane grid;

    private KeyboardEventHandler keyboardController;

    public NewGame(int defaultWindowWidth, int defaultWindowHeight, boolean startFullscreen, String windowTitle) {
        super(defaultWindowWidth, defaultWindowHeight, startFullscreen, windowTitle, "newgame.css");
        keyboardController = new FullscreenKeyboardController(getScene());
    }

    @Override
    protected void init(Stage s) {
        grid = new GridPane();

        Label playerNameLabel = new Label("Name: ");
        TextField playerName = new TextField();
        grid.addRow(0, playerNameLabel, playerName);

        Label nbFightingLabel = new Label("Number of fighting dukes: ");
        Spinner<Integer> nbFighting = new Spinner<>(2, 50, 3);
        grid.addRow(1, nbFightingLabel, nbFighting);

        Label nbNeutralLabel = new Label("Number of neutral dukes: ");
        Spinner<Integer> nbNeutral = new Spinner<>(1, 50, 2);
        grid.addRow(2, nbNeutralLabel, nbNeutral);

        Label nbCastlePerPlayerLabel = new Label("Number of castle per player: ");
        Spinner<Integer> nbCastlePerPlayer = new Spinner<>(1, 50, 4);
        grid.addRow(3, nbCastlePerPlayerLabel, nbCastlePerPlayer);

        Button submit = new Button("Ok");
        Button cancel = new Button("Cancel");

        submit.setOnAction(e -> {
            List<String> fighting = new ArrayList<>();
            List<String> neutral = new ArrayList<>();
            int castlePerPlayer = nbCastlePerPlayer.getValue();
            fighting.add(playerName.getText());

            for (int i = 0; i < nbFighting.getValue() - 1; i++) {
                fighting.add(FirstNameDico.getRandName());
            }
            for (int i = 0; i < nbNeutral.getValue(); i++) {
                neutral.add(FirstNameDico.getRandName());
            }
            App.buildGame(fighting, neutral, castlePerPlayer).start(s);
            stop();
        });

        cancel.setOnAction(e -> {
            App.buildWelcome().start(s);
            stop();
        });

        Arrays.asList(playerName, nbFighting, nbNeutral, nbCastlePerPlayer).forEach(n -> n.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && !e.isAltDown()) {
                submit.fire();
                submit.setDisable(true);
            }
        }));

        grid.addRow(4, submit, cancel);

        grid.setId("grid");

        getRoot().addAll(grid);
    }

    @Override
    protected void loop(Stage s, long now) {
        keyboardController.handle(s);

        grid.setTranslateX((windowWidth / 2.0) - grid.getWidth() / 2.0);
        grid.setTranslateY((windowHeight / 2.0) - grid.getHeight() / 2.0);
    }
}
