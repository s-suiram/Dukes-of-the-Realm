package game.logic.troop;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Queue;

/**
 * The TroopProducer class encapsulate the process of producing troops.
 * A Queue is used to produce troops in the good order one by one
 */
public class TroopProducer implements Serializable {
    /**
     * Store the troops who need to be produced, the first value of the queue is the troop being produced
     */
    private Queue<TroopRemainingTime> queue;

    /**
     * Create a producer
     */
    public TroopProducer() {
        queue = new LinkedList<>();
    }

    /**
     * Add a troop to the producing queue
     *
     * @param t the type of the troop to produce
     */
    public void addTroop(TroopType t) {
        queue.add(new TroopRemainingTime(t));
    }

    /**
     * Returns the current queue
     *
     * @return the current queue
     */
    public Queue<TroopRemainingTime> getQueue() {
        return queue;
    }

    /**
     * Make the queue progress
     *
     * @return EMPTY if the first troop is not ready to be produced or if the queue is empty, and an Optional of the troop if the first troop is ready to be produced
     */
    public Optional<Troop> step() {
        try {
            if ((queue.element().remainingTime -= 1) == 0) {
                switch (queue.remove().troopType) {
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

    /**
     * Cancel the current troop which is being produced.
     * Does nothing if the queue is empty
     */
    public void cancel() {
        if (!queue.isEmpty()) queue.remove();
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Currently in queue: \n");
        queue.forEach(s::append);
        return s.toString();
    }

    /**
     * Data class that store the remaining time and the type of troop to produce when the time is over
     */
    public static class TroopRemainingTime {
        /**
         * The type of troop to produce
         */
        TroopType troopType;

        /**
         * The remaining time before the troop is produced
         */
        int remainingTime;

        /**
         * Build a TroopRemainingTime with the specified troop type
         *
         * @param troopType the troop type
         */
        public TroopRemainingTime(TroopType troopType) {
            this.troopType = troopType;
            this.remainingTime = troopType.getTime();
        }

        /**
         * Returns the remaining time
         *
         * @return the remaining time
         */
        public int getRemainingTime() {
            return remainingTime;
        }

        /**
         * Returns the troop type
         *
         * @return the troop type
         */
        public TroopType getTroopType() {
            return troopType;
        }

        @Override
        public String toString() {
            return troopType + " : " + remainingTime + "\n";
        }
    }
}
