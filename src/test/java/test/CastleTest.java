package test;

import game.logic.Castle;
import game.logic.World;
import game.logic.troop.Pikeman;
import game.logic.troop.TroopType;
import javafx.geometry.Point2D;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CastleTest {
    @Test
    public void moneyFromLevelNoStep() {
        Castle c = new Castle(null, null);
        assertEquals(0, c.getMoney());
    }

    @Test
    public void moneyFromLevelWithStep() {
        Castle c = new Castle(null, null);
        c.step();
        assertEquals(10, c.getMoney());
    }

    @Test
    public void moneyFromLevelWithMultipleStep() {
        Castle c = new Castle(null, null);
        for (int i = 0; i < 10; i++) c.step();
        assertEquals(100, c.getMoney());
    }

    @Test
    public void moneyFromLevelWithMultipleStepWithDifferentLevel() {
        Castle c = new Castle(null, null);
        for (int i = 0; i < 15; i++) c.step();
        c.setLevel(2);
        for (int i = 0; i < 9; i++) c.step();
        c.setLevel(3);
        for (int i = 0; i < 5; i++) c.step();
        assertEquals(480, c.getMoney());
    }

    @Test
    public void moneyFromLevelWithLevelUp() {
        Castle c = new Castle(null, null);
        c.setLevel(2);
        c.step();
        assertEquals(20, c.getMoney());
    }

    @Test
    public void produceNoOrder() {
        Castle c = new Castle(null, null);
        c.step();
        assertTrue(c.getTroops().isEmpty());
    }

    @Test
    public void produceSimpleOrder() {
        Castle c = new Castle(null, null);
        c.produce(TroopType.PIKE_MAN);
        for (int i = 0; i < TroopType.PIKE_MAN.getTime(); i++) c.step();
        assertEquals(1, c.getTroops().size());
        assertTrue(c.getTroops().get(0) instanceof Pikeman);
    }

    @Test
    public void castlePosition() {
        World.getInstance().addFightingDukes("max");
        World.getInstance().getPlayer("max").ifPresent(it -> it.addCastle(null, new Point2D(500, 100)));
        assertTrue(World.getInstance().castleHere(new Point2D(500, 105)));
        assertFalse(World.getInstance().castleHere(new Point2D(500, 256)));
    }
}