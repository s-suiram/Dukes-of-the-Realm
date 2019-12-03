package game.logic;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class World {
    private static World instance;

    private List<Player> players;

    public World() {
        players = new ArrayList<>();
    }

    public static World getInstance() {
        if (instance == null) instance = new World();
        return instance;
    }

    public void step() {
        for (Player p : players) {
            p.getCastles().forEach(Castle::step);
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Optional<Player> getPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    public void addPlayer(String name) {
        players.add(new Player(name));
    }

    public List<Castle> getCastles() {
        List<Castle> c = new ArrayList<>();
        players.forEach(p -> c.addAll(p.getCastles()));
        return c;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        players.forEach(p -> s.append(p.toString()));
        return s.toString();
    }
}
