package game.logic.utils;

import java.io.Serializable;

public class Rectangle implements Serializable {
    public int x;
    public int y;
    public int width;
    public int height;
    public int maxX;
    public int maxY;

    public <T extends Number> Rectangle(T x, T y, T w, T h) {
        this.x = x.intValue();
        this.y = y.intValue();
        this.width = w.intValue();
        this.height = h.intValue();
        this.maxX = x.intValue() + w.intValue();
        this.maxY = y.intValue() + h.intValue();
    }

    public boolean intersects(Rectangle r) {
        if (r == null) return false;
        return r.maxX > x && r.maxY > y && r.x < maxX && r.y < maxY;
    }

    public boolean contains(int cx, int cy) {
        int tw = this.width;
        int th = this.height;
        if ((tw | th) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int tx = this.x;
        int ty = this.y;
        if (cx < tx || cy < ty) {
            return false;
        }
        tw += tx;
        th += ty;
        //    overflow || intersect
        return ((tw < tx || tw > cx) &&
                (th < ty || th > cy));
    }
}
