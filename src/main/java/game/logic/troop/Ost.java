package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.Castle;
import game.logic.World;

import java.util.*;

public class Ost extends Observable {

    protected static final Set<Ost> OSTS = new HashSet<>();
    private static final int SPACING_VALUE = Troop.DIAMETER * 2;
    private static final int OFFSET = (int) (Castle.WIDTH * 0.7);
    private static final int SHIELD_MARGIN = 20;
    private static final int FRAME_SKIP = 20;
    private static final int STEP_WAIT = 90;

    private int troopIndex;
    private int speed;
    private int stepsLefts;
    private int frameSkip;
    private int counter;

    private List<Troop> troops;
    private Castle origin;
    private Castle target;
    private Rectangle shield;

    private boolean isTargetAlly;
    private boolean viewDone;
    private boolean intersecting;
    private boolean lockDir;

    private Point2D center;
    private Point2D delta;
    private Point2D speedDir;
    private Point2D lastSpeedDir;
    private Point2D startingPos;
    private final Point2D spacing;

    public Ost(List<Troop> troops, Castle origin, Castle target) {
        this.troops = troops;
        this.origin = origin;
        this.target = target;
        this.speed = troops.stream().mapToInt(t -> t.speed).min().getAsInt();
        this.shield = new Rectangle(0, 0, 0, 0);

        this.viewDone = false;
        this.lockDir = false;
        this.troopIndex = 0;
        this.counter = 0;
        this.stepsLefts = STEP_WAIT / speed;
        this.frameSkip = FRAME_SKIP / speed;

        this.spacing = new Point2D();
        this.speedDir = new Point2D();
        this.lastSpeedDir = new Point2D();
        this.delta = new Point2D();
        this.startingPos = new Point2D();
        this.center = new Point2D();

        this.isTargetAlly = origin.getOwner() == target.getOwner();
        this.troops.sort(Comparator.comparingInt(o -> o.speed));
        this.origin.getTroops().removeAll(troops);
        computeStartingPos();
        OSTS.add(this);
    }

    public static Set<Ost> getOsts() {
        return Collections.unmodifiableSet(OSTS);
    }

    public static boolean isAlive(Ost o) {
        return OSTS.contains(o);
    }

    private static void rotate(Point2D p, int angle, Point2D o) {
        double s = Math.sin(Math.toRadians(angle));
        double c = Math.cos(Math.toRadians(angle));

        p.x -= o.x;
        p.y -= o.y;

        double x = p.x * c - p.y * s;
        double y = p.x * s + p.y * c;

        p.setLocation((float) x + o.x, (float) y + o.y);
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

    public boolean allSent() {
        return troopIndex >= troops.size();
    }

    public void step() {
        counter++;
        move();
        if (!allSent()) {
            if (counter % frameSkip == 0)
                walkThroughDoor();
        } else {
            if (stepsLefts == 0) {
                if (shield.width == 0)
                    computeCenter();
                else
                    updateShield();
                computeDelta();
                pathFind();
            } else {
                stepsLefts--;
            }
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
        OSTS.remove(this);
        setChanged();
        notifyObservers();
    }

    private void move() {
        troops.stream()
                .limit(troopIndex)
                .forEach(troop -> {
                    troop.translate(speedDir.x, speedDir.y);
                });

    }

    private void rollback() {
        troops.forEach(t -> t.translate(-speedDir.x, -speedDir.y));
    }

    private boolean intersectCastle() {
        Rectangle r = shield;
        intersecting = Castle.getCastles().stream()
                .anyMatch(c -> World.doOverlap(shield, c.getBoundingRect()));
        return intersecting;
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
        float width = (float) (Troop.DIAMETER + SHIELD_MARGIN + maxX - minX) * 2;
        float height = (float) (Troop.DIAMETER + SHIELD_MARGIN + maxY - minY) * 2;
        int max = (int) Math.max(width, height);
        center.setLocation(avgx, avgy);
        shield.width = max;
        shield.height = max;
        shield.x = (int) (center.x - max / 2);
        shield.y = (int) (center.y - max / 2);
        System.out.println(center);
    }

    private void computeDelta() {
        float dx = target.getBoundingRect().x + Castle.WIDTH / 2 - center.x;
        float dy = target.getBoundingRect().y + Castle.WIDTH / 2 - center.y;
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

    private void walkThroughDoor() {

        troops.get(troopIndex++).setCenterPos(startingPos.x - spacing.x, startingPos.y - spacing.y);
        if (!allSent())
            troops.get(troopIndex++).setCenterPos(startingPos);
        if (!allSent())
            troops.get(troopIndex++).setCenterPos(startingPos.x + spacing.x, startingPos.y + spacing.y);
    }

}
