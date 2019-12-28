package game.view;

import com.sun.javafx.geom.Point2D;
import game.controller.KeyboardEventHandler;
import game.controller.MouseEventHandler;
import game.logic.Cardinal;
import game.logic.World;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.stream.Collectors;


public class App extends Application {

    public static int DEFAULT_WINDOWED_X = 1000;
    public static int DEFAULT_WINDOWED_Y = 800;

    public static int WINDOW_WIDTH = DEFAULT_WINDOWED_X;
    public static int WINDOW_HEIGHT = DEFAULT_WINDOWED_Y;
    public static boolean paused;
    public static int frames;
    private static Stage stage;

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public void handleCameraMove(Scene s) {
        int distToScreen = 50;
        Point2D p = MouseEventHandler.getInstance().getMousePos();
        com.sun.javafx.geom.Rectangle left = new com.sun.javafx.geom.Rectangle(0, 0, distToScreen, App.WINDOW_HEIGHT);
        com.sun.javafx.geom.Rectangle right = new com.sun.javafx.geom.Rectangle(App.WINDOW_WIDTH - distToScreen, 0, distToScreen, App.WINDOW_HEIGHT);
        com.sun.javafx.geom.Rectangle up = new com.sun.javafx.geom.Rectangle(0, 0, App.WINDOW_WIDTH, distToScreen);
        com.sun.javafx.geom.Rectangle down = new com.sun.javafx.geom.Rectangle(0, App.WINDOW_HEIGHT - distToScreen, App.WINDOW_WIDTH, distToScreen);

        if (left.contains((int) p.x, (int) p.y)) {
            WorldView.getInstance().move(Cardinal.WEST);
        }
        if (right.contains((int) p.x, (int) p.y)) {
            WorldView.getInstance().move(Cardinal.EAST);
        }
        if (up.contains((int) p.x, (int) p.y)) {
            WorldView.getInstance().move(Cardinal.NORTH);
        }
        if (down.contains((int) p.x, (int) p.y)) {
            WorldView.getInstance().move(Cardinal.SOUTH);
        }
    }

    @Override
    public void start(Stage primaryStage) {
        paused = false;
        stage = primaryStage;
        primaryStage.setFullScreen(true);
        WINDOW_WIDTH = (int) Screen.getPrimary().getBounds().getMaxX();
        WINDOW_HEIGHT = (int) Screen.getPrimary().getBounds().getMaxY();

        Group root = new Group();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);

        KeyboardEventHandler.init(s);
        MouseEventHandler.init(s);
        WorldView.init(s);

        VBox HUD = new VBox();
        Group castles = new Group(
                WorldView.getInstance()
                        .getCastles()
                        .stream()
                        .map(CastleView::getGroup)
                        .collect(Collectors.toList())
        );

        Group troops = new Group();

        Rectangle greenBackground = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        greenBackground.setFill(Color.web("668054"));
        greenBackground.setOnMouseClicked(e -> WorldView.getInstance().clearAllContextualMenu());
        greenBackground.setOnMouseEntered(e -> s.setCursor(Cursor.OPEN_HAND));
        root.getChildren().addAll(greenBackground, troops, castles, HUD);

        primaryStage.widthProperty().addListener((obs, oldVal, newVal) -> WINDOW_WIDTH = newVal.intValue());
        primaryStage.heightProperty().addListener((obs, oldVal, newVal) -> WINDOW_HEIGHT = newVal.intValue());

        WorldView.getInstance().clearAllContextualMenu();

        primaryStage.setScene(s);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

        Label mouseCamPos = new Label();
        HUD.getChildren().add(mouseCamPos);

        Label pause = new Label("PAUSED");
        root.getChildren().add(pause);
        pause.setStyle("-fx-font-size: 99pt");
        pause.setTextFill(Color.rgb(45, 62, 80));
        pause.setVisible(false);
        pause.impl_processCSS(true);
        pause.setTranslateX(WINDOW_WIDTH / 2.0 - pause.prefWidth(-1) / 2.0);

        //s.setOnMouseMoved(e -> mouseCamPos.setText(String.format("mouse + cam pos: %f, %f", e.getX() + WorldView.getInstance().cameraPos.x, e.getY() + WorldView.getInstance().cameraPos.y)));
        WorldView.getInstance().clearAllContextualMenu();

        //Make main game class
        new AnimationTimer() {

            @Override
            public void handle(long now) {
                frames++;
                KeyboardEventHandler.getInstance().handle();
                WorldView.getInstance().draw(troops);
                handleCameraMove(s);
                if (!paused) {
                    World.getInstance().step();
                    pause.setVisible(false);
                } else pause.setVisible(true);

                if (frames == 60) frames = 1;
            }
        }.start();

    }
}

