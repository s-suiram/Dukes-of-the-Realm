package game.view.scene;

import com.sun.javafx.geom.Point2D;
import game.controller.KeyboardEventHandler;
import game.controller.MouseEventHandler;
import game.logic.Cardinal;
import game.logic.World;
import game.view.WorldView;
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

public class Game extends CustomScene {
    private boolean pause;
    private VBox pauseHud;

    private GameSetting settings;

    private List<String> f, n;
    private int castlePerPlayer;

    public Game(int defaultWindowWidth, int defaultWindowHeight,
                boolean startFullScreen, String windowTitle,
                List<String> f, List<String> n, int castlePerPlayer) {
        super(defaultWindowWidth, defaultWindowHeight, startFullScreen, windowTitle);
        this.f = f;
        this.n = n;
        this.castlePerPlayer = castlePerPlayer;
        settings = new GameSetting();
    }

    @Override
    protected void init(Stage s) {
        Group castles = new Group();
        Group troops = new Group();

        World.init(f, n, castlePerPlayer);
        WorldView.init(castles, troops);
        KeyboardEventHandler.init(getScene(), s);
        MouseEventHandler.init(getScene());
        pause = false;

        Rectangle background = new Rectangle(
                0, 0,
                getWindowWidth(), getWindowHeight()
        );
        background.setFill(Color.web("668054"));
        background.setOnMouseClicked(e -> WorldView.getInstance().clearAllContextualMenu());
        background.setOnMouseEntered(e -> getScene().setCursor(Cursor.OPEN_HAND));

        onWidthResize(background::setWidth);
        onHeightResize(background::setHeight);

        Label pauseLabel = new Label("PAUSE !");
        pauseLabel.setStyle("-fx-font-size: 99pt");
        pauseLabel.setTextFill(Color.rgb(45, 62, 80));

        Button cameraToggle = new Button("Toggle camera border movement");
        cameraToggle.setStyle("-fx-font-size: 20pt; -fx-background-color: #128011");
        cameraToggle.setFocusTraversable(false);
        cameraToggle.setOnAction(e -> {
            settings.cameraMoveBorder = !settings.cameraMoveBorder;
            if (settings.cameraMoveBorder)
                cameraToggle.setStyle("-fx-font-size: 20pt; -fx-background-color: #128011");
            else
                cameraToggle.setStyle("-fx-font-size: 20pt; -fx-background-color: #802a2b");
        });

        pauseHud = new VBox(pauseLabel, cameraToggle);
        pauseHud.setVisible(false);


        getRoot().addAll(background, troops, castles, pauseHud);
    }

    @Override
    protected void loop(Stage s, long now) {
        KeyboardEventHandler.getInstance().handle();
        WorldView.getInstance().draw();
        if (settings.cameraMoveBorder) handleCameraMove();
        if (!isPause()) {
            World.getInstance().step();
            pauseHud.setVisible(false);
        } else {
            pauseHud.setVisible(true);
        }
    }

    public void handleCameraMove() {
        Point2D p = MouseEventHandler.getInstance().getMousePos();

        double topInset = getScene().getWindow().getHeight() - getScene().getHeight();

        Rectangle2D left = new Rectangle2D(0, 0, settings.cameraMoveBorderThickness, getWindowHeight());
        Rectangle2D right = new Rectangle2D(getWindowWidth() - settings.cameraMoveBorderThickness, 0, settings.cameraMoveBorderThickness, getWindowHeight());
        Rectangle2D up = new Rectangle2D(0, 0, getWindowWidth(), settings.cameraMoveBorderThickness);
        Rectangle2D down = new Rectangle2D(0, getWindowHeight() - settings.cameraMoveBorderThickness - topInset, getWindowWidth(), settings.cameraMoveBorderThickness);

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

    public boolean isPause() {
        return pause;
    }

    public void togglePause() {
        this.pause = !this.pause;
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
