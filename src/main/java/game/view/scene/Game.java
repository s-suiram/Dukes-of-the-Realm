package game.view.scene;

import com.sun.javafx.geom.Point2D;
import game.App;
import game.controller.GameKeyboardController;
import game.controller.KeyboardEventHandler;
import game.controller.MouseEventHandler;
import game.logic.Cardinal;
import game.logic.World;
import game.view.WorldView;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.List;

/**
 * The game scene
 */
public class Game extends CustomScene {
    private VBox menu;
    private Rectangle menuBackground;
    private Label pauseLabel;

    private GameSetting settings;

    private List<String> f, n;
    private int castlePerPlayer;

    private KeyboardEventHandler keyboardController;

    public Game(int defaultWindowWidth, int defaultWindowHeight,
                boolean startFullScreen, String windowTitle,
                List<String> f, List<String> n, int castlePerPlayer) {
        super(defaultWindowWidth, defaultWindowHeight, startFullScreen, windowTitle);
        this.f = f;
        this.n = n;
        this.castlePerPlayer = castlePerPlayer;
        settings = new GameSetting();
        keyboardController = new GameKeyboardController(getScene());
    }

    @Override
    protected void init(Stage s) {
        Group castles = new Group();
        Group troops = new Group();

        World.init(f, n, castlePerPlayer);
        WorldView.init(castles, troops);
        MouseEventHandler.init(getScene());

        Rectangle background = new Rectangle(
                0, 0,
                getWindowWidth(), getWindowHeight()
        );

        background.setFill(Color.web("668054"));
        background.setOnMouseClicked(e -> WorldView.getInstance().clearAllContextualMenu());
        background.setOnMouseEntered(e -> getScene().setCursor(Cursor.OPEN_HAND));

        pauseLabel = new Label("PAUSE !");
        pauseLabel.setVisible(false);
        pauseLabel.setStyle("-fx-font-size: 99pt");
        pauseLabel.setTextFill(Color.rgb(45, 62, 80));
        Button cameraToggle = new Button("Toggle camera border movement");
        cameraToggle.setStyle("-fx-font-size: 13pt; -fx-background-color: #128011");
        cameraToggle.setFocusTraversable(false);
        cameraToggle.setOnAction(e -> {
            settings.cameraMoveBorder = !settings.cameraMoveBorder;
            if (settings.cameraMoveBorder)
                cameraToggle.setStyle("-fx-font-size: 13pt; -fx-background-color: #128011");
            else
                cameraToggle.setStyle("-fx-font-size: 13pt; -fx-background-color: #802a2b");
        });

        Button exit = new Button("Exit");
        exit.setStyle("-fx-font-size: 13pt;");
        exit.setFocusTraversable(false);
        exit.setOnAction(e -> {
            stop();
            App.buildWelcome().start(s);
        });

        menu = new VBox(cameraToggle, exit);
        menu.setVisible(false);
        menu.setAlignment(Pos.CENTER);
        menu.setSpacing(10);

        menuBackground = new Rectangle(0, 0, windowWidth, windowHeight);
        menuBackground.setFill(new Color(1.0, 1.0, 1.0, 0.5));
        menuBackground.setStrokeWidth(0);
        menuBackground.setVisible(false);

        onWidthResize(newVal -> {
            background.setWidth(newVal);
            menuBackground.setWidth(newVal);
        });

        onHeightResize(newVal -> {
            background.setHeight(newVal);
            menuBackground.setHeight(newVal);
        });

        getRoot().addAll(background, troops, castles, menuBackground, pauseLabel, menu);
    }

    @Override
    protected void loop(Stage s, long now) {
        keyboardController.handle(s);
        WorldView.getInstance().draw();
        if (settings.cameraMoveBorder) handleCameraMove();
        if (!pauseLabel.isVisible())
            World.getInstance().step();

        pauseLabel.setTranslateX(windowWidth / 2.0 - pauseLabel.getWidth() / 2.0);

        menu.setTranslateX(windowWidth / 2.0 - menu.getWidth() / 2.0);
        menu.setTranslateY(windowHeight / 2.0 - menu.getHeight() / 2.0);
    }

    public void handleCameraMove() {
        Point2D p = MouseEventHandler.getInstance().getMousePos();

        double topInset = 0.0;
        if (getScene().getWindow() != null) {
            topInset = getScene().getWindow().getHeight() - getScene().getHeight();
        }

        Rectangle2D left = new Rectangle2D(0, 0, settings.cameraMoveBorderThickness, getWindowHeight());
        Rectangle2D right = new Rectangle2D(getWindowWidth() - settings.cameraMoveBorderThickness, 0, settings.cameraMoveBorderThickness, getWindowHeight());
        Rectangle2D up = new Rectangle2D(0, 0, getWindowWidth(), settings.cameraMoveBorderThickness);
        Rectangle2D down = new Rectangle2D(0, getWindowHeight() - settings.cameraMoveBorderThickness - topInset, getWindowWidth(), settings.cameraMoveBorderThickness);

        if (left.contains((int) p.x, (int) p.y))
            WorldView.getInstance().move(Cardinal.WEST);
        if (right.contains((int) p.x, (int) p.y))
            WorldView.getInstance().move(Cardinal.EAST);
        if (up.contains((int) p.x, (int) p.y))
            WorldView.getInstance().move(Cardinal.NORTH);
        if (down.contains((int) p.x, (int) p.y))
            WorldView.getInstance().move(Cardinal.SOUTH);

    }

    public void toggleMenu() {
        menu.setVisible(!menu.isVisible());
        menuBackground.setVisible(menu.isVisible());
        pauseLabel.setVisible(menu.isVisible());
    }

    public void togglePause() {
        pauseLabel.setVisible(!pauseLabel.isVisible());
    }

    static class GameSetting {
        public boolean cameraMoveBorder;
        public int cameraMoveBorderThickness;

        public GameSetting() {
            cameraMoveBorder = true;
            cameraMoveBorderThickness = 50;
        }
    }

}
