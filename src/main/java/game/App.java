package game;

import game.view.scene.Game;
import game.view.scene.NewGame;
import game.view.scene.Welcome;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {

    public final static int DEFAULT_WINDOW_WIDTH = 1000;
    public final static int DEFAULT_WINDOW_HEIGHT = 800;
    public final static boolean START_FULLSCREEN = false;
    public final static String WINDOW_TITLE = "Dukes Of The Realm";

    private static Game game;

    public static void main(String[] args) {
        launch(args);
    }

    public static Welcome buildWelcome() {
        return new Welcome(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, START_FULLSCREEN, WINDOW_TITLE);
    }

    public static NewGame buildNewGame() {
        return new NewGame(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, START_FULLSCREEN, WINDOW_TITLE);
    }

    public static Game buildGame(List<String> f, List<String> n, int c) {
        game = new Game(DEFAULT_WINDOW_WIDTH, DEFAULT_WINDOW_HEIGHT, START_FULLSCREEN, WINDOW_TITLE, f, n, c);
        return game;
    }

    public static Game getGame() {
        return game;
    }

    @Override
    public void start(Stage stage) {
        buildWelcome().start(stage);
    }
}
