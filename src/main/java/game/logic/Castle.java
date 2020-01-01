package game.logic;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.troop.*;


import java.util.*;
import java.util.stream.Collectors;

public class Castle {

    private static final Set<Castle> CASTLES = new HashSet<>();

    public static int WIDTH = 100;
    public static int HEIGHT = 100;
    private static int tempCounter = 0;

    private Rectangle boundingRect;
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
        CASTLES.add(this);
    }

    public Castle(Player owner, Cardinal door, Point2D position) {
        this(owner, door);
        boundingRect = new Rectangle((int)position.x, (int)position.y, WIDTH, HEIGHT);
        center = new Point2D(
                (float) this.getBoundingRect().x + Castle.WIDTH / 2.0f,
                (float) this.getBoundingRect().y + Castle.WIDTH / 2.0f
        );
        Osts = new ArrayList<>();
    }

    public static Set<Castle> getCastles() {
        return Collections.unmodifiableSet(CASTLES);
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

    public void addOst(List<Troop> troops, Castle dest) {
        Osts.add(new Ost(troops, this, dest));
    }

    public boolean produce(TroopType t) {
        if (money >= t.getCost()) {
            producer.addTroop(t);
            money -= t.getCost();
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

    public List<Onager> getOnagers() {
        return troops.stream().filter(troop -> troop instanceof Onager).map(troop -> (Onager) troop).collect(Collectors.toList());
    }

    public List<Pikeman> getPikemen() {
        return troops.stream().filter(troop -> troop instanceof Pikeman).map(troop -> (Pikeman) troop).collect(Collectors.toList());
    }

    public List<Knight> getKnights() {
        return troops.stream().filter(troop -> troop instanceof Knight).map(troop -> (Knight) troop).collect(Collectors.toList());
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

    public Rectangle getBoundingRect() {
        return boundingRect;
    }
}
