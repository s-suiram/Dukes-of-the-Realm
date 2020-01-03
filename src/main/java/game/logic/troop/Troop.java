package game.logic.troop;

import game.logic.utils.Point;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

/**
 * The Troop class represent the model of a troop in the game
 */
public abstract class Troop extends Observable implements Serializable {

    /**
     * Radius of a Troop
     */
    public static final int RADIUS = 5;
    /**
     * Diameter of a Troop
     */
    public static final int DIAMETER = RADIUS * 2;
    /**
     * Set of all the troops created since the beginning of the game
     */
    protected static final Set<Troop> TROOPS = new HashSet<>();
    /**
     * Speed of the Troop, define how fast the troop will be on the field
     */
    public final int speed;
    /**
     * Store the damage of the Troop
     */
    public final int damage;
    /**
     * Store the life of the Troop
     */
    public final int hp;
    /**
     * Store the name of the Troop (Pikeman, Knight or Onager)
     */
    public final String name;

    /**
     * Store the center of the Troop
     */
    private Point centerPos;

    private boolean viewDone;
    /**
     * Store the squad the Troop is part of, if the troop still belongs to a Castle, squad is null
     */
    private Squad squad;

    /**
     * Build a Troop
     *
     * @param speed  the speed of the Troop
     * @param damage the damage of the Troop
     * @param hp     the health point of the Troop
     * @param name   the name of the Troop
     */
    public Troop(int speed, int damage, int hp, String name) {
        this.speed = speed;
        this.damage = damage;
        this.hp = hp;
        this.name = name;
        this.viewDone = false;
        this.centerPos = new Point();
        squad = null;
        TROOPS.add(this);
    }

    /**
     * Return whether the troop is alive or not
     *
     * @param t the troop
     * @return true if the Troop is alive and false if not
     */
    public static boolean isAlive(Troop t) {
        return TROOPS.contains(t);
    }

    /**
     * Clear all the troops created
     */
    public static void clearTroops() {
        TROOPS.clear();
    }

    /**
     * Returns the center position of the troop
     *
     * @return the center position of the troop
     */
    public Point getCenterPos() {
        return centerPos;
    }

    /**
     * Define the center of the troop with a Point2D
     *
     * @param centerPos the center
     */
    public void setCenterPos(Point centerPos) {
        this.centerPos.setLocation(centerPos);
    }

    /**
     * Returns the speed of the Troop
     *
     * @return the speed of the Troop
     */
    public int getSpeed() {
        return speed;
    }

    /**
     * Assign a Squad to a troop
     *
     * @param o the squad
     */
    public void setSquad(Squad o) {
        this.squad = o;
    }

    public double getRelativeX() {
        return centerPos.x - squad.getHitbox().x;
    }

    public double getRelativeY() {
        return centerPos.y - squad.getHitbox().y;
    }

    /**
     * Remove the troop
     */
    public void kill() {
        TROOPS.remove(this);
        setChanged();
        notifyObservers();
    }

    /**
     * Move the troop by the specified coordinates
     *
     * @param x the x increment
     * @param y the y increment
     */
    public void translate(double x, double y) {
        setCenterPos(centerPos.x + x, centerPos.y + y);
    }

    /**
     * Define the center of the troop
     *
     * @param x the x position
     * @param y the y position
     */
    public void setCenterPos(double x, double y) {
        this.centerPos.setLocation((float) x, (float) y);
    }

    /**
     * Specify that the troop view should disappear
     */
    public void setViewDone() {
        viewDone = true;
    }

    /**
     * Returns the negation of viewDone
     *
     * @return the negation of viewDone
     */
    public boolean viewNotDone() {
        return !viewDone;
    }

    @Override
    public String toString() {
        return name + "\n";
    }

}
