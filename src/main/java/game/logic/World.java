package game.logic;

import com.sun.javafx.geom.Point2D;
import javafx.geometry.Rectangle2D;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class World {

    public static int FIELD_WIDTH = 2000;
    public static int FIELD_HEIGHT = 2000;

    private static World instance;

    private List<Player> players;

    public World() {
        players = new ArrayList<>();
    }

    private static void initSomeThings() {
        instance.addFightingDukes("Fighting1");
        instance.addFightingDukes("Fighting2");
        instance.addFightingDukes("Fighting3");

        instance.getPlayer("Fighting1").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(100, 100)));
        instance.getPlayer("Fighting1").ifPresent(it -> it.addCastle(Cardinal.EAST, new Point2D(300, 100)));
        instance.getPlayer("Fighting1").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(600, 300)));

        instance.getPlayer("Fighting2").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(1200, 1100)));
        instance.getPlayer("Fighting2").ifPresent(it -> it.addCastle(Cardinal.EAST, new Point2D(300, 1300)));
        instance.getPlayer("Fighting2").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(1600, 600)));

        instance.getPlayer("Fighting3").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(100, 1800)));
        instance.getPlayer("Fighting3").ifPresent(it -> it.addCastle(Cardinal.EAST, new Point2D(1300, 1300)));
        instance.getPlayer("Fighting3").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(1500, 1400)));

        instance.addNeutralDukes("Neutral1");
        instance.addNeutralDukes("Neutral2");
        instance.addNeutralDukes("Neutral3");

        instance.getPlayer("Neutral1").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(1800, 900)));
        instance.getPlayer("Neutral1").ifPresent(it -> it.addCastle(Cardinal.EAST, new Point2D(500, 1300)));
        instance.getPlayer("Neutral1").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(300, 300)));

        instance.getPlayer("Neutral2").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(1500, 1800)));
        instance.getPlayer("Neutral2").ifPresent(it -> it.addCastle(Cardinal.WEST, new Point2D(300, 1300)));
        instance.getPlayer("Neutral2").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(1600, 600)));

        instance.getPlayer("Neutral3").ifPresent(it -> it.addCastle(Cardinal.NORTH, new Point2D(400, 1300)));
        instance.getPlayer("Neutral3").ifPresent(it -> it.addCastle(Cardinal.EAST, new Point2D(800, 300)));
        instance.getPlayer("Neutral3").ifPresent(it -> it.addCastle(Cardinal.SOUTH, new Point2D(600, 200)));
    }

    private static void randomGen() {
        instance.addFightingDukes("Fighting1");
        instance.addFightingDukes("Fighting2");
        instance.addFightingDukes("Fighting3");

        instance.addNeutralDukes("Neutral1");
        instance.addNeutralDukes("Neutral2");
        instance.addNeutralDukes("Neutral3");
        int spaceBetweenCastle = 100;
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
                        .anyMatch(castle -> new Rectangle2D((int) randPoint.x - spaceBetweenCastle, (int) randPoint.y - spaceBetweenCastle, Castle.WIDTH + 2 * spaceBetweenCastle, Castle.HEIGHT + 2 * spaceBetweenCastle)
                                .intersects(castle.getBoundingRect())));
                Cardinal randDoor = Cardinal.values()[r.nextInt(4)];
                p.addCastle(randDoor, new Point2D(randPoint.x, randPoint.y));
            }
        });
        getInstance().getCastles().forEach(c -> System.out.println(c.getBoundingRect().getMinX() + " " + c.getBoundingRect().getMinY()));
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

    public void addFightingDukes(String name) {
        players.add(new FightingDukes(name));
    }

    public void addNeutralDukes(String name) {
        players.add(new NeutralDukes(name));
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

    public Optional<Castle> castleById(int id) {
        return getCastles().stream().filter(c -> c.hashCode() == id).findFirst();
    }

    public boolean castleHere(Point2D here) {
        return getCastles().stream().map(Castle::getBoundingRect).anyMatch(rect -> rect.contains(here.x, here.y));
    }
}
