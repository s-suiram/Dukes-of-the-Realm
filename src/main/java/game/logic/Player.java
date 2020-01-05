package game.logic;

import game.logic.troop.Troop;
import game.logic.troop.TroopType;
import game.logic.utils.DistinctList;
import game.logic.utils.Point;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * The Player class represent a player that owns castles and make decisions
 */
public abstract class Player implements Serializable {

    /**
     * The list of castle the player owns
     */
    private List<Castle> castles;

    /**
     * The name of the player
     */
    private String name;


    protected boolean isBot;

    /**
     * Build a player with the specified name
     *
     * @param name the name of the player
     */
    public Player(String name, boolean isBot) {
        castles = new ArrayList<>();
        this.name = name;
        this.isBot = isBot;
    }

    public Player(String name){
        this(name,true);
    }

    public void act(){
        int cs = castles.size();
        Castle c = castles.get(World.rand(0,cs));
        Castle t = Castle.getCastles().get(World.rand(0,Castle.getCastles().size()));
        List<Troop> troops = c.getTroops();

        if( c.getFlorin() >= c.levelUpPrice() || World.rand(0,Math.max(1,10-c.getLevel())) == 0) {

            if(c.getTimeToLevelUp() == -1 && c.getFlorin() >= c.levelUpPrice()){
                c.startLevelUp();
            } else if( c.getTroops().size() < 10) {
                c.produce(TroopType.ONAGER);
                c.produce(TroopType.KNIGHT);
                c.produce(TroopType.PIKE_MAN);
            } else if (c != t && c.getSquads().size() == 0 ) {
                DistinctList<Troop> toSend = IntStream.range(0, World.rand(1,10))
                        .boxed()
                        .map(i -> troops.get(World.rand(0,cs)))
                        .collect(DistinctList.collector());
                toSend.sort(Comparator.comparingInt(o -> o.speed));
                c.createSquad(toSend,t);
            }
        }


    }

    /**
     * Returns the name of the player
     *
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Returns the castles of the player
     *
     * @return the castles of the player
     */
    public List<Castle> getCastles() {
        return castles;
    }

    /**
     * Add a castle for the player
     *
     * @param door the direction of the door of the castle
     * @param pos  the position on the field of the castle
     */
    public void addCastle(Cardinal door, Point pos) {
        castles.add(new Castle(this, door, pos));
    }

    @Override
    public String toString() {
        return name;
    }
}
