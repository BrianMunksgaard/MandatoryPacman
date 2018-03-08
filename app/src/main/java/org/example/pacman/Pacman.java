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

    Location location;

    public Pacman(Context context, int x, int y) {
        this.context = context;
        location = new Location(x, y);

        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_left);
        pacBitmapRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_right);
        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_up);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_down);

        pacBitmap = pacBitmapLeft;
    }

    public void updateLocation(int x, int y) {
        location.update(x, y);
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
