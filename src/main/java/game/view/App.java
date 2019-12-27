package game.view;

import game.controller.KeyboardEventHandler;
import game.controller.MouseEventHandler;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
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

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);

        VBox HUD = new VBox();
        Group castles = new Group(
                WorldView.getInstance()
                        .getCastles()
                        .stream()
                        .map(CastleView::getGroup)
                        .collect(Collectors.toList())
        );
        System.out.println(Screen.getPrimary().getBounds().getMaxX());
        Rectangle greenBackground = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        greenBackground.setFill(Color.web("668054"));
        greenBackground.setOnMouseClicked(e -> CastleView.clearContextualMenu());

        root.getChildren().addAll(greenBackground, castles, HUD, CastleView.getContextualMenu());

        KeyboardEventHandler.init(s);
        MouseEventHandler.init(s);

        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);
        primaryStage.setScene(s);
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.setTitle("Dukes of the realm");
        primaryStage.show();

        Label mouseCamPos = new Label();
        HUD.getChildren().add(mouseCamPos);
        s.setOnMouseMoved(e -> mouseCamPos.setText(String.format("mouse + cam pos: %f, %f", e.getX() + WorldView.getInstance().cameraPos.x, e.getY() + WorldView.getInstance().cameraPos.y)));

        //Make main game class
        new AnimationTimer() {
            long frames = 0;

            @Override
            public void handle(long now) {
                KeyboardEventHandler.getInstance().handle();
                WorldView.getInstance().draw();
                System.out.println(CastleView.getContextualMenu().getChildren());
                //World.getInstance().step();
                frames++;
            }
        }.start();

    }
}

