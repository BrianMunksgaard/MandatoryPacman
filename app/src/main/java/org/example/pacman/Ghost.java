package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * This class is used to represent a ghost character
 * in the Pac Man game.
 */
public class Ghost extends Character {

    private Context context;

    private int noOfStepsLeft = 0;

    /**
     * Constructs a new ghost with the correct ghost bitmap.
     * @param context
     * @param x
     * @param y
     */
    public Ghost(Context context, int x, int y) {
        this.context = context;
        location = new Location(x, y);

        characterBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_android_black_36dp);
    }

    public int getNoOfStepsLeft() {
        return noOfStepsLeft;
    }

    public void setNoOfStepsLeft(int newValue) {
        noOfStepsLeft = newValue;
    }

}
