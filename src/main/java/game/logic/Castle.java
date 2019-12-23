package game.logic;

import game.logic.troop.Troop;
import game.logic.troop.TroopProducer;
import game.logic.troop.TroopType;
import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;

public class Castle {

    private static int tempCounter = 0;

    private Rectangle2D spatialRepresentation;

    private Player owner;
    private int tempId;
    private int money;
    private int level;
    private int timeToLevelUp;
    private List<Troop> troops;

    private Cardinal door;

    private TroopProducer producer;

    public Castle(Player owner, Cardinal door) {
        this.owner = owner;
        money = 0;
        level = 1;
        timeToLevelUp = -1;
        troops = new ArrayList<>();
        this.door = door;
        producer = new TroopProducer();
        tempId = tempCounter++;
    }

    public Castle(Player owner, Cardinal door, Point2D position) {
        this(owner, door);
        spatialRepresentation = new Rectangle2D(position.getX(), position.getY(), 50, 50);
    }

    public boolean startLevelUp() {
        if (money - ((level + 1) * 1000) >= 0) {
            money -= (level + 1) * 1000;
            timeToLevelUp = 100 + 50 * (level + 1);
            return true;
        }
        return false;
    }

    public void produce(TroopType t, int n) {
        producer.addTroop(t, n);
    }

    public void produce(TroopType t) {
        producer.addTroop(t);
    }

    public void step() {
        money += level * 10;
        producer.step().ifPresent(troop -> troops.add(troop));

        if (timeToLevelUp > -1) {
            timeToLevelUp--;
            if (timeToLevelUp == 0) {
                level++;
            }
        }
    }

    public Player getOwner() {
        return owner;
    }

    public int getMoney() {
        return money;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getTimeToLevelUp() {
        return timeToLevelUp;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public Cardinal getDoor() {
        return door;
    }

    public TroopProducer getProducer() {
        return producer;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Owner: " + owner + "\n" +
                "id: " + hashCode() + "\n" +
                "door: " + door + "\n" +
                "money: " + money + "\n" +
                "level: " + level + "\n" +
                "time before next level: " + (timeToLevelUp == -1 ? "no level up currently" : timeToLevelUp) + "\n" +
                "troops :\n");
        troops.forEach(s::append);
        s.append(producer);
        return s.toString();
    }

    @Override
    public int hashCode() {
        return tempId;
    }

    public Rectangle2D getSpatialRepresentation() {
        return spatialRepresentation;
    }
}
