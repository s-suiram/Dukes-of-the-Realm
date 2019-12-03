package game.logic.troop;

public abstract class Troop {
    final int cost;
    final int speed;
    final int damage;
    final int hp;
    final String name;

    public Troop(int cost, int time, int speed, int damage, int hp, String name) {
        this.cost = cost;
        this.speed = speed;
        this.damage = damage;
        this.hp = hp;
        this.name = name;
    }

    public int getCost() {
        return cost;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDamage() {
        return damage;
    }

    public int getHp() {
        return hp;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Troop) {
            return cost == ((Troop) obj).cost
                    && speed == ((Troop) obj).speed
                    && damage == ((Troop) obj).damage
                    && hp == ((Troop) obj).hp
                    && name.equals(((Troop) obj).name);
        }
        return false;
    }
}
