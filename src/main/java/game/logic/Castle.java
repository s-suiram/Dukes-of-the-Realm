package game.logic;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.*;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Castle {

    public static int WIDTH = 100;
    public static int HEIGHT = 100;
    private static int tempCounter = 0;

    private Rectangle2D boundingRect;
    private Point2D center;
    private Player owner;
    private int tempId;
    private int money;
    private int level;
    private int timeToLevelUp;
    private List<Troop> troops;
    private List<Ost> Osts;
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
        boundingRect = new Rectangle2D(position.x, position.y, WIDTH, HEIGHT);
        center = new Point2D(
                (float) this.getBoundingRect().getMinX() + Castle.WIDTH / 2.0f,
                (float) this.getBoundingRect().getMinY() + Castle.WIDTH / 2.0f
        );
        Osts = new ArrayList<>();
    }

    public int levelUpPrice() {
        return (level + 1) * 1000;
    }

    public boolean startLevelUp() {
        if (timeToLevelUp == -1 && money - ((level + 1) * 1000) >= 0) {
            money -= (level + 1) * 1000;
            timeToLevelUp = 100 + 50 * (level + 1);
            return true;
        }
        return false;
    }

    public void generateOst() {
        int nb = 12;//(int) (Math.random() * 30) + 10;
        List<Troop> troops = new ArrayList<>();
        for (int i = 0; i < nb; i++) {
            int rnd = (int) (Math.random() * 3);
            troops.add(rnd == 0 ? new Onager() : rnd == 1 ? new Pikeman() : new Knight());
        }
        Osts.add(new Ost(troops, this, null));
    }


    public boolean produce(TroopType t) {
        if (money >= t.getCost()) {
            producer.addTroop(t);
            return true;
        }
        return false;
    }

    public void step() {
        if (owner instanceof NeutralDukes) {
            money += level;
        } else {
            money += level * 10;
        }

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

    public List<Ost> getOsts() {
        return Osts;
    }

    public Point2D getCenter() {
        return center;
    }

    public List<Troop> getOstsTroops() {
        return Osts.stream().flatMap(o -> o.getTroops().stream()).collect(Collectors.toList());
    }

    public int getTimeToLevelUp() {
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

    public Rectangle2D getBoundingRect() {
        return boundingRect;
    }
}
