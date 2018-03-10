package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Ghost extends Character {

    private Context context;

    private int noOfStepsLeft = 0;

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
