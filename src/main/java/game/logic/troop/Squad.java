package game.logic.troop;

import game.logic.Castle;
import game.logic.World;
import game.logic.utils.Point;
import game.logic.utils.Rectangle;

import java.io.Serializable;
import java.util.*;

/**
 * The type Squad.
 */
public class Squad implements Serializable {

    private static final int SPACING_VALUE = Troop.DIAMETER * 2;
    private static final int OFFSET = (int) (Castle.SIZE * 0.7);
    private static final int SHIELD_MARGIN = 40;
    private static final int FRAME_SKIP = 20;
    private static final int LOADING_CYCLES = 90;
    private final Point spacing;
    private int troopIndex;
    private int speed;
    private int loadingCyclesLeft;
    private int frameSkip;
    private int counter;
    private List<Troop> troops;
    private Castle origin;
    private Castle target;
    private Castle currentIntersect;
    private Rectangle hitbox;
    private boolean isTargetAlly;
    private boolean lockDir;
    private boolean combatMode;
    private Point center;
    private Point delta;
    private Point speedDir;
    private Point lastSpeedDir;
    private Point startingPos;

    /**
     * Instantiates a new Squad.
     *
     * @param troops the troops
     * @param origin the origin
     * @param target the target
     */
    public Squad(List<Troop> troops, Castle origin, Castle target) {
        this.troops = troops;
        this.origin = origin;
        this.target = target;
        this.speed = troops.stream().mapToInt(t -> t.speed).min().getAsInt();
        this.hitbox = new Rectangle(0, 0);

        this.lockDir = false;
        this.combatMode = false;

        this.troopIndex = 0;
        this.counter = 0;
        this.loadingCyclesLeft = LOADING_CYCLES / speed;
        this.frameSkip = FRAME_SKIP / speed;

        this.spacing = new Point();
        this.speedDir = new Point();
        this.lastSpeedDir = new Point();
        this.delta = new Point(Double.MAX_VALUE, Double.MAX_VALUE);
        this.startingPos = new Point();
        this.center = new Point();

        this.isTargetAlly = origin.getOwner() == target.getOwner();
        this.troops.sort(Comparator.comparingInt(o -> o.speed));
        this.origin.getTroops().removeAll(troops);
        troops.forEach(troop -> troop.setSquad(this));
        computeStartingPos();
        World.getInstance().squads.add(this);
    }


    public static Set<Squad> getSquads(){
        return  World.getInstance().squads;
    }

    /**
     * Is alive boolean.
     *
     * @param o the o
     * @return the boolean
     */
    public static boolean isAlive(Squad o) {
        return World.getInstance().squads.contains(o);
    }

    /**
     * Gets troops.
     *
     * @return the troops
     */
    public List<Troop> getTroops() {
        return troops;
    }

    /**
     * Is loading boolean.
     *
     * @return true if the squad is still loading
     */
    public boolean isLoading() {
        return loadingCyclesLeft > 0;
    }

    /**
     * Gets angle.
     *
     * @return the angle
     */
    public int getAngle() {
        int angle;
        if (speedDir.x == 0) {
            if (speedDir.y < 0)
                angle = 0;
            else
                angle = 180;
        } else {
            if (speedDir.x < 0)
                angle = 90;
            else
                angle = 270;
        }
        return angle;
    }

    /**
     * Dir changed boolean.
     *
     * @return the boolean
     */
    public boolean dirChanged() {
        if (onTarget())
            return false;
        return speedDir.x != lastSpeedDir.x || lastSpeedDir.y != speedDir.y;
    }

    /**
     * All sent boolean.
     *
     * @return true if some troops aren't sent yet
     */
    public boolean troopsLeft() {
        return troopIndex < troops.size();
    }

    /**
     * Updates the squad.
     */
    public void step() {

        if (!onTarget()) {
            counter++;
            translate(speedDir);
            if (troopsLeft()) {
                if (counter % frameSkip == 0)
                    walkThroughDoor();
            } else {
                if (!isLoading()) {
                    if (isInitDone())
                        pathFind();
                    else
                        initHitbox();
                } else {
                    loadingCyclesLeft--;
                }
            }
        } else {
            //System.out.println("impact");
        }
    }


    /**
     * Gets hitbox.
     *
     * @return the hitbox
     */
    public Rectangle getHitbox() {
        return hitbox;
    }

    /**
     * Kills the squad and its troops
     */
    public void kill() {
        troops.forEach(Troop::kill);
        World.getInstance().squads.remove(this);
    }

    private boolean isInitDone() {
        return hitbox.getWidth() != 0;
    }

