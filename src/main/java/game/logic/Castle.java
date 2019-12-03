package game.logic;

import game.logic.troop.Troop;
import game.logic.troop.TroopProducer;
import game.logic.troop.TroopType;

import java.util.ArrayList;
import java.util.List;

public class Castle {
    private Player owner;
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

        if (timeToLevelUp != -1) {
            if (timeToLevelUp != 0) {
                level++;
            } else {
                timeToLevelUp--;
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
                "id: " + super.toString() + "\n" +
                "door: " + door + "\n" +
                "money: " + money + "\n" +
                "level: " + level + "\n" +
                "troops :\n");
        troops.forEach(t -> s.append(t.getName()));
        s.append("\n");
        s.append(producer);
        return s.toString();
    }
}
