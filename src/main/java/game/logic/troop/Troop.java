package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import javafx.geometry.Rectangle2D;


import java.util.Collections;
import java.util.HashSet;
import java.util.Observable;
import java.util.Set;

public abstract class Troop extends Observable {


    protected static final Set<Troop> TROOPS = new HashSet<>();
    public static final int RADIUS = 5;
    public static final int DIAMETER = RADIUS*2;

    public final int speed;
    public final int damage;
    public final int hp;
    public final String name;

    private Point2D centerPos;
    private boolean viewDone;
    private Ost parent;

    public Troop(int speed, int damage, int hp, String name) {
        this.speed = speed;
        this.damage = damage;
        this.hp = hp;
        this.name = name;
        this.viewDone = false;
        this.centerPos = new Point2D();
        TROOPS.add(this);
    }

    public static boolean isAlive(Troop t) {
        return TROOPS.contains(t);
    }

    public Point2D getCenterPos() {
        return centerPos;
    }

    public int getSpeed() {
        return speed;
    }

    public void setOst(Ost o) {
        this.parent = o;
    }

    public double getRelativeX(){
        return centerPos.x - parent.getShield().x;
    }

    public double getRelativeY(){
        return centerPos.y - parent.getShield().y;
    }

    public void kill() {
        TROOPS.remove(this);
        setChanged();
        notifyObservers();
    }

    public void translate(double x, double y){
        setCenterPos(centerPos.x + x, centerPos.y + y);
    }

    public void setCenterPos(Point2D centerPos) {
        this.centerPos.setLocation(centerPos);
    }

    public void setCenterPos(double x, double y){
        this.centerPos.setLocation((float)x,(float)y);
    }

    public void setViewDone(){
        viewDone = true;
    }

    public boolean viewNotDone(){
        return !viewDone;
    }

    @Override
    public String toString() {
        return name + "\n";
    }

}
