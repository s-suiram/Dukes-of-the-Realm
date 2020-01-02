package game.logic.troop;

/**
 * Represent the type of troop
 */
public enum TroopType {
    KNIGHT(20, 500),
    ONAGER(50, 1000),
    PIKE_MAN(5, 100);

    /**
     * The number of turn required for the troop to be produced
     */
    final int time;

    /**
     * The cost of the unit
     */
    final int cost;

    TroopType(int time, int cost) {
        this.time = time;
        this.cost = cost;
    }

    public static TroopType fromString(String s) {
        switch (s.toLowerCase()) {
            case "knight":
                return KNIGHT;
            case "onager":
                return ONAGER;
            case "pikeman":
                return PIKE_MAN;
            default:
                throw new IllegalArgumentException(s + " is not a trooptype value");
        }
    }

    /**
     * Returns the time
     *
     * @return the time
     */
    public int getTime() {
        return time;
    }

    /**
     * Returns the cost of the unit
     *
     * @return the cost of the unit
     */
    public int getCost() {
        return cost;
    }
}