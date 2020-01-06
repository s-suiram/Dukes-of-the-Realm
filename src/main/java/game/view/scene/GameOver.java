package game.view.scene;

import game.App;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class GameOver extends CustomScene {
    private Label label;
    private Button button;

    public GameOver(int defaultWindowWidth, int defaultWindowHeight, boolean startFullScreen, String windowTitle) {
        super(defaultWindowWidth, defaultWindowHeight, startFullScreen, windowTitle, "gameover-and-win.css");
    }

    @Override
    protected void init(Stage s) {
        label = new Label("Game over");
        button = new Button("Return to menu");

        button.setOnAction(e -> {
            stop();
            App.buildWelcome().start(s);
        });

        getRoot().addAll(label, button);
    }

    @Override
    protected void loop(Stage s, long now) {
        label.setTranslateX((windowWidth / 2.0) - (label.getWidth() / 2.0));

        button.setTranslateX((windowWidth / 2.0) - (button.getWidth() / 2.0));
        button.setTranslateY((windowHeight / 2.0) - (button.getHeight() / 2.0));
    }
}
