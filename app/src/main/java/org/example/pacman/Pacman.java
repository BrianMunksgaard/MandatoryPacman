package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by Jens Christian Rasch on 08-03-2018.
 */

public class Pacman extends Character {
    private Context context;

    //bitmap of the pacman_right
    private Bitmap pacBitmapLeft;
    private Bitmap pacBitmapRight;
    private Bitmap pacBitmapUp;
    private Bitmap pacBitmapDown;

    public Pacman(Context context, int x, int y) {
        this.context = context;
        location = new Location(x, y);

        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_left);
        pacBitmapRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_right);
        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_up);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_down);

        characterBitmap = pacBitmapLeft;
    }

    @Override
    public void setDirection(Direction direction) {
        super.setDirection(direction);

        switch (direction) {
            case UP:
                characterBitmap = pacBitmapUp;
                break;
            case DOWN:
                characterBitmap = pacBitmapDown;
                break;
            case LEFT:
                characterBitmap = pacBitmapLeft;
                break;
            case RIGHT:
                characterBitmap = pacBitmapRight;
                break;
            default:
                break;
        }
    }
}
