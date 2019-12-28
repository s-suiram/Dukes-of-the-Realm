package game.logic.troop;

import com.sun.javafx.geom.Point2D;

public abstract class Troop {

    public static final int SIZE = 10;
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
    }

    public Point2D getPos() {
        return pos;
    }

    @Override
    public String toString() {
        return name + "\n";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Troop) {
            return speed == ((Troop) obj).speed
                    && damage == ((Troop) obj).damage
                    && hp == ((Troop) obj).hp
                    && name.equals(((Troop) obj).name);
        }
        return false;
    }
}
