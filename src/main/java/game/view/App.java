package game.view;

import game.controller.MouseEventHandler;
import game.logic.Cardinal;
import game.view.observable.ObserverLabel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static game.controller.GameEvent.*;

public class App extends Application {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;
    public static Map<KeyCode, Boolean> keysPressed;

    static {
        keysPressed = new HashMap<>(KeyCode.values().length);
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static boolean isKeyPressed(KeyCode k) {
        return keysPressed.getOrDefault(k, false);
    }

    public static boolean isKeyPressedAndConsume(KeyCode k) {
        boolean b = keysPressed.getOrDefault(k, false);
        keysPressed.put(k, false);
        return b;
    }


    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);
        VBox HUD = new VBox();
        Rectangle greenBackground = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        greenBackground.setFill(Color.web("668054"));
        root.getChildren().addAll(greenBackground, HUD);
        //root.setStyle("-fx-background-color: #668054");
        s.setOnKeyPressed(e -> keysPressed.put(e.getCode(), true));
        s.setOnKeyReleased(e -> keysPressed.put(e.getCode(), false));
        MouseEventHandler handler = new MouseEventHandler(s);

        WorldView.getInstance().getCastles().forEach(c -> root.getChildren().add(c.getGroup()));

        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

        Label mouseLabel = new Label();
        Label mousePlusCamLabel = new Label();

        s.setOnMouseMoved(e -> {
            mouseLabel.setText(String.format("Mouse pos: %f, %f", e.getX(), e.getY()));
            double x = e.getX() + WorldView.getInstance().cameraPos.x;
            double y = e.getY() + WorldView.getInstance().cameraPos.y;

            mousePlusCamLabel.setText(String.format("Mouse + camera pos: %f, %f", x, y));
        });


        ObserverLabel cameraPos = new ObserverLabel(CAMERA_MOVE);
        ObserverLabel cameraSpeed = new ObserverLabel(CAMERA_SPEED);

        cameraPos.setText(
                String.format(
                        "(%f, %f)",
                        WorldView.getInstance().cameraPos.x,
                        WorldView.getInstance().cameraPos.y
                ));

        cameraSpeed.setText(String.valueOf(WorldView.getInstance().getCameraSpeed()));

        WorldView.getInstance().addObservers(cameraPos, cameraSpeed);
        HUD.getChildren().addAll(cameraPos, cameraSpeed, mouseLabel, mousePlusCamLabel);

        //Make main game class
        new AnimationTimer() {
            long frames = 0;

            @Override
            public void handle(long now) {
                CAMERA_MOVE_LEFT.define(() -> WorldView.getInstance().move(Cardinal.WEST));
                CAMERA_MOVE_UP.define(() -> WorldView.getInstance().move(Cardinal.NORTH));
                CAMERA_MOVE_DOWN.define(() -> WorldView.getInstance().move(Cardinal.SOUTH));
                CAMERA_MOVE_RIGHT.define(() -> WorldView.getInstance().move(Cardinal.EAST));

                CAMERA_SPEED_INCREASE.define(() -> WorldView.getInstance().increaseCameraSpeed());
                CAMERA_SPEED_DECREASE.define(() -> WorldView.getInstance().decreaseCameraSpeed());
                CAMERA_SPEED_RESET.define(() -> WorldView.getInstance().resetCameraSpeed());

                frames++;
            }
        }.start();


    }


}
