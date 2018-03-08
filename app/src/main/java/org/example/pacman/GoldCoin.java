package org.example.pacman;

/**
 * This class should contain information about a single GoldCoin.
 * such as x and y coordinates (int) and whether or not the goldcoin
 * has been taken (boolean)
 */

public class GoldCoin {

    private Location location;
    private int value;
    private boolean hasBeenTaken = false;

    public GoldCoin(int x, int y, int value) {
        location = new Location(x, y);
        this.value = value;
    }

    public Location getLocation() {
        return location;
    }

    public int getValue() {
        return value;
    }

    public boolean isTaken() {
        return hasBeenTaken;
    }

    public void take() {
        hasBeenTaken = true;
    }


}
