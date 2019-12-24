package game.view;

import com.sun.javafx.geom.Point2D;
import game.logic.Cardinal;
import game.logic.World;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class WorldView {
    public static int cameraSpeed = 5;
    private static WorldView instance = null;
    private List<CastleView> castles;
    private int cameraX;
    private int cameraY;
    private boolean cameraMoved = false;

    private WorldView() {
        castles = new ArrayList<>();
        World.getInstance().getCastles().forEach(c -> castles.add(new CastleView(c)));
        cameraX = 0;
        cameraY = 0;
    }

    public static WorldView getInstance() {
        if (instance == null) instance = new WorldView();
        return instance;
    }

    public List<CastleView> getCastles() {
        return castles;
    }

    public boolean checkAndRestoreCameraMoved() {
        if (cameraMoved) {
            cameraMoved = false;
            return true;
        }
        return false;
    }

    public void move(Cardinal direction) {
        cameraMoved = true;
        switch (direction) {
            case NORTH:
                cameraY -= cameraSpeed;
                break;
            case SOUTH:
                cameraY += cameraSpeed;
                break;
            case EAST:
                cameraX += cameraSpeed;
                break;
            case WEST:
                cameraX -= cameraSpeed;
                break;
        }
        if (cameraX < 0) cameraX = 0;
        if (cameraY < 0) cameraY = 0;
        if (cameraX > World.FIELD_WIDTH) cameraX = World.FIELD_WIDTH;
        if (cameraY > World.FIELD_HEIGHT) cameraY = World.FIELD_HEIGHT;
    }

    public Point2D getCameraPosition() {
        return new Point2D(cameraX, cameraY);
    }

    public List<Rectangle> getTransformedCastleRects() {
        List<Rectangle> rects = getInstance()
                .getCastles().stream()
                .map(CastleView::getRepresentation)
                .collect(Collectors.toList());

        rects.forEach(r -> {
            r.setTranslateX(-cameraX);
            r.setTranslateY(-cameraY);
        });

        return rects;
    }
}
