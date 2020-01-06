package game.logic;

import game.logic.troop.TroopType;

public class NeutralDukes extends Player {
    public NeutralDukes(String name) {
        super(name);
    }

    @Override
    public void act() {
        int cs = castles.size();
        Castle c = castles.get(World.rand(0, cs));

        if (c.getFlorin() >= c.levelUpPrice() || World.rand(0, Math.max(1, 10 - c.getLevel())) == 0) {

            if (c.getTimeToLevelUp() == -1 && c.getFlorin() >= c.levelUpPrice()) {
                c.startLevelUp();
            } else if (c.getTroops().size() < 10) {
                c.produce(TroopType.ONAGER);
                c.produce(TroopType.KNIGHT);
                c.produce(TroopType.PIKE_MAN);
            }
        }
    }
}
