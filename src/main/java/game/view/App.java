package game.view;

import game.controller.KeyboardEventHandler;
import game.controller.MouseEventHandler;
import game.logic.Cardinal;

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
import java.util.Objects;
import java.util.stream.Collectors;


public class App extends Application {

    public static int WINDOW_WIDTH = 1000;
    public static int WINDOW_HEIGHT = 800;

    @Override
    public void start(Stage primaryStage) {
        Group root = new Group();
        Scene s = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT, Color.GREY);
        VBox HUD = new VBox();
        Group castles = new Group(WorldView.getInstance().getCastles().stream().map(CastleView::getGroup).collect(Collectors.toList()));
        Rectangle greenBackground = new Rectangle(0, 0, WINDOW_WIDTH, WINDOW_HEIGHT);
        greenBackground.setFill(Color.web("668054"));
        root.getChildren().addAll(greenBackground, HUD, castles);

        KeyboardEventHandler.init(s);
        MouseEventHandler.init(s);

        primaryStage.setWidth(WINDOW_WIDTH);
        primaryStage.setHeight(WINDOW_HEIGHT);
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
        });


        //Make main game class
        new AnimationTimer() {
            long frames = 0;

            @Override
            public void handle(long now) {

                KeyboardEventHandler.getInstance().handle();
                WorldView.getInstance().draw();
                root.getChildren().removeIf(node -> node instanceof ContextualMenuCastle && ((ContextualMenuCastle) node).isConsumed());
                WorldView.getInstance().getCastles().stream()
                        .map(CastleView::getMenuCastle)
                        .filter(Objects::nonNull)
                        .findFirst()
                        .ifPresent(cmenu -> {
                            if (!root.getChildren().contains(cmenu)) root.getChildren().add(cmenu);
                        });

                frames++;
            }
        }.start();
    }
}

