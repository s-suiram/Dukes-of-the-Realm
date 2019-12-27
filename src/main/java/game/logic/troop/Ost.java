package game.logic.troop;

import com.sun.javafx.geom.Point2D;
import game.logic.Castle;

import java.util.List;

public class Ost {

    private static final int MAX_THROUGH = 3;
    private static final Point2D SPACING = new Point2D();
    private static final int SPACING_VALUE = (int) (Troop.SIZE * 2 );
    private static final int OFFSET = (int) (Castle.WIDTH*0.7);

    private List<Troop> troops;
    private int troopIndex;
    private Castle origin;
    private Castle target;
    private boolean isTargetAlly;
    private Point2D startingPos;

    public Ost(List<Troop> troops, Castle origin, Castle target) {
        this.troops = troops;
        this.origin = origin;
        this.target = target;
        //isTargetAlly = origin.getOwner() == target.getOwner();
        troopIndex = 0;
        troops.sort((o1, o2) -> Integer.compare(o2.speed, o1.speed));
        startingPos = new Point2D();
        computeStartingPos();
        walkThroughDoor();
    }

    public List<Troop> getTroops() {
        return troops;
    }

    public void step () {

    }

    private void computeStartingPos() {
        Point2D center = new Point2D(
                (float) origin.getBoundingRect().getMinX() + Castle.WIDTH/2,
                (float) origin.getBoundingRect().getMinY() + Castle.WIDTH/2
        );
        switch (origin.getDoor()) {
            case NORTH:
                startingPos.setLocation(center.x, center.y - OFFSET);
                SPACING.setLocation(SPACING_VALUE,0);
                break;
            case SOUTH:
                startingPos.setLocation(center.x, center.y + OFFSET);
                SPACING.setLocation(SPACING_VALUE,0);
                break;
            case EAST:
                startingPos.setLocation(center.x + OFFSET, center.y);
                SPACING.setLocation(0,SPACING_VALUE);
                break;
            case WEST:
                startingPos.setLocation(center.x - OFFSET , center.y);
                SPACING.setLocation(0,SPACING_VALUE);
                break;
        }
        origin.getTroops().removeAll(troops);
    }

    private void walkThroughDoor() {
            troops.get(troopIndex).pos.setLocation(startingPos.x - SPACING.x, startingPos.y - SPACING.y);
            troops.get(troopIndex+1).pos.setLocation(startingPos);
            troops.get(troopIndex+2).pos.setLocation(startingPos.x + SPACING.x, startingPos.y + SPACING.y);
            troopIndex += MAX_THROUGH;
    }

}
