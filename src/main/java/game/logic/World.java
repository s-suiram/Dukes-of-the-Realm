package game.logic;

import com.sun.javafx.geom.Point2D;
import game.logic.troop.Ost;
import game.logic.troop.Troop;
import game.view.App;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

public class World {

    public static int FIELD_WIDTH = 4000;
    public static int FIELD_HEIGHT = 4000;

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
                } while (getInstance()
                        .getCastles()
                        .stream()
                        .anyMatch(castle -> new Rectangle2D((int) randPoint.x - minSpace, (int) randPoint.y - minSpace, Castle.WIDTH + 2 * minSpace, Castle.HEIGHT + 2 * minSpace)
                                .intersects(castle.getBoundingRect())));
                Cardinal randDoor = Cardinal.values()[r.nextInt(4)];
                p.addCastle(randDoor, new Point2D(randPoint.x, randPoint.y));
            }
        });

        getInstance().getCastles().forEach(Castle::generateOst);

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
        if (App.frames % 10 == 0)
            getCastles().forEach(Castle::step);
        getOsts().forEach(Ost::step);
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

    public List<Castle> getCastles() {
        return players.stream()
                .flatMap(player -> player.getCastles().stream())
                .collect(Collectors.toList());
    }

    public List<Troop> getTroops() {
        return getCastles().stream()
                .flatMap(castle -> castle.getOstsTroops().stream())
                .collect(Collectors.toList());
    }

    public List<Ost> getOsts() {
        return getCastles().stream()
                .flatMap(c -> c.getOsts().stream())
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        players.forEach(p -> s.append(p.toString()));
        return s.toString();
    }

    public Optional<Castle> castleById(int id) {
        return getCastles().stream().filter(c -> c.hashCode() == id).findFirst();
    }

    public boolean castleHere(Point2D here) {
        return getCastles().stream().map(Castle::getBoundingRect).anyMatch(rect -> rect.contains(here.x, here.y));
    }
}
