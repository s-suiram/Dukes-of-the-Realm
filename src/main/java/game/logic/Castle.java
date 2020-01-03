package game.logic;

import game.logic.troop.*;
import game.logic.utils.Point;
import game.logic.utils.Rectangle;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Castle define the model of a castle in the game
 */
public class Castle implements Serializable {

    /**
     * Constant which define size of a Castle
     */
    public final static int SIZE = 100;

    /**
     * Store all the created castles
     */
    private static final Set<Castle> CASTLES = new HashSet<>();

    /**
     * Position and size of the Castle
     */
    private Rectangle boundingRect;

    /**
     * Center of the Castle
     */
    private Point center;

    private Point targetPoint;

    /**
     * Store the player who owns the Castle
     */
    private Player owner;

    /**
     * Store the treasure of the Castle
     */
    private int florin;

    /**
     * Store the level of the Castle
     */
    private int level;
    private int timeToLevelUp;

    /**
     * Store the troops of the Castle
     */
    private List<Troop> troops;

    /**
     * Store the Squad this Castle launched
     */
    private List<Squad> squads;

    /**
     * Store the direction of the door
     */
    private Cardinal door;

    /**
     * Store the production unit of the Castle
     */
    private TroopProducer producer;


    /**
     * Create a new castle
     *
     * @param owner    The player who owns the Castle
     * @param door     The direction of the door
     * @param position The position of the Castle in the World
     */
    public Castle(Player owner, Cardinal door, Point position) {
        this.owner = owner;
        florin = 0;
        level = 1;
        timeToLevelUp = -1;
        troops = new ArrayList<>();
        this.door = door;
        producer = new TroopProducer();
        CASTLES.add(this);
        boundingRect = new Rectangle(position.x, position.y, SIZE, SIZE);
        center = new Point(
                this.getBoundingRect().x + Castle.SIZE / 2.0f,
                this.getBoundingRect().y + Castle.SIZE / 2.0f
        );
        targetPoint = new Point(center);
        switch (door) {
            case NORTH:
                targetPoint.y -= SIZE / 2f;
                break;
            case EAST:
                targetPoint.x += SIZE / 2f;
                break;
            case WEST:
                targetPoint.x -= SIZE/2f;
                break;
            case SOUTH:
                targetPoint.y += SIZE/2f;
                break;
        }
        squads = new ArrayList<>();
    }

    /**
     * Return all the castle created
     *
     * @return a unmodifiable set of all the castle created since the game was launched
     */
    public static Set<Castle> getCastles() {
        return Collections.unmodifiableSet(CASTLES);
    }

    /**
     * Clear the castle list
     */
    public static void clearCastle() {
        CASTLES.clear();
    }

    /**
     * Returns the level up price
     *
     * @return the level up price
     */
    public int levelUpPrice() {
        return (level + 1) * 1000;
    }

    /**
     * Launch the level up process if the castle has enough florin
     *
     * @return true if the castle has enough florin and false if a level is already ongoing or if the Castle has not enough money
     */
    public boolean startLevelUp() {
        if (timeToLevelUp == -1 && florin - levelUpPrice() >= 0) {
            florin -= levelUpPrice();
            timeToLevelUp = 100 + 50 * (level + 1);
            return true;
        }
        return false;
    }

    /**
     * Create a new squad with the given troops and the given target
     *
     * @param troops the troops who will be part of the squad
     * @param target the castle which is targeted by the squad
     */
    public void createSquad(List<Troop> troops, Castle target) {
        squads.add(new Squad(troops, this, target));
    }

    /**
     * Launch the production of the specified troop
     *
     * @param t the troop to create
     * @return true if the castle has enough florin to create the troop, and false if not
     */
    public boolean produce(TroopType t) {
        if (florin >= t.getCost()) {
            producer.addTroop(t);
            florin -= t.getCost();
            return true;
        }
        return false;
    }

    /**
     * Make the Castle state evolve
     */
    public void step() {
        if (owner instanceof NeutralDukes) {
            florin += level;
        } else {
            florin += level * 10;
        }

        producer.step().ifPresent(troop -> troops.add(troop));

        if (timeToLevelUp > -1) {
            timeToLevelUp--;
            if (timeToLevelUp == 0) {
                level++;
            }
        }
    }

    /**
     * Returns the owner of the Castle
     *
     * @return the owner of the Castle
     */
    public Player getOwner() {
        return owner;
    }

    /**
     * Returns the treasure quantity
     *
     * @return the treasure quantity
     */
    public int getFlorin() {
        return florin;
    }

    /**
     * Returns the current level
     *
     * @return the current level
     */
    public int getLevel() {
        return level;
    }

    /**
     * Returns the center of the Castle
     *
     * @return the center of the Castle
     */
    public Point getCenter() {
        return center;
    }

    public Point getTargetPoint() {
        return targetPoint;
    }

    /**
     * Returns the remaining time to level up
     *
     * @return the remaining time to level up
     */
    public int getTimeToLevelUp() {
        return timeToLevelUp;
    }

    /**
     * Returns the facts that this Castle and c are ally or not
     *
     * @param c the other Castle to test with
     * @return true if the two Castle are ally and false if not
     */
    public boolean isAllyOf(Castle c) {
        return c.getOwner() == this.owner;
    }

    /**
     * Return the current troops of the castle
     *
     * @return the current troops of the castle
     */
    public List<Troop> getTroops() {
        return troops;
    }

    /**
     * Return the direction of the door
     *
     * @return the direction of the door
     */
    public Cardinal getDoor() {
        return door;
    }

    /**
     * Return the production unit
     *
     * @return the production unit
     */
    public TroopProducer getProducer() {
        return producer;
    }

    /**
     * Returns the onagers contained in the troops of the castles
     *
     * @return the onagers
     */
    public List<Onager> getOnagers() {
        return troops.stream().filter(troop -> troop instanceof Onager).map(troop -> (Onager) troop).collect(Collectors.toList());
    }

    /**
     * Returns the pikemen contained in the troops of the castles
     *
     * @return the pikemen
     */
    public List<Pikeman> getPikemen() {
        return troops.stream().filter(troop -> troop instanceof Pikeman).map(troop -> (Pikeman) troop).collect(Collectors.toList());
    }

    /**
     * Returns the knights contained in the troops of the castles
     *
     * @return the knights
     */
    public List<Knight> getKnights() {
        return troops.stream().filter(troop -> troop instanceof Knight).map(troop -> (Knight) troop).collect(Collectors.toList());
    }

    /**
     * Returns the bounding rectangle of the Castle
     *
     * @return the bounding rectangle of the Castle
     */
    public Rectangle getBoundingRect() {
        return boundingRect;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Owner: " + owner + "\n" +
                "id: " + hashCode() + "\n" +
                "door: " + door + "\n" +
                "money: " + florin + "\n" +
                "level: " + level + "\n" +
                "time before next level: " + (timeToLevelUp == -1 ? "no level up currently" : timeToLevelUp) + "\n" +
                "troops :\n");
        troops.forEach(s::append);
        s.append(producer);
        return s.toString();
    }
}
