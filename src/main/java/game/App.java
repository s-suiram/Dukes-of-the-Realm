package game;

import game.view.Game;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    private static Game game = new Game();

    public static void main(String[] args) {
        launch(args);
    }

    public static Game getGame() {
        return game;
    }

    @Override
    public void start(Stage primaryStage) {
        getGame().start(primaryStage);
    }
}
