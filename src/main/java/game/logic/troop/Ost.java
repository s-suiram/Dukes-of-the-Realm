package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;

import java.util.List;

public class Ost {

    private static final int MAX_THROUGH = 3;
    private static final Point2D SPACING = new Point2D();
    private static final int SPACING_VALUE = Troop.SIZE * 2;
    private static final int OFFSET = (int) (Castle.WIDTH * 0.7);
    private static final int FRAME_SKIP = 20;

    private List<Troop> troops;
    private int troopIndex;
    private Castle origin;
    private Castle target;
    private boolean isTargetAlly;
    private Point2D startingPos;
    private int speed;
    private int frameCount;
    private final Point2D spacing;

    public Ost(List<Troop> troops, Castle origin, Castle target) {
        this.troops = troops;
        this.origin = origin;
        this.target = target;
        this.spacing = new Point2D();
        speed = troops.stream().mapToInt(t -> t.speed).min().getAsInt();
        //isTargetAlly = origin.getOwner() == target.getOwner();
        troopIndex = 0;
        troops.sort((o1, o2) -> Integer.compare(o2.speed, o1.speed));
        startingPos = new Point2D();
        computeStartingPos();
        walkThroughDoor();
        frameCount = 0;
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void step() {
        move();
        if (++frameCount == FRAME_SKIP) {
            frameCount = 0;
            if (troopIndex < troops.size())
                walkThroughDoor();
        }
    }

    private void move() {
        troops.stream()
                .limit(troopIndex)
                .forEach(troop -> {
                    switch (origin.getDoor()) {
                        case NORTH:
                            troop.pos.y -= speed;
                            break;
                        case SOUTH:
                            troop.pos.y += speed;
                            break;
                        case EAST:
                            troop.pos.x += speed;
                            break;
                        case WEST:
                            troop.pos.x -= speed;
                            break;
                    }
                });
    }

    private void computeStartingPos() {
        Point2D center = origin.getCenter();
        switch (origin.getDoor()) {
            case NORTH:
                startingPos.setLocation(center.x, center.y - OFFSET);
                spacing.setLocation(SPACING_VALUE, 0);
                break;
            case SOUTH:
                startingPos.setLocation(center.x, center.y + OFFSET);
                spacing.setLocation(SPACING_VALUE, 0);
                break;
            case EAST:
                startingPos.setLocation(center.x + OFFSET, center.y);
                spacing.setLocation(0, SPACING_VALUE);
                break;
            case WEST:
                startingPos.setLocation(center.x - OFFSET, center.y);
                spacing.setLocation(0, SPACING_VALUE);
                break;
        }
        origin.getTroops().removeAll(troops);
    }

    private void walkThroughDoor() {
        if(troopIndex < troops.size() )
            troops.get(troopIndex++).pos.setLocation(startingPos.x - spacing.x, startingPos.y - spacing.y);
        if (troopIndex < troops.size())
            troops.get(troopIndex++).pos.setLocation(startingPos);
        if (troopIndex < troops.size())
            troops.get(troopIndex++).pos.setLocation(startingPos.x + spacing.x, startingPos.y + spacing.y);

    }

}
