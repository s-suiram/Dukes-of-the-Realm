package game.logic;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A utility enum to define directions
 */
public enum Cardinal {
    NORTH,
    SOUTH,
    EAST,
    WEST;

    /**
     * Returns the values of the enum without the ones specified in exclude
     *
     * @param exclude the exclude list
     * @return the values of the enum without the ones specified in exclude
     */
    public static List<Cardinal> valuesMinus(List<Cardinal> exclude) {
        return Arrays.stream(values()).filter(card -> !exclude.contains(card)).collect(Collectors.toList());
    }
}
