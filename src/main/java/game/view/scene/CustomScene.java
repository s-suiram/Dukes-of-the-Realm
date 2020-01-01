package game.view.scene;

import javafx.animation.AnimationTimer;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
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
    private AnimationTimer animationTimer;

    protected CustomScene(int defaultWindowWidth, int defaultWindowHeight, boolean startFullScreen, String windowTitle) {
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
        animationTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                loop(s, now);
            }
        };
        animationTimer.start();

        s.setScene(scene);

        if (startFullscreen) {
            s.setFullScreen(true);
        }

        s.widthProperty().addListener((obs, oldVal, newVal) -> {
            windowWidth = newVal.intValue();
            if (widthResize != null)
                widthResize.accept(windowWidth);
        });

        s.heightProperty().addListener((obs, oldVal, newVal) -> {
            windowHeight = newVal.intValue();
            if (heightResize != null)
                heightResize.accept(windowHeight);
        });

        s.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        s.setFullScreenExitHint("");
        s.sizeToScene();
        s.setTitle(windowTitle);
        s.show();
    }

    public void stop() {
        animationTimer.stop();
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
