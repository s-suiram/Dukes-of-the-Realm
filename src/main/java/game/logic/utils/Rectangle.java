package game.logic.utils;

import java.io.Serializable;

/**
 * defines a 2d rectangle.
 */
public class Rectangle implements Serializable {

    public int x;
    public int y;
    private int width;
    private int height;

    /**
     * Instantiates a new Rectangle.
     *
     * @param x the x
     * @param y the y
     * @param w the w
     * @param h the h
     */
    public <T extends Number> Rectangle(T x, T y, T w, T h) {
        if( w.intValue() < 0 || h.intValue() < 0)
            throw new IllegalArgumentException("The width or height of a rectangle should not be negative.");

        this.x = x.intValue();
        this.y = y.intValue();
        this.width = w.intValue();
        this.height = h.intValue();
    }

    /**
     * Instantiates a new Rectangle.
     *
     * @param r the rectangle
     */
    public Rectangle(Rectangle r){
        this(r.x, r.y, r.width, r.height);
    }

    /**
     * Instantiates a new Rectangle.
     *
     * @param topLeft  the top left point
     * @param botRight the bottom right point
     */
    public Rectangle(Point topLeft, Point botRight) {
        this(topLeft.x, topLeft.y,  botRight.x - topLeft.x,  botRight.y - topLeft.y);
    }

    /**
     * checks if a rectangle intersects another
     *
     * @param r the rectangle
     * @return true if the two intersects
     */
    public boolean intersects(Rectangle r) {
        if (r == null) return false;
        return r.getMaxX() > x && r.getMaxY() > y && r.x < getMaxX() && r.y < getMaxY();
    }

    /**
     * Gets width.
     *
     * @return the width
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets width.
     *
     * @param width the width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * Gets height.
     *
     * @return the height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Sets height.
     *
     * @param height the height
     */
    public void setHeight(int height) {
        this.height = height;
    }

    /**
     * computes the maxX coordinate
     *
     * @return maxX int
     */
    public int getMaxX(){
        return x + width;
    }

    /**
     * computes the maxY coordinate
     *
     * @return maxY int
     */
    public int getMaxY(){
        return y + width;
    }

    /**
     * Checks if this rectangle contains a point
     *
     * @param cx the x coordinate
     * @param cy the y coordinate
     * @return true if this rectangle contains the point
     */
    public boolean contains(int cx, int cy) {
        if ((width | height) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...

        if (cx < x || cy < y) {
            return false;
        }

        int mx = getMaxX();
        int my = getMaxY();
        //    overflow || intersect
        return ((mx < x || mx > cx) && (my < y || my > cy));
    }

    /**
     * Checks if this rectangle contains a point
     *
     * @param p the point
     * @return true if this rectangle contains the point
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }
}
