package org.example.pacman;

/**
 * Created by jcr on 13-03-2018.
 */

public class Wall {

    private Location location;

    public Wall(int x, int y) {
        location = new Location(x, y);
    }

    public Location getLocation() {
        return location;
    }
}
