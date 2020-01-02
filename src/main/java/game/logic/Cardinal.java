package game.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public static List<Cardinal> valuesMinus(List<Cardinal> c) {
        return Arrays.stream(values()).filter(card -> !c.contains(card)).collect(Collectors.toList());
    }
}
