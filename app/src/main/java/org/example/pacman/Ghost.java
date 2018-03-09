package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Ghost {

    private Context context;

    private Bitmap ghostBitmap;

    Location location;

    private Direction currentDireciton;
    private int noOfStepsLeft = 0;

    int speed = 10;

    public Ghost(Context context, int x, int y) {
        this.context = context;
        location = new Location(x, y);

        ghostBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_android_black_36dp);
    }

    public void updateLocation(int x, int y) {
        location.update(x, y);
    }

    public void setDirection(Direction direction) {
        currentDireciton = direction;
    }

    public int getNoOfStepsLeft() {
        return noOfStepsLeft;
    }

    public void setNoOfStepsLeft(int newValue) {
        noOfStepsLeft = newValue;
    }

    public Bitmap getGhostBitmap() {
        return ghostBitmap;
    }

}
