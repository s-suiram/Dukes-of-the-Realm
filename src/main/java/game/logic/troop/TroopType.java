package game.logic.troop;

public enum TroopType {
    KNIGHT(20),
    ONAGER(50),
    PIKE_MAN(5);

    final int time;

    TroopType(int time) {
        this.time = time;
    }

    public int getTime() {
        return time;
    }
}