package game.view;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.util.function.Consumer;

public abstract class CustomScene {
    public boolean startFullscreen;
    protected double defaultWindowWidth;
    protected double defaultWindowHeight;
    protected double windowWidth;
    protected double windowHeight;
    protected Scene scene;
    private Consumer<Double> widthResize;
    private Consumer<Double> heightResize;
    private String windowTitle;

    public CustomScene(int defaultWindowWidth, int defaultWindowHeight, boolean startFullScreen, String windowTitle) {
        this.defaultWindowWidth = defaultWindowWidth;
        this.defaultWindowHeight = defaultWindowHeight;
        this.windowWidth = startFullScreen ? Screen.getPrimary().getBounds().getMaxX() : defaultWindowWidth;
        this.windowHeight = startFullScreen ? Screen.getPrimary().getBounds().getMaxY() : defaultWindowHeight;
        this.startFullscreen = startFullScreen;
        this.windowTitle = windowTitle;

        scene = new Scene(new Group(), windowWidth, windowHeight, Color.GREY);
    }

    protected void onWidthResize(Consumer<Double> widthResize) {
        this.widthResize = widthResize;
    }

    protected void onHeightResize(Consumer<Double> heightResize) {
        this.heightResize = heightResize;
    }

    protected Group getGroupRoot() {
        return (Group) scene.getRoot();
    }

    protected ObservableList<Node> getRoot() {
        return getGroupRoot().getChildren();
    }

    public void start(Stage s) {
        init(s);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                loop(s, now);
            }
        }.start();

        if (startFullscreen) {
            s.setFullScreen(true);
        }

        s.widthProperty().addListener((obs, oldVal, newVal) -> {
            windowWidth = newVal.intValue();
            widthResize.accept(windowWidth);
        });

        s.heightProperty().addListener((obs, oldVal, newVal) -> {
            windowHeight = newVal.intValue();
            heightResize.accept(windowHeight);
        });

        s.setScene(scene);
        s.sizeToScene();
        s.setTitle(windowTitle);
        s.show();
    }

    public double getWindowWidth() {
        return windowWidth;
    }

    public double getWindowHeight() {
        return windowHeight;
    }

    public Scene getScene() {
        return scene;
    }

    protected abstract void init(Stage s);

    protected abstract void loop(Stage s, long now);
}
