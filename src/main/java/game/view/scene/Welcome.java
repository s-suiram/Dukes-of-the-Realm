package game.view.scene;

import game.App;
import game.controller.FullscreenKeyboardController;
import game.controller.KeyboardEventHandler;
import game.logic.World;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

/**
 * Welcome scene
 */
public class Welcome extends CustomScene {
    private VBox elements;
    private Label title;
    private KeyboardEventHandler keyboardController;

    public Welcome(int defaultWindowWidth, int defaultWindowHeight, boolean startFullScreen, String windowTitle) {
        super(defaultWindowWidth, defaultWindowHeight, startFullScreen, windowTitle);
        keyboardController = new FullscreenKeyboardController(getScene());
    }

    @Override
    protected void init(Stage s) {

        title = new Label("DUKES OF THE REALM");
        title.setStyle("-fx-font-size: 60pt");

        Button newGame = new Button("New Game");
        newGame.setFocusTraversable(false);
        newGame.setOnAction(e -> {
            App.buildNewGame().start(s);
            stop();
        });

        Label corruptedSaveLabel = new Label("Your file is corrupted or it is not a save file !");
        corruptedSaveLabel.setVisible(false);
        Button loadGame = new Button("Load Game");
        loadGame.setFocusTraversable(false);

        loadGame.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            File file = fileChooser.showOpenDialog(s);
            if (file != null) {
                try {
                    ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file));
                    App.buildGame((World) inputStream.readObject()).start(s);
                    inputStream.close();
                } catch (IOException | ClassNotFoundException ex) {
                    corruptedSaveLabel.setVisible(true);
                }
            }
        });

        Button exit = new Button("Exit");
        exit.setFocusTraversable(false);
        exit.setOnAction(e -> System.exit(0));


        getRoot().addAll(title, elements = new VBox(newGame, loadGame, exit, corruptedSaveLabel));
        elements.setSpacing(50);

        onHeightResize(elements::setPrefHeight);
        onWidthResize(elements::setPrefWidth);
        elements.setAlignment(Pos.CENTER);
    }

    @Override
    protected void loop(Stage s, long now) {
        keyboardController.handle(s);

        title.setTranslateX(windowWidth / 2.0 - title.getWidth() / 2.0);

        elements.setTranslateX(windowWidth / 2.0 - elements.getWidth() / 2.0);
        elements.setTranslateY(windowHeight / 2.0 - elements.getHeight() / 2.0);
    }
}
