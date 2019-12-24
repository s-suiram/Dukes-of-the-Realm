package game.view;

import game.logic.Cardinal;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class App extends Application {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;
    public static Map<KeyCode, Boolean> keys;

    static {
        keys = new HashMap<>(KeyCode.values().length);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public boolean isKeyPressed(KeyCode k) {
        return keys.getOrDefault(k, false);
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);
        Pane castles = new Pane();
        Pane HUD = new Pane();
        root.getChildren().addAll(castles, HUD);
        root.setStyle("-fx-background-color: #668054");
        s.setOnKeyPressed(e -> keys.put(e.getCode(), true));
        s.setOnKeyReleased(e -> keys.put(e.getCode(), false));

        castles.getChildren().addAll(WorldView.getInstance().getTransformedCastleRects());

        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

        Label cameraPos = new Label();

        HUD.getChildren().add(cameraPos);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (isKeyPressed(KeyCode.LEFT)) WorldView.getInstance().move(Cardinal.WEST);
                if (isKeyPressed(KeyCode.UP)) WorldView.getInstance().move(Cardinal.NORTH);
                if (isKeyPressed(KeyCode.DOWN)) WorldView.getInstance().move(Cardinal.SOUTH);
                if (isKeyPressed(KeyCode.RIGHT)) WorldView.getInstance().move(Cardinal.EAST);
                if (WorldView.getInstance().checkAndRestoreCameraMoved()) {
                    castles.getChildren().removeIf(it -> true);
                    castles.getChildren().addAll(WorldView.getInstance().getTransformedCastleRects());
                    cameraPos.setText(
                            String.format(
                                    "(%f, %f)",
                                    WorldView.getInstance().getCameraPosition().x,
                                    WorldView.getInstance().getCameraPosition().y)
                    );
                }
            }
        }.start();

    }


}
