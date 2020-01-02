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

/**
 * This class encapsulate a scene and is displayed when needed
 */
public abstract class CustomScene {
    /**
     * The width of the window
     */
    protected double windowWidth;

    /**
     * The height of the window
     */
    protected double windowHeight;

    /**
     * The scene which hold the elements to display
     */
    protected Scene scene;
    /**
     * Boolean to know if the scene starts fullscreen or not
     */
    private boolean startFullscreen;
    /**
     * The callback for the width resizing
     */
    private Consumer<Double> widthResize;
    /**
     * The callback for the height resizing
     */
    private Consumer<Double> heightResize;
    /**
     * The title of the window
     */
    private String windowTitle;
    /**
     * The main loop of the scene
     */
    private AnimationTimer animationTimer;

    /**
     * Build a CustomScene
     *
     * @param defaultWindowWidth  the default width if not fullscreen
     * @param defaultWindowHeight the default height if not fullscreen
     * @param startFullScreen     the default state of the window
     * @param windowTitle         the title of the window
     */
    protected CustomScene(int defaultWindowWidth, int defaultWindowHeight, boolean startFullScreen, String windowTitle) {
        this.windowWidth = startFullScreen ? Screen.getPrimary().getBounds().getMaxX() : defaultWindowWidth;
        this.windowHeight = startFullScreen ? Screen.getPrimary().getBounds().getMaxY() : defaultWindowHeight;
        this.startFullscreen = startFullScreen;
        this.windowTitle = windowTitle;

        scene = new Scene(new Group(), windowWidth, windowHeight, Color.GREY);
    }

    /**
     * Set the width resize callback
     *
     * @param widthResize the action to execute
     */
    protected void onWidthResize(Consumer<Double> widthResize) {
        this.widthResize = widthResize;
    }

    /**
     * Set the width resize callback
     *
     * @param heightResize the action to execute
     */
    protected void onHeightResize(Consumer<Double> heightResize) {
        this.heightResize = heightResize;
    }

    /**
     * Returns the root of the scene
     *
     * @return the root of the scene
     */
    protected Group getGroupRoot() {
        return (Group) scene.getRoot();
    }

    /**
     * Returns the children list of the root of the scene
     *
     * @return the children list of the root of the scene
     */
    protected ObservableList<Node> getRoot() {
        return getGroupRoot().getChildren();
    }

    /**
     * Display the scene on the given stage
     *
     * @param s the stage to display the scene on
     */
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

    /**
     * Stop the internal loop
     */
    public void stop() {
        animationTimer.stop();
    }

    /**
     * Returns the window width
     *
     * @return the window width
     */
    public double getWindowWidth() {
        return windowWidth;
    }

    /**
     * Returns the window height
     *
     * @return the window height
     */
    public double getWindowHeight() {
        return windowHeight;
    }

    /**
     * Returns the scene
     *
     * @return the scene
     */
    public Scene getScene() {
        return scene;
    }

    /**
     * This method will be called once
     *
     * @param s the stage
     */
    protected abstract void init(Stage s);

    /**
     * This method wil be called 60 times per second
     *
     * @param s   the stage
     * @param now the timestamp
     */
    protected abstract void loop(Stage s, long now);
}
