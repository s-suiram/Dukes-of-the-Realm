package game.logic.troop;

public enum TroopType {
    KNIGHT(20),
    ONAGER(50),
    PIKE_MAN(5);

    final int time;

    TroopType(int time) {
        this.time = time;
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

    public int getTime() {
        return time;
    }
}