package org.example.pacman;

/**
 * The location class is used to represent locations,
 * that is, x and y values in a coordinate system.
 */
public class Location {

    int pixelX;
    int pixelY;

    /**
     * Construct new location object.
     */
    public Location(int x, int y) {
        pixelX = x;
        pixelY = y;
    }

    /**
     * Updates the current coordinate.
     */
    public void update(int x, int y) {
        pixelX = x;
        pixelY = y;
    }

    /**
     * Checks whether or not this location is equal
     * to the specified location.
     */
    public boolean equalsTo(Location comparer) {
        return (distanceTo(comparer) == 0);
    }

    /**
     * Calculates the distance from this location
     * to the specified location.
     */
    public double distanceTo(Location target) {
        double distx = Math.pow((target.pixelX - pixelX), 2);
        double disty = Math.pow((target.pixelY - pixelY),2);
        return Math.sqrt(distx + disty);
    }

}
