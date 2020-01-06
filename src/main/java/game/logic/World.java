package game.logic;

import game.logic.troop.Knight;
import game.logic.troop.Onager;
import game.logic.troop.Pikeman;
import game.logic.troop.Squad;
import game.logic.utils.DistinctList;
import game.logic.utils.Point;
import game.logic.utils.Rectangle;
import javafx.stage.Screen;

import java.io.Serializable;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * The World singleton contains all the model :
 * <ul>
 *     <li>Players</li>
 *     <li>Castle</li>
 *     <li>Troops</li>
 *     <li>Squads</li>
 * </ul>
 * <p>
 * It also take care of all initialization for the model
 */
public class World implements Serializable {
    /**
     * The width of the field
     */
    public static int FIELD_WIDTH = 4000;
    /**
     * The height of the field
     */
    public static int FIELD_HEIGHT = 4000;
    /**
     * The number of frame elapsed
     */
    public static int frames;
    /**
     * A random generator utility
     */
    public static Random generator = new Random();
    /**
     * The instance of World
     */
    private static World instance;
    public DistinctList<Castle> castles;
    public DistinctList<Squad> squads;
    /**
     * Store all the player in the game
     */
    private DistinctList<Player> players;


    /**
     * Build a new World
     */
    private World() {
        players = new DistinctList<>();
        castles = new DistinctList<>();
        squads = new DistinctList<>();
        frames = 0;
    }

    private World(World world) {
        this();
        this.players.addAll(world.getPlayers());
        this.squads.addAll(world.squads);
        this.castles.addAll(world.castles);
    }

    /**
     * Initialize the world and generate random castle configuration
     *
     * @param fightingNames the name of the fighting dukes
     * @param neutralNames  the name of the neutral dukes
     * @param castlePerDuke the number of castle for one duke
     */
    public static void init(List<String> fightingNames, List<String> neutralNames, int castlePerDuke) {
        instance = new World();
        randomGeneration(fightingNames, neutralNames, castlePerDuke);
    }


    public static void init(World world) {
        instance = new World(world);
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

    /**
     * Return a random integer that belongs to [min; max[
     *
     * @param min the lower bound (inclusive)
     * @param max the max bound (exclusive)
     * @return a random integer
     */
    public static int rand(int min, int max) {
        return generator.nextInt(max - min) + min;
    }

    private static void randomGeneration(List<String> fightingNames, List<String> neutralNames, int nbCastlePerDuke) {
        double padding = 0.2; //0.2 -> 20% smaller bounds
        int fieldForCastle = Castle.SIZE; //Space on the field for one castle

        int nbFighter = fightingNames.size();
        fightingNames.forEach(n -> getInstance().addFightingDukes(n));

        getInstance().getPlayer(fightingNames.get(0)).ifPresent(p -> p.isBot = false);

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

        List<Rectangle> tiles = new ArrayList<>(nbCastle);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                tiles.add(new Rectangle(
                        (x * widthPerTile) + (widthPerTile * padding),
                        (y * heightPerTile) + (heightPerTile * padding),
                        widthPerTile * (1 - padding * 2),
                        heightPerTile * (1 - padding * 2)
                ));
            }
        }

        Function<Point, Boolean> isRight = p -> p.x == width - 1;
        Function<Point, Boolean> isLeft = p -> p.x == 0;
        Function<Point, Boolean> isUp = p -> p.y == 0;
        Function<Point, Boolean> isDown = p -> p.y == height - 1;

        Function<Point, Cardinal> getValidDoor = p -> {
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

        Queue<Integer> randQueue = IntStream.range(0, tiles.size())
                .boxed()
                .collect(Collectors.toCollection(LinkedList::new));

        Collections.shuffle((List<?>) randQueue);

        Stream.of(fightingNames, neutralNames)
                .flatMap(Collection::stream)
                .forEach(name -> getInstance().getPlayer(name).ifPresent(p -> {
                    for (int i = 0; i < nbCastlePerDuke; i++) {
                        int rand = randQueue.remove();
                        Rectangle picked = tiles.get(rand);
                        int x1 = rand / width;
                        int y1 = rand % width;

                        int x = rand(picked.x, (picked.getMaxX() - Castle.SIZE));
                        int y = rand(picked.y, (picked.getMaxY() - Castle.SIZE));

                        p.addCastle(getValidDoor.apply(new Point(x1, y1)), new Point(x, y));
                    }
                }));

        neutralNames.forEach(e -> World.getInstance().getPlayer(e).get().getCastles().forEach(c -> {
            for (int i = 0; i < 25; i++)
                c.getTroops().add(new Pikeman());
            for (int i = 0; i < 10; i++)
                c.getTroops().add(new Knight());
            for (int i = 0; i < 5; i++)
                c.getTroops().add(new Onager());

            c.setLevel(10);
        }));

        fightingNames.forEach(e -> World.getInstance().getPlayer(e).get().getCastles().forEach(c -> {
            for (int i = 0; i < 5; i++)
                c.getTroops().add(new Pikeman());
            for (int i = 0; i < 2; i++)
                c.getTroops().add(new Knight());
            for (int i = 0; i < 1; i++)
                c.getTroops().add(new Onager());
        }));

    }

    /**
     * Returns the instance of World
     *
     * @return the instance of World
     * @throws NullPointerException if the init() method is not called before getInstance()
     */
    public static World getInstance() {
        if (instance == null) throw new NullPointerException("Call init on World");
        return instance;
    }

    public static <N extends Number & Comparable<Integer>> int compare(N number) {
        return Integer.compare(number.compareTo(0), 0);

    }

    /**
     * Make the world advance
     */
    public void step() {
        frames++;
        squads.removeIf(Squad::isDead);
        castles.forEach(Castle::removeDeads);
        if (frames % 1 == 0) {
            castles.forEach(Castle::step);
        }
        squads.forEach(Squad::step);

        players.subList(1, players.size()).forEach(Player::act);

        if (frames == 60) frames = 1;
    }

    /**
     * Returns the player list
     *
     * @return the player list
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Get a player by name
     *
     * @param name the name of the player
     * @return a player named <code>name</code>
     */
    public Optional<Player> getPlayer(String name) {
        return players.stream().filter(player -> player.getName().equals(name)).findFirst();
    }

    /**
     * Add a fighting duke
     *
     * @param name the name of the fighting duke
     */
    public void addFightingDukes(String name) {
        players.add(new FightingDukes(name));
    }

    public Player getPlayer() {
        return players.get(0);
    }

    /**
     * Add a neutral duke
     *
     * @param name the name of the neutral duke
     */
    public void addNeutralDukes(String name) {
        players.add(new NeutralDukes(name));
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        players.forEach(p -> s.append(p.toString()));
        return s.toString();
    }

    public boolean hasPlayerWon() {
        return players.stream().filter(Player::isBot).filter(p -> p instanceof FightingDukes).map(p -> p.getCastles().size()).reduce(0, Integer::sum) == 0;
    }

    public boolean hasEnemyWon() {
        return players.stream().filter(p -> !p.isBot()).map(p -> p.getCastles().size()).reduce(0, Integer::sum) == 0;
    }

}
