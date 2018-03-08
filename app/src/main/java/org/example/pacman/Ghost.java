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

    private int x;
    private int y;

    public Ghost(Context context, int x, int y) {
        this.context = context;
        this.x = x;
        this.y = y;

        ghostBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_android_black_36dp);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Bitmap getGhostBitmap() {
        return ghostBitmap;
    }

}
