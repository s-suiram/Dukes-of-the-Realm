package game.logic;

import game.logic.utils.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * The Player class represent a player that owns castles and make decisions
 */
public abstract class Player implements Serializable {

    /**
     * The list of castle the player owns
     */
    protected List<Castle> castles;

    /**
     * The name of the player
     */
    protected String name;


    protected boolean isBot;

    /**
     * Build a player with the specified name
     *
     * @param name the name of the player
     */
    public Player(String name, boolean isBot) {
        castles = new ArrayList<>();
        this.name = name;
        this.isBot = isBot;
    }

    public Player(String name){
        this(name,true);
    }

    abstract public void act();

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
    public void addCastle(Cardinal door, Point pos) {
        castles.add(new Castle(this, door, pos));
    }

    @Override
    public String toString() {
        return name;
    }
}
