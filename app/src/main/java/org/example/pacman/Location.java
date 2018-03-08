package org.example.pacman;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Location {

    int x;
    int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void update(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean equalsTo(Location comparer) {
        return (distanceTo(comparer) == 0);
    }

    public double distanceTo(Location target) {
        double distx = Math.pow((target.x - x), 2);
        double disty = Math.pow((target.y - y),2);
        return Math.sqrt(distx + disty);
    }

}
