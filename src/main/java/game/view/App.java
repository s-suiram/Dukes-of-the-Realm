package game.view;

import game.logic.Cardinal;
import game.view.observable.ObserverLabel;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

import static game.controller.GameEvent.*;

public class App extends Application {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;
    public static Map<KeyCode, Boolean> keysPressed;
    private static int[] lastpos = {0, 0};
    private static int[] delta = {0, 0};
    private static boolean drag = false;
    private  static  final  int framedragSkip = 10;
    private  static  int frameDragCount = 0;

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

    private static void handleMouseDrag(MouseEvent e) {
        if (!drag) {
            delta[0] = 0;
            delta[1] = 0;
        } else {
            delta[0] = (int) (e.getX() - lastpos[0]);
            delta[1] = (int) (e.getY() - lastpos[1]);
        }

        lastpos[0] = (int) e.getX();
        lastpos[1] = (int) e.getY();
        drag = true;
    }

    private static void handleDragRelase(MouseEvent e) {
        drag = false;
        delta[0] = 0;
        delta[1] = 0;
    }

    @Override
    public void start(Stage primaryStage) {
        StackPane root = new StackPane();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);
        Pane castles = new Pane();
        VBox HUD = new VBox();
        root.getChildren().addAll(castles, HUD);
        root.setStyle("-fx-background-color: #668054");
        s.setOnKeyPressed(e -> keysPressed.put(e.getCode(), true));
        s.setOnKeyReleased(e -> keysPressed.put(e.getCode(), false));
        s.setOnMouseDragged(e -> {
            frameDragCount++;
            if(frameDragCount == framedragSkip) {
                App.handleMouseDrag(e);
                frameDragCount = 0;
            }
    });

        s.setOnMouseReleased(e -> {
            if (drag) handleDragRelase(e);
        });

        castles.getChildren().addAll(WorldView.getInstance().getTransformedCastleRects());
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

        Label mouseLabel = new Label();
        Label mousePlusCamLabel = new Label();

        s.setOnMouseMoved(e -> {
            mouseLabel.setText(String.format("Mouse pos: %f, %f", e.getX(), e.getY()));
            double x = e.getX() + WorldView.getInstance().getCameraPosition().x;
            double y = e.getY() + WorldView.getInstance().getCameraPosition().y;

            mousePlusCamLabel.setText(String.format("Mouse + camera pos: %f, %f", x, y));
        });

        castles.setPickOnBounds(true);
        castles.setOnMouseClicked(e -> System.out.println("lol"));


        ObserverLabel cameraPos = new ObserverLabel(CAMERA_MOVE);
        ObserverLabel cameraSpeed = new ObserverLabel(CAMERA_SPEED);

        cameraPos.setText(
                String.format(
                        "(%f, %f)",
                        WorldView.getInstance().getCameraPosition().x,
                        WorldView.getInstance().getCameraPosition().y)
        );

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

               if( frameDragCount == 0){
                   System.out.println(delta[0] + " " + delta[1]);

                   WorldView.getInstance().move(delta[0],delta[1]);

               }

                if (WorldView.getInstance().checkAndRestoreCameraMoved()) {
                    castles.getChildren().removeIf(it -> true);
                    castles.getChildren().addAll(WorldView.getInstance().getTransformedCastleRects());
                }
                frames++;
            }
        }.start();

    }


}
