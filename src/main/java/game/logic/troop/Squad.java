package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.Castle;
import game.logic.World;

import java.util.*;

public class Squad extends Observable {

    protected static final Set<Squad> SQUADS = new HashSet<>();
    private static final int SPACING_VALUE = Troop.DIAMETER * 2;
    private static final int OFFSET = (int) (Castle.WIDTH * 0.7);
    private static final int SHIELD_MARGIN = 40;
    private static final int FRAME_SKIP = 20;
    private static final int LOADING_CYCLES = 90;
    private final Point2D spacing;
    private int troopIndex;
    private int speed;
    private int loadingCyclesLeft;
    private int frameSkip;
    private int counter;
    private int minimumDistance;
    private List<Troop> troops;
    private Castle origin;
    private Castle target;
    private Castle currentIntersect;
    private Rectangle shield;
    private boolean isTargetAlly;
    private boolean viewDone;
    private boolean lockDir;
    private boolean combatMode;
    private Point2D center;
    private Point2D delta;
    private Point2D speedDir;
    private Point2D lastSpeedDir;
    private Point2D startingPos;

    public Squad(List<Troop> troops, Castle origin, Castle target) {
        this.troops = troops;
        this.origin = origin;
        this.target = target;
        this.speed = troops.stream().mapToInt(t -> t.speed).min().getAsInt();
        this.shield = new Rectangle(0, 0, 0, 0);

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

    public static void clearSquads() {
        SQUADS.clear();
    }

    public static Set<Squad> getSquads() {
        return Collections.unmodifiableSet(SQUADS);
    }

    public static boolean isAlive(Squad o) {
        return SQUADS.contains(o);
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void setViewDone() {
        viewDone = true;
    }

    public boolean viewNotDone() {
        return !viewDone;
    }

    public Point2D getCenter() {
        return center;
    }

    public boolean isLoading() {
        return loadingCyclesLeft > 0;
    }

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

    public boolean dirChanged() {
        if (onTarget())
            return false;
        return speedDir.x != lastSpeedDir.x || lastSpeedDir.y != speedDir.y;
    }

    public boolean allSent() {
        return troopIndex >= troops.size();
    }

    public void step() {
        if (!onTarget()) {
            counter++;
            translate(speedDir);
            if (!allSent()) {
                if (counter % frameSkip == 0)
                    walkThroughDoor();
            } else {
                if (!isLoading()) {
                    if (shield.width == 0)
                        computeCenter();
                    else
                        // updateShield();
                        computeDelta();
                    pathFind();
                } else {
                    loadingCyclesLeft--;
                }
            }
        } else {
            //System.out.println("impact");
        }

    }

    private void updateShield() {
        center.x += speedDir.x;
        center.y += speedDir.y;
        shield.x += speedDir.x;
        shield.y += speedDir.y;
    }

    public Rectangle getShield() {
        return shield;
    }

    public void kill() {
        troops.forEach(Troop::kill);
        SQUADS.remove(this);
        setChanged();
        notifyObservers();
    }

    private void move() {
        troops.stream()
                .limit(troopIndex)
                .forEach(troop -> troop.translate(speedDir.x, speedDir.y));
    }

    private boolean intersectCastle() {
        Rectangle r = shield;
        currentIntersect = Castle.getCastles().stream()
                .filter(c -> World.doOverlap(shield, c.getBoundingRect()))
                .findFirst()
                .orElse(null);
        return currentIntersect != null;
    }

    private void pathFind() {
        lastSpeedDir.setLocation(speedDir);
        if (!lockDir) {
            if (isOuTolerance(delta.x))
                speedDir.setLocation(speed * (int) (delta.x / Math.abs(delta.x)), 0);
            else if (isOuTolerance(delta.y)) {
                speedDir.setLocation(0, speed * (int) (delta.y / Math.abs(delta.y)));
            }
        }
        if (intersectCastle()) {
            if (!lockDir) {
                avoidCastle();
                lockDir = true;
            }
        } else {
            lockDir = false;
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


    private void computeCenter() {
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
        shield.width = max;
        shield.height = max;
        shield.x = (int) (center.x - max / 2);
        shield.y = (int) (center.y - max / 2);
        minimumDistance = max / 2 + (Castle.WIDTH / 2 - Castle.CENTER_CARD_OFFSET);
        System.out.println(minimumDistance);
    }

    private void computeDelta() {
        float dx = target.getCenterCard().x - center.x;
        float dy = target.getCenterCard().y - center.y;
        delta.setLocation((int) (dx), (int) (dy));
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

    private void translate(Point2D p) {
        troops.forEach(t -> t.translate(p.x, p.y));
        center.x += p.x;
        center.y += p.y;
        shield.x += p.x;
        shield.y += p.y;
    }

    private boolean onTarget() {
        if (currentIntersect != target)
            return false;

        switch (target.getDoor()) {
            case SOUTH:
            case NORTH:
                return Math.abs(delta.x) < speed && Math.abs(delta.y) <= minimumDistance;
            case WEST:
            case EAST:
                return Math.abs(delta.y) < speed && Math.abs(delta.x) <= minimumDistance;
        }
        return false;
    }


    private void walkThroughDoor() {

        troops.get(troopIndex++).setCenterPos(startingPos.x - spacing.x, startingPos.y - spacing.y);
        if (!allSent())
            troops.get(troopIndex++).setCenterPos(startingPos);
        if (!allSent())
            troops.get(troopIndex++).setCenterPos(startingPos.x + spacing.x, startingPos.y + spacing.y);
    }

}
