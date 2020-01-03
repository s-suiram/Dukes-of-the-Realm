package game.logic.utils;

import java.io.Serializable;

public class Point implements Serializable {
    public int x;
    public int y;

    public <T extends Number> Point(T x, T y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public Point() {
        this.x = 0;
        this.y = 0;
    }

    public Point(Point p) {
        this(p.x, p.y);
    }

    public <T extends Number> void setLocation(T x, T y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }
}
