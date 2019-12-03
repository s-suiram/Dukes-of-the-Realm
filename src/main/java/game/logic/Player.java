package game.logic;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private List<Castle> castles;

    private String name;

    public Player(String name) {
        castles = new ArrayList<>();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<Castle> getCastles() {
        return castles;
    }

    public void addCastle(Cardinal door) {
        castles.add(new Castle(this, door));
    }

    @Override
    public String toString() {
        return name;
    }
}
