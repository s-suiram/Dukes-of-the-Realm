package game.logic.utils;

import java.io.Serializable;

/**
 * defines a 2d rectangle.
 */
public class Rectangle implements Serializable {

    private static  IllegalArgumentException subZero = new IllegalArgumentException(
            "The width or height of a rectangle should not be negative."
    );

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
            throw subZero;

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
     * Instantiates a new Rectangle at (0,0)
     *
     * @param w the width
     * @param h the height
     */
    public <N extends Number> Rectangle( N w, N h){
        this(0,0,w,h);
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
    public <N extends Number> void setWidth(N width) {
        if(width.intValue() < 0)
            throw subZero;
        this.width = width.intValue();
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
    public <N extends Number> void setHeight(N height) {
        if(height.intValue() < 0)
            throw subZero;
        this.height = height.intValue();
    }

    public int getMidX() {
        return (getMaxX() + x)/2;
    }

    public int getMidY() {
        return (getMaxY() + y)/2;
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
     * Tanslates the rectangle
     * @param x the x value
     * @param y the y value
     */
    public <N extends Number> void translate(N x, N y) {
        this.x += x.intValue();
        this.y += y.intValue();
    }

    /**
     * Checks if this rectangle contains a point
     *
     * @param px the x coordinate
     * @param py the y coordinate
     * @return true if this rectangle contains the point
     */
    public <N extends Number> boolean contains(N px, N py) {
        int cx = px.intValue();
        int cy = py.intValue();
        return x < cx && cx < getMaxX() && y < cy && cy < getMaxY();
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


    /**
     * check if the parameter rectangle is contained in this one
     * @param r the rectangle
     * @return  true if this rectangle contains r
     */
    public boolean contains(Rectangle r){
        return x < r.x && r.getMaxX() < getMaxX() && y < r.y && r.getMaxY() < getMaxY();
    }

    @Override
    public String toString() {
        return "Rectangle{" +
                "x=" + x +
                ", y=" + y +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
