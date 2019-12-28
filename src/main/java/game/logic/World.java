package game.logic;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Ost;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class World {

    public static int FIELD_WIDTH = 4000;
    public static int FIELD_HEIGHT = 4000;
    public static int frames;


    private static World instance;

    private List<Player> players;

    public World() {
        players = new ArrayList<>();
    }

    private static void randomGen() {
        instance.addFightingDukes("Fighting1");
        instance.addFightingDukes("Fighting2");
        instance.addFightingDukes("Fighting3");

        instance.addNeutralDukes("Neutral1");
        instance.addNeutralDukes("Neutral2");
        instance.addNeutralDukes("Neutral3");
        int minSpace = 300;
        int nbCastlePerDukes = 5;
        World.getInstance().getPlayers().forEach(p -> {
            for (int i = 0; i < nbCastlePerDukes; i++) {
                Random r = new Random();
                final Point2D randPoint = new Point2D();
                do {
                    randPoint.x = r.nextInt(FIELD_WIDTH - Castle.WIDTH);
                    randPoint.y = r.nextInt(FIELD_HEIGHT - Castle.HEIGHT);
                } while (Castle.getCastles()
                        .stream()
                        .anyMatch(castle -> new Rectangle2D((int) randPoint.x - minSpace, (int) randPoint.y - minSpace, Castle.WIDTH + 2 * minSpace, Castle.HEIGHT + 2 * minSpace)
                                .intersects(castle.getBoundingRect())));
                Cardinal randDoor = Cardinal.values()[r.nextInt(4)];
                p.addCastle(randDoor, new Point2D(randPoint.x, randPoint.y));
            }
        });

        Castle.getCastles().forEach(Castle::generateOst);
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
            //initSomeThings();
            randomGen();
        }
        return instance;
    }

    public void step() {
        frames++;
        if (frames % 60 == 0) {
            if (frames % 10 == 0)
                Castle.getCastles().forEach(Castle::step);
            Ost.getOsts().forEach(Ost::step);
        }
        if (frames == 60) frames = 1;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Optional<Player> getPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    public void addFightingDukes(String name) {
        players.add(new FightingDukes(name));
    }

    public void addNeutralDukes(String name) {
        players.add(new NeutralDukes(name));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        players.forEach(p -> s.append(p.toString()));
        return s.toString();
    }

    public Optional<Castle> castleById(int id) {
        return Castle.getCastles().stream().filter(c -> c.hashCode() == id).findFirst();
    }

    public boolean castleHere(Point2D here) {
        return Castle.getCastles().stream().map(Castle::getBoundingRect).anyMatch(rect -> rect.contains(here.x, here.y));
    }
}