    private boolean intersectCastle() {
        return currentIntersect != null;
    }

    private boolean intersectTarget() {
        return currentIntersect == target;
    }

    private void computeIntersection() {
        currentIntersect = Castle.getCastles().stream()
                .filter(c -> hitbox.intersects(c.getBoundingRect()))
                .findFirst()
                .orElse(null);
    }

    private void pathFind() {
        computeIntersection();
        computeDelta();
        lastSpeedDir.setLocation(speedDir);

        if (intersectCastle()) {
            if (!lockDir) {
                avoidCastle();
                lockDir = true;
            }
        } else {
            lockDir = false;
        }

        if (!lockDir) {
            if (isOuTolerance(delta.x))
                speedDir.setLocation(World.compare(delta.x), 0).mul(speed);
            else if (isOuTolerance(delta.y)) {
                speedDir.setLocation(0, World.compare(delta.y)).mul(speed);
            }
        }
    }

    private boolean isOuTolerance(double delta) {
        return !(delta > -speed) || !(delta < speed);
    }

    private void computeStartingPos() {
        Point center = origin.getCenter();
        switch (origin.getDoor()) {
            case NORTH:
                startingPos.setLocation(0, -OFFSET).add(center);
                spacing.setLocation(SPACING_VALUE, 0);
                speedDir.setLocation(0, -speed);
                break;
            case SOUTH:
                startingPos.setLocation(0, OFFSET).add(center);
                spacing.setLocation(SPACING_VALUE, 0);
                speedDir.setLocation(0, speed);
                break;
            case EAST:
                startingPos.setLocation(OFFSET, 0).add(center);
                spacing.setLocation(0, SPACING_VALUE);
                speedDir.setLocation(speed, 0);
                break;
            case WEST:
                startingPos.setLocation(- OFFSET,0).add(center);
                spacing.setLocation(0, SPACING_VALUE);
                speedDir.setLocation(-speed, 0);
                break;
        }
        lastSpeedDir.setLocation(speedDir);
    }


    private void initHitbox() {
        double maxX = Double.MIN_VALUE;
        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Troop t : troops) {
            Point p = t.getCenterPos();
            if (minX > p.x) {
                minX = p.x;
            } else if (maxX < p.x) {
                maxX = p.x;
            }

            if (minY > p.y) {
                minY = p.y;
            } else if (maxY < p.y) {
                maxY = p.y;
            }
        }
        if (maxX < minX)
            maxX = minX;
        if (maxY < minY)
            maxY = minY;

        float avgx = (float) (maxX + minX) / 2;
        float avgy = (float) (maxY + minY) / 2;
        float width = (float) (Troop.DIAMETER + SHIELD_MARGIN + maxX - minX);
        float height = (float) (Troop.DIAMETER + SHIELD_MARGIN + maxY - minY);
        int max = (int) Math.max(width, height);
        center.setLocation(avgx, avgy);
        hitbox = new Rectangle(center.x - max / 2, center.y - max / 2, max, max);
        System.out.println(center);
        System.out.println(hitbox);
    }

    private void computeDelta() {
        if (intersectTarget())
            delta.setLocation(target.getTargetPoint().cpy().sub(center));
        else
            delta.setLocation(target.getCenter().cpy().sub(center));
    }

    private void avoidCastle() {
        if (speedDir.x == 0) {
            if (delta.x > 0)
                speedDir.setLocation(speed, 0);
            else
                speedDir.setLocation(-speed, 0);
        } else {
            if (delta.y > 0)
                speedDir.setLocation(0, speed);
            else
                speedDir.setLocation(0, -speed);
        }
    }

    private void translate(Point p) {
        troops.forEach(t -> t.translate(p.x, p.y));
        center.x += p.x;
        center.y += p.y;
        hitbox.x += p.x;
        hitbox.y += p.y;
    }

    private boolean onTarget() {
        if (!hitbox.contains(target.getTargetPoint()))
            return false;

        switch (target.getDoor()) {
            case SOUTH:
            case NORTH:
                return Math.abs(delta.x) < speed;
            case WEST:
            case EAST:
                return Math.abs(delta.y) < speed;
            default:
                return false;
        }
    }

    private void walkThroughDoor() {

        troops.get(troopIndex++).setCenterPos(startingPos.x - spacing.x, startingPos.y - spacing.y);
        if (troopsLeft())
            troops.get(troopIndex++).setCenterPos(startingPos);
        if (troopsLeft())
            troops.get(troopIndex++).setCenterPos(startingPos.x + spacing.x, startingPos.y + spacing.y);
    }
}
