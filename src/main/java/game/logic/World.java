package game.logic;

import com.sun.javafx.geom.Point2D;
import com.sun.javafx.geom.Rectangle;
import game.logic.troop.Squad;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class World {
    public static int FIELD_WIDTH = 4000;

    public static int FIELD_HEIGHT = 4000;
    public static int frames;

    public static Random generator = new Random();

    private static World instance;

    public final Rectangle bounds;
    Double d = 5.0;
    private List<Player> players;

    public World() {
        players = new ArrayList<>();
        bounds = new Rectangle(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
        Castle.clearCastle();
        Squad.clearSquads();
    }

    public static void init(List<String> fightingNames, List<String> neutralNames, int castlePerDuke) {
        instance = new World();
        randomGeneration(fightingNames, neutralNames, castlePerDuke);
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

    private static void randomGeneration(List<String> fightingNames, List<String> neutralNames, int nbCastlePerDuke) {
        double padding = 0.1; //0.2 -> 20% smaller bounds
        int fieldForCastle = 150; //Space on the field for one castle

        int nbFighter = fightingNames.size();
        fightingNames.forEach(n -> getInstance().addFightingDukes(n));

        Player.setPlayer(World.getInstance().getPlayer(fightingNames.get(0)).get());

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

        Function<Point2D, Boolean> isRight = p -> p.x == width - 1;
        Function<Point2D, Boolean> isLeft = p -> p.x == 0;
        Function<Point2D, Boolean> isUp = p -> p.y == 0;
        Function<Point2D, Boolean> isDown = p -> p.y == height - 1;

        Function<Point2D, Cardinal> getValidDoor = p -> {
            List<Cardinal> exclude = new ArrayList<>(2);

            if (isDown.apply(p))
                exclude.add(Cardinal.SOUTH);
            if (isLeft.apply(p))
                exclude.add(Cardinal.WEST);
            if (isRight.apply(p))
                exclude.add(Cardinal.EAST);
            if (isUp.apply(p))
                exclude.add(Cardinal.NORTH);

            return Cardinal.valuesMinus(exclude).get(rand(0, 4 - exclude.size()));
        };

        Set<Integer> randSet = new TreeSet<>((o1, o2) -> {
            if (o1.equals(o2)) return 0;
            return rand(-1, 1) == 0 ? 1 : -1;
        });
        IntStream.range(0, tiles.size()).forEach(randSet::add);

        Queue<Integer> randQueue = new LinkedList<>(randSet);

        Stream.of(fightingNames, neutralNames)
                .flatMap(Collection::stream)
                .collect(Collectors.toList())
                .forEach(name -> getInstance().getPlayer(name).ifPresent(p -> {
                    for (int i = 0; i < nbCastlePerDuke; i++) {
                        int rand = randQueue.remove();
                        Rectangle2D picked = tiles.get(rand);
                        int x1 = rand / width;
                        int y1 = rand % width;

                        int x = rand(((int) picked.getMinX()), ((int) picked.getMaxX() - Castle.WIDTH));
                        int y = rand(((int) picked.getMinY()), ((int) picked.getMaxY() - Castle.HEIGHT));

                        p.addCastle(getValidDoor.apply(new Point2D(x1, y1)), new Point2D(x, y));
                    }
                }));
    }

    public static World getInstance() {
        if (instance == null) throw new NullPointerException("Call init on World");
        return instance;
    }

    // Returns true if two rectangles (l1, r1) and (l2, r2) overlap
    public static boolean doOverlap(Rectangle a, Rectangle b) {
        Rectangle2D lol = new Rectangle2D(a.x, a.y, a.width, a.height);
        Rectangle2D lil = new Rectangle2D(b.x, b.y, b.width, b.height);
        return lol.intersects(lil);
    }

    public void step() {
        frames++;
        if (frames % 2 == 0) {
            Castle.getCastles().forEach(Castle::step);
        }
        Squad.getSquads().forEach(Squad::step);
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

} 
