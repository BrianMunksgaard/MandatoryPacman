package org.example.pacman;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Location {

    int pixelX;
    int pixelY;

    public Location(int x, int y) {
        pixelX = x;
        pixelY = y;
    }

    public void update(int x, int y) {
        pixelX = x;
        pixelY = y;
    }

    public boolean equalsTo(Location comparer) {
        return (distanceTo(comparer) == 0);
    }

    public double distanceTo(Location target) {
        double distx = Math.pow((target.pixelX - pixelX), 2);
        double disty = Math.pow((target.pixelY - pixelY),2);
        return Math.sqrt(distx + disty);
    }

}
