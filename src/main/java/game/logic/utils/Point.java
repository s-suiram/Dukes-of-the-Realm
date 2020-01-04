package game.logic.utils;

import java.io.Serializable;
import java.util.Objects;

/**
 * Defines a point in 2d space.
 */
public class Point implements Serializable {

    public int x;
    public int y;

    /**
     * Instantiates a new Point.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     */
    public <T extends Number> Point(T x, T y) {
        this.x = x.intValue();
        this.y = y.intValue();
    }

    /**
     * Instantiates a new Point.
     */
    public Point() {
        this.x = 0;
        this.y = 0;
    }

    /**
     * Instantiates a new Point.
     *
     * @param p the point to takes coordinates from.
     */
    public Point(Point p) {
        this(p.x, p.y);
    }

    /**
     * set the location of this point.
     *
     * @param <T> a number
     * @param x   the x position
     * @param y   the y position
     * @return this for chaining methods
     */
    public <T extends Number> Point setLocation(T x, T y) {
        this.x = x.intValue();
        this.y = y.intValue();
        return this;
    }

    /**
     * set the location of this point.
     *
     * @param p the point to copy the location from
     * @return this for chaining methods
     */
    public Point setLocation(Point p) {
        setLocation(p.x, p.y);
        return this;
    }

    /**
     * adds one point to this one
     *
     * @param p the point to add
     * @return this for chaining methods
     */
    public Point add(Point p) {
        this.x += p.x;
        this.y += p.y;
        return this;
    }

    /**
     * subs one point to this one
     *
     * @param p the point to sub
     * @return this for chaining methods
     */
    public Point sub(Point p) {
        this.x -= p.x;
        this.y -= p.y;
        return this;
    }


    /**
     * Tanslates the point
     * @param x the x value
     * @param y the y value
     */
    public <N extends Number> void translate(N x, N y) {
        this.x += x.intValue();
        this.y += y.intValue();
    }

    /**
     * compute the euclidean distance between two points.
     * the point calling the method is considered the starting point
     *
     * @param p the point to compute distance to
     * @return a new point containing the x and y distances
     */
    public Point euclideanDist(Point p) {
        return new Point(p.x - x, p.y - y);
    }

    /**
     * takes the negative of both coordinates.
     *
     * @return this for chaining methods
     */
    public Point neg() {
        x = -x;
        y = -y;
        return this;
    }

    public <N extends Number> Point mul(N scal){
        x *= scal.intValue();
        y *= scal.intValue();
        return this;
    }

    /**
     * copy this point into another.
     *
     * @return a copy of this point
     */
    public Point cpy() {
        return new Point(x, y);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Point point = (Point) o;
        return x == point.x && y == point.y;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
