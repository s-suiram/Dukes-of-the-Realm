package game.logic;

import game.logic.troop.Troop;
import game.logic.troop.TroopType;
import game.logic.utils.DistinctList;

import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

public class FightingDukes extends Player {
    public FightingDukes(String name) {
        super(name);
    }

    @Override
    public void act() {
        int cs = castles.size();
        if (cs == 0) return;
        Castle c = castles.get(World.rand(0, cs));
        Castle t = Castle.getCastles().get(World.rand(0, Castle.getCastles().size()));
        List<Troop> troops = c.getTroops();

        if (c.getFlorin() >= c.levelUpPrice() || World.rand(0, Math.max(1, 10 - c.getLevel())) == 0) {

            if (c.getTimeToLevelUp() == -1 && c.getFlorin() >= c.levelUpPrice()) {
                c.startLevelUp();
            } else if (c.getTroops().size() < 10) {
                c.produce(TroopType.ONAGER);
                c.produce(TroopType.KNIGHT);
                c.produce(TroopType.PIKE_MAN);
            } else if (c != t && c.getSquads().size() == 0) {
                DistinctList<Troop> toSend = IntStream.range(0, World.rand(1, 10))
                        .boxed()
                        .map(i -> troops.get(World.rand(0, cs)))
                        .collect(DistinctList.collector());
                toSend.sort(Comparator.comparingInt(o -> o.speed));
                c.createSquad(toSend, t);
            }
        }
    }
}
