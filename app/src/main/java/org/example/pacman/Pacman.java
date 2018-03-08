package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Pacman {
    private Context context;

    //bitmap of the pacman_right
    private Bitmap pacBitmap;
    private Bitmap pacBitmapLeft;
    private Bitmap pacBitmapRight;
    private Bitmap pacBitmapUp;
    private Bitmap pacBitmapDown;

    private int pacx, pacy;

    public Pacman(Context context, int x, int y) {
        this.context = context;
        pacx = x;
        pacy = y;

        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_left);
        pacBitmapRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_right);
        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_up);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_down);

        pacBitmap = pacBitmapLeft;
    }

    public int getX() {
        return pacx;
    }

    public void setX(int x) {
        pacx = x;
    }

    public int getY() {
        return pacy;
    }

    public void setY(int y) {
        pacy = y;
    }

    public void setPacBitmap(Direction direction) {
        switch (direction) {
            case UP:
                pacBitmap = pacBitmapUp;
                break;
            case DOWN:
                pacBitmap = pacBitmapDown;
                break;
            case LEFT:
                pacBitmap = pacBitmapLeft;
                break;
            default:
                pacBitmap = pacBitmapRight;
                break;
        }
    }

    public Bitmap getCurrentBitmap() {
        return pacBitmap;
    }
}
