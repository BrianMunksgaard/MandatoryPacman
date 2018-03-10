package org.example.pacman;

import android.graphics.Bitmap;

/**
 * Created by jcr on 10-03-2018.
 */

public abstract class Character {

    protected Direction direction;
    protected Location location;
    protected int speed;

    protected Bitmap characterBitmap;

    public void setDirection(Direction newDirection) {
        direction = newDirection;
    }

    public Direction getDirection() {
        return direction;
    }

    public Location getLocation() {
        return location;
    }

    public void updateLocation(int x, int y) {
        location.update(x, y);
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getSpeed() {
        return speed;
    }

    public Bitmap getCharacterBitmap() {
        return characterBitmap;
    }

}
