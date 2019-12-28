package game.logic.troop;

import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;

public class TroopProducer {

    private LinkedList<TroopRemainingTime> queue;

    public TroopProducer() {
        queue = new LinkedList<>();
    }

    public void addTroop(TroopType t) {
        queue.add(new TroopRemainingTime(t));
    }

    public void addTroop(TroopType t, int nb) {
        for (int i = 0; i < nb; i++)
            addTroop(t);
    }

    public LinkedList<TroopRemainingTime> getQueue() {
        return queue;
    }

    public Optional<Troop> step() {
        try {
            if ((queue.element().remainingTime -= 1) == 0) {
                switch (queue.remove().t) {
                    case KNIGHT:
                        return Optional.of(new Knight());
                    case ONAGER:
                        return Optional.of(new Onager());
                    case PIKE_MAN:
                        return Optional.of(new Pikeman());
                }
            }
            return Optional.empty();
        } catch (NoSuchElementException ignored) {
            return Optional.empty();
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Currently in queue: \n");
        queue.forEach(s::append);
        return s.toString();
    }

    public void cancel() {
        queue.remove();
    }

    public static class TroopRemainingTime {
        TroopType t;

        int remainingTime;

        public TroopRemainingTime(TroopType t) {
            this.t = t;
            this.remainingTime = t.getTime();
        }

        public int getRemainingTime() {
            return remainingTime;
        }

        public TroopType getT() {
            return t;
        }

        @Override
        public String toString() {
            return t + " : " + remainingTime + "\n";
        }
    }
}
