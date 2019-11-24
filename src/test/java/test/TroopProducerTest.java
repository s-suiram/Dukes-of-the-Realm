package test;

import game.logic.troop.Knight;
import game.logic.troop.Troop;
import game.logic.troop.TroopProducer;
import game.logic.troop.TroopType;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class TroopProducerTest {

    @Test
    void addTroopSimple() {
        TroopProducer p = new TroopProducer();
        p.getQueue().forEach(System.out::println);
        p.addTroop(TroopType.PIKE_MAN);
        p.getQueue().forEach(System.out::println);
    }

    @Test
    void addTroopMultiple() {
        TroopProducer p = new TroopProducer();
        p.getQueue().forEach(System.out::println);
        p.addTroop(TroopType.PIKE_MAN, 5);
        assertEquals(TroopType.PIKE_MAN.getTime(), p.getQueue().element().getRemainingTime());
        assertEquals(TroopType.PIKE_MAN, p.getQueue().element().getT());

        assertEquals(TroopType.PIKE_MAN, p.getQueue().get(1).getT());
        assertEquals(TroopType.PIKE_MAN.getTime(), p.getQueue().get(1).getRemainingTime());
    }

    @Test
    void step() {
        TroopProducer p = new TroopProducer();
        p.addTroop(TroopType.KNIGHT, 2);
        p.addTroop(TroopType.PIKE_MAN);
        p.addTroop(TroopType.ONAGER, 2);

        Optional<Troop> t = p.step();
        assertFalse(t.isPresent());

        assertEquals(19, p.getQueue().element().getRemainingTime());

        for (int i = 0; i < 18; i++) {
            t = p.step();
            assertFalse(t.isPresent());
        }

        t = p.step();
        assertTrue(t.isPresent());
        assertEquals(new Knight(), t.get());

        assertFalse(p.step().isPresent());

        p.getQueue().clear();

        assertFalse(p.step().isPresent());
    }
}