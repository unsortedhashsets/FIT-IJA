package maps;

import java.lang.Math;

/**
 * Class that represents position on the map.
 * 
 * @author Mikhail Abramov (xabram00)
 * @author Serhii Salatskyi (xsalat00)
 *
 */
public class Coordinate {
    private final int X;
    private final int Y;

    /**
     * Allocates a Coordinate object with given parameters.
     * @param x X coordinate value.
     * @param y Y coordinate value.
     */
    public Coordinate(int x, int y) {
        this.X = x;
        this.Y = y;
    }

    /**
     * Create Coordinate's instance according to its parameters.
     * @param x X coordinate value.
     * @param y Y coordinate value.
     * @return Coordinate's instance or null, if x or y is less than 0.
    */
    public static Coordinate create(int x, int y) {
        return (x >= 0 && y >= 0) ? new Coordinate(x, y) : null;
    }

    /**
     * Returns X coordinate value.
     * @return X coordinate value.
    */
    public int getX() {
        return this.X;
    }

    /**
     * Returns Y coordinate value.
     * @return Y coordinate value.
    */
    public int getY() {
        return this.Y;
    }

    /**
     * Returns the difference between this and given coordinates by X value.
     * @param c a given coordinate.
     * @return X coordinates' difference.
    */
    public int diffX(Coordinate c) {
        return this.X - c.getX();
    }

    /**
     * Returns the difference between this and given coordinates by Y value.
     * @param c a given coordinate.
     * @return Y coordinates' difference.
    */
    public int diffY(Coordinate c) {
        return this.Y - c.getY();
    }

    /**
     * Returns the length between this and given coordinates.
     * Length is calculated by Euclidean distance.
     * @param c a given coordinate.
     * @return Length.
    */
    public double length(Coordinate c) {
        return Math.sqrt(Math.pow(this.diffX(c), 2) + Math.pow(this.diffY(c), 2));
    }

    /**
     * Compares two coordinates for equality. 
     * @param obj The object to compare with.
     * @return true, if they're equal, otherwise false.
    */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (this.getClass() != obj.getClass())
            return false;

        Coordinate that = (Coordinate) obj;
        return that.X == this.X && that.Y == this.Y;
    }

    /**
     * Returns a hash code value for this coordinate. 
     * @return A hash code value for this coordinate.
    */
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 31 * hash + this.X;
        hash = 31 * hash + this.Y;

        return hash;
    }

    /**
     * Converts this Coordinate object to a String. 
     * @return A string representation of this coordinate.
    */
    @Override
    public String toString(){
        return "(" + this.X + ", " + this.Y + ")";
    }
}
