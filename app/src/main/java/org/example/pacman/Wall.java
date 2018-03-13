package org.example.pacman;

/**
 * Created by jcr on 13-03-2018.
 */

public class Wall {

    private Location location;

    public Wall(int x, int y) {
        location = new Location(x, y);
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }
}
