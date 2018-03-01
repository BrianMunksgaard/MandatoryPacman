package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

/**
 *
 * This class should contain all your game logic
 */

public class Game {
    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have

    private boolean coinsInitialized = false;

    //bitmap of the pacman_right
    private Bitmap pacBitmap;
    private Bitmap pacBitmapLeft;
    private Bitmap pacBitmapRight;
    private Bitmap pacBitmapUp;
    private Bitmap pacBitmapDown;
    //textview reference to points
    private TextView pointsView;
    private int pacx, pacy;
    //the list of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
        pacBitmapLeft = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_left);
        pacBitmapRight = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_right);
        pacBitmapUp = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_up);
        pacBitmapDown = BitmapFactory.decodeResource(context.getResources(), R.drawable.pacman_down);

        pacBitmap = pacBitmapLeft;
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    //TODO initialize goldcoins also here
    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

        coinsInitialized = false;

        gameView.invalidate(); //redraw screen
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public boolean initializeCoins() {
        Log.d("Game - initialize coins","h = "+h+", w = "+w);
        coins.clear();
        Random rnd = new Random();
        for (int i = 0; i < 15; i++) {
            int rndHeight = 10 + rnd.nextInt(h - 9);
            int rndWidth = 10 + rnd.nextInt(w - 9);

            coins.add(new GoldCoin(rndWidth, rndHeight, 10));
        }

        return true;
    }

    public boolean areCoinsInitialized() {
        return coinsInitialized;
    }

    public void movePacman(int pixels, Direction direction)
    {
        int _pacx = pacx;
        int _pacy = pacy;

        switch(direction) {
            case UP:
                if (pacy - pixels  > 0) pacy -= pixels;
                pacBitmap = pacBitmapUp;
                break;
            case DOWN:
                if (pacy + pixels + pacBitmap.getHeight() < h) pacy += pixels;
                pacBitmap = pacBitmapDown;
                break;
            case LEFT:
                if (pacx - pixels  > 0) pacx -= pixels;
                pacBitmap = pacBitmapLeft;
                break;
            case RIGHT:
                if (pacx + pixels + pacBitmap.getWidth() < w)  pacx += pixels;
                pacBitmap = pacBitmapRight;
                break;
        }

        if(pacx != _pacx || pacy != _pacy) {
            doCollisionCheck();
            gameView.invalidate();
        }
    }

    public void doCollisionCheck()
    {

    }

    public int getPacx()
    {
        return pacx;
    }

    public int getPacy()
    {
        return pacy;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public Bitmap getPacBitmap()
    {
        return pacBitmap;
    }


}
