package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.Castle;
import game.logic.World;

import java.util.*;

/**
 * The type Squad.
 */
public class Squad extends Observable {

    private static final Set<Squad> SQUADS = new HashSet<>();
    private static final int SPACING_VALUE = Troop.DIAMETER * 2;
    private static final int OFFSET = (int) (Castle.SIZE * 0.7);
    private static final int SHIELD_MARGIN = 40;
    private static final int FRAME_SKIP = 20;
    private static final int LOADING_CYCLES = 90;
    private final Point2D spacing;
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
    private boolean viewDone;
    private boolean lockDir;
    private boolean combatMode;
    private Point2D center;
    private Point2D delta;
    private Point2D speedDir;
    private Point2D lastSpeedDir;
    private Point2D startingPos;

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
        this.hitbox = new Rectangle(0, 0, 0, 0);

        this.viewDone = false;
        this.lockDir = false;
        this.combatMode = false;

        this.troopIndex = 0;
        this.counter = 0;
        this.loadingCyclesLeft = LOADING_CYCLES / speed;
        this.frameSkip = FRAME_SKIP / speed;

        this.spacing = new Point2D();
        this.speedDir = new Point2D();
        this.lastSpeedDir = new Point2D();
        this.delta = new Point2D((float) Double.MAX_VALUE, (float) Double.MAX_VALUE);
        this.startingPos = new Point2D();
        this.center = new Point2D();

        this.isTargetAlly = origin.getOwner() == target.getOwner();
        this.troops.sort(Comparator.comparingInt(o -> o.speed));
        this.origin.getTroops().removeAll(troops);
        troops.forEach(troop -> troop.setSquad(this));
        computeStartingPos();
        SQUADS.add(this);
    }

    /**
     * Clear squads.
     */
    public static void clearSquads() {
        SQUADS.clear();
    }

    /**
     * Gets squads.
     *
     * @return the squads
     */
    public static Set<Squad> getSquads() {
        return Collections.unmodifiableSet(SQUADS);

    }

    /**
     * Is alive boolean.
     *
     * @param o the o
     * @return the boolean
     */
    public static boolean isAlive(Squad o) {
        return SQUADS.contains(o);
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
     * Sets view done.
     */
    public void setViewDone() {
        viewDone = true;
    }

    /**
     * View not done boolean.
     *
     * @return the boolean
     */
    public boolean viewNotDone() {
        return !viewDone;
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
        SQUADS.remove(this);
        setChanged();
        notifyObservers();
    }

    private boolean isInitDone(){
        return hitbox.width != 0;
    }

    private boolean intersectCastle() {
        return currentIntersect != null;
    }

    private boolean intersectTarget(){
        return currentIntersect == target;
    }

    private void computeIntersection(){
        currentIntersect = Castle.getCastles().stream()
                .filter(c -> World.doOverlap(hitbox, c.getBoundingRect()))
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
                speedDir.setLocation(speed * (int) (delta.x / Math.abs(delta.x)), 0);
            else if (isOuTolerance(delta.y)) {
                speedDir.setLocation(0, speed * (int) (delta.y / Math.abs(delta.y)));
            }
        }
    }

    private boolean isOuTolerance(double delta) {
        return !(delta > -speed) || !(delta < speed);
    }

    private void computeStartingPos() {
        Point2D center = origin.getCenter();
        switch (origin.getDoor()) {
            case NORTH:
                startingPos.setLocation(center.x, center.y - OFFSET);
                spacing.setLocation(SPACING_VALUE, 0);
                speedDir.setLocation(0, -speed);
                break;
            case SOUTH:
                startingPos.setLocation(center.x, center.y + OFFSET);
                spacing.setLocation(SPACING_VALUE, 0);
                speedDir.setLocation(0, speed);
                break;
            case EAST:
                startingPos.setLocation(center.x + OFFSET, center.y);
                spacing.setLocation(0, SPACING_VALUE);
                speedDir.setLocation(speed, 0);
                break;
            case WEST:
                startingPos.setLocation(center.x - OFFSET, center.y);
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
            Point2D p = t.getCenterPos();
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
        hitbox.width = max;
        hitbox.height = max;
        hitbox.x = (int) (center.x - max / 2);
        hitbox.y = (int) (center.y - max / 2);
    }

    private void computeDelta() {
        float dx, dy;
        if( intersectTarget()) {
            dx = target.getTargetPoint().x - center.x;
            dy = target.getTargetPoint().y - center.y;
        } else {
            dx = target.getCenter().x - center.x;
            dy = target.getCenter().y - center.y;
        }
        delta.setLocation((int) (dx), (int) (dy));
    }

    private void avoidCastle() {
        System.out.println("avoid" + speedDir);
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

    private void translate(Point2D p) {
        troops.forEach(t -> t.translate(p.x, p.y));
        center.x += p.x;
        center.y += p.y;
        hitbox.x += p.x;
        hitbox.y += p.y;
    }

    private boolean onTarget() {
        if (!intersectTarget())
            return false;

        switch (target.getDoor()) {
            case SOUTH:
            case NORTH:
                return Math.abs(delta.x) < speed && World.contains(hitbox,target.getTargetPoint());
            case WEST:
            case EAST:
                return Math.abs(delta.y) < speed && World.contains(hitbox,target.getTargetPoint());
        }
        return false;
    }

    private void walkThroughDoor() {

        troops.get(troopIndex++).setCenterPos(startingPos.x - spacing.x, startingPos.y - spacing.y);
        if (troopsLeft())
            troops.get(troopIndex++).setCenterPos(startingPos);
        if (troopsLeft())
            troops.get(troopIndex++).setCenterPos(startingPos.x + spacing.x, startingPos.y + spacing.y);
    }

}
