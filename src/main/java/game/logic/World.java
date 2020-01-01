package game.logic;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.troop.Ost;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class World {

    public static int FIELD_WIDTH = 4000;
    public static int FIELD_HEIGHT = 4000;
    public static int frames;

    public static Random generator = new Random();

    private static World instance;
    Double d = 5.0;
    private List<Player> players;
    public final Rectangle bounds;

    public World() {
        players = new ArrayList<>();
        bounds = new Rectangle(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
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
                        .map(c -> new Rectangle2D(
                                c.getBoundingRect().x,
                                c.getBoundingRect().y,
                                c.getBoundingRect().width,
                                c.getBoundingRect().height))
                        .anyMatch(rec -> new Rectangle2D(
                                (int) randPoint.x - minSpace,
                                (int) randPoint.y - minSpace,
                                Castle.WIDTH + 2 * minSpace,
                                Castle.HEIGHT + 2 * minSpace)
                                .intersects(rec))
                );
                Cardinal randDoor = Cardinal.values()[r.nextInt(4)];
                p.addCastle(randDoor, new Point2D(randPoint.x, randPoint.y));
            }
        });


    }

    private static int getBestGrid(int nbCastle) {
        int[] divisors = IntStream.range(1, nbCastle).parallel().filter(val -> nbCastle % val == 0).toArray();

        int minDiff = Integer.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < divisors.length - 1; i++) {
            if (Math.abs((nbCastle / divisors[i]) - divisors[i]) < minDiff) {
                minDiff = Math.abs((nbCastle / divisors[i]) - divisors[i]);
                index = i;
            }
        }

        return divisors[index];
    }

    public static int rand(int min, int max) {
        return generator.nextInt(max - min) + min;
    }

    private static void parallelRandomGeneration(List<String> fightingNames, List<String> neutralNames, int nbCastlePerDuke) {
        double padding = 0.25; //0.2 -> 20% smaller bounds
        int fieldForCastle = 150;

        int nbFighter = fightingNames.size();
        fightingNames.forEach(n -> getInstance().addFightingDukes(n));

        int nbNeutral = neutralNames.size();
        neutralNames.forEach(n -> getInstance().addNeutralDukes(n));

        int nbCastle = nbCastlePerDuke * (nbFighter + nbNeutral);

        FIELD_WIDTH = FIELD_HEIGHT = nbCastle * fieldForCastle;

        if (FIELD_WIDTH < Screen.getPrimary().getBounds().getMaxX())
            FIELD_WIDTH = (int) Screen.getPrimary().getBounds().getMaxX();

        if (FIELD_HEIGHT < Screen.getPrimary().getBounds().getMaxY())
            FIELD_HEIGHT = (int) Screen.getPrimary().getBounds().getMaxY();

        int height, width;

        height = getBestGrid(nbCastle);
        width = nbCastle / height;

        int widthPerTile, heightPerTile;
        widthPerTile = FIELD_WIDTH / width;
        heightPerTile = FIELD_HEIGHT / height;

        List<Rectangle2D> tiles = new ArrayList<>(nbCastle);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Rectangle2D(
                        (x * widthPerTile) + (widthPerTile * padding),
                        (y * heightPerTile) + (heightPerTile * padding),
                        widthPerTile * (1 - padding * 2),
                        heightPerTile * (1 - padding * 2)
                ));
            }
        }

        Stream.of(fightingNames, neutralNames).flatMap(Collection::stream).collect(Collectors.toList()).forEach(name -> {
            getInstance().getPlayer(name).ifPresent(p -> {
                for (int i = 0; i < nbCastlePerDuke; i++) {
                    Rectangle2D picked = tiles.remove(rand(0, tiles.size()));
                    int x = rand(((int) picked.getMinX()), ((int) picked.getMaxX() - Castle.WIDTH));
                    int y = rand(((int) picked.getMinY()), ((int) picked.getMaxY() - Castle.HEIGHT));
                    p.addCastle(Cardinal.values()[rand(0, Cardinal.values().length)], new Point2D(x, y));
                }
            });
        });
    }

    public static World getInstance() {
        if (instance == null) {
            instance = new World();
            //initSomeThings();
            parallelRandomGeneration(Arrays.asList("P1", "P2", "P3"), Arrays.asList("N1", "N2", "N3"), 5);
        }
        return instance;
    }

    public void step() {
        frames++;
        if (frames % 2== 0) {
            Castle.getCastles().forEach(Castle::step);
        }
        Ost.getOsts().forEach(Ost::step);
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
        return Castle.getCastles().stream()
                .map(Castle::getBoundingRect)
                .anyMatch(rect -> rect.contains((int) here.x, (int) here.y));
    }

    // Returns true if two rectangles (l1, r1) and (l2, r2) overlap  
    public static  boolean doOverlap(Rectangle a, Rectangle b) {
        Rectangle2D lol = new Rectangle2D(a.x, a.y, a.width, a.height);
        Rectangle2D lil = new Rectangle2D(b.x, b.y, b.width, b.height);
        return lol.intersects(lil);
    }

} 
