package game.logic;


import com.sun.javafx.geom.Point2D;

import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represent a player that owns castles and make decisions
 */
public abstract class Player {
    /**
     * The player which is in front of his computer
     */
    private static Player player;

    /**
     * The list of castle the player owns
     */
    private List<Castle> castles;

    /**
     * The name of the player
     */
    private String name;

    /**
     * Build a player with the specified name
     *
     * @param name the name of the player
     */
    public Player(String name) {
        castles = new ArrayList<>();
        this.name = name;
    }

    /**
     * Returns the current player
     *
     * @return the current player
     */
    public static Player getPlayer() {
        return player;
    }

    /**
     * Set the current player
     *
     * @param player the player
     */
    public static void setPlayer(Player player) {
        Player.player = player;
    }

    /**
     * Returns the name of the player
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the castles of the player
     *
     * @return the castles of the player
     */
    public List<Castle> getCastles() {
        return castles;
    }

    /**
     * Add a castle for the player
     *
     * @param door the direction of the door of the castle
     * @param pos  the position on the field of the castle
     */
    public void addCastle(Cardinal door, Point2D pos) {
        castles.add(new Castle(this, door, pos));
    }

    @Override
    public String toString() {
        return name;
    }
}
