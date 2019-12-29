package game.logic.troop;

import com.sun.javafx.geom.Point2D;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public abstract class Troop {

    public static final int SIZE = 10;
    protected static final Set<Troop> TROOPS = new HashSet<>();
    public final Point2D pos;
    public final int speed;
    public final int damage;
    public final int hp;
    public final String name;

    public Troop(int speed, int damage, int hp, String name) {
        this.speed = speed;
        this.damage = damage;
        this.hp = hp;
        this.name = name;
        pos = new Point2D();
        TROOPS.add(this);
    }

    public static boolean isAlive(Troop t) {
        return TROOPS.contains(t);
    }

    public static Set<Troop> getTroops() {
        return Collections.unmodifiableSet(TROOPS);
    }

    public Point2D getPos() {
        return pos;
    }

    public void kill() {
        TROOPS.remove(this);
    }

    public void step() {
        if (hp == 0) {
            kill();
        }
    }

    @Override
    public String toString() {
        return name + "\n";
    }

}
