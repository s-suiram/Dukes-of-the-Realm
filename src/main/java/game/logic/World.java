package game.logic;

import java.util.List;

public class World {
    private static World instance;

    public static World getInstance() {
        if (instance == null) instance = new World();
        return instance;
    }

    private List<Player> players;
}
