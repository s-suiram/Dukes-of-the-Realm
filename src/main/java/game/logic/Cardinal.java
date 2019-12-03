package game.logic;

public enum Cardinal {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    public static Cardinal fromString(String s) {
        switch (s.toLowerCase()) {
            case "north":
                return NORTH;
            case "south":
                return SOUTH;
            case "east":
                return EAST;
            case "west":
                return WEST;
            default:
                throw new IllegalArgumentException(s + " is not a cardinal value");
        }
    }
}
