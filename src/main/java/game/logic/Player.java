package game.logic;


import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

public abstract class Player {
    private static Player player; // The player in front of his computer

    private List<Castle> castles;

    private String name;

    public Player(String name) {
        castles = new ArrayList<>();
        this.name = name;
    }

    public static Player getPlayer() {
        return player;
    }

    public static void setPlayer(Player player) {
        Player.player = player;
    }

    public String getName() {
        return name;
    }

    public List<Castle> getCastles() {
        return castles;
    }

    public void addCastle(Cardinal door) {
        castles.add(new Castle(this, door));
    }

    public void addCastle(Cardinal door, Point2D pos) {
        castles.add(new Castle(this, door, pos));
    }

    @Override
    public String toString() {
        return name;
    }
}
