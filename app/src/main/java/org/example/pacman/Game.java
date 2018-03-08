package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;

/**
 *
 * This class should contain all your game logic
 */

public class Game {

    //context is a reference to the activity
    private Context context;
    private int points = 0; //how points do we have

    boolean gameOver = false;

    private boolean coinsInitialized = false;

    // Game grid
    private final int gridRatio = 100;
    private GoldCoin[][] gameGrid;
    private int gridHeight = 0;
    private int gridWidth = 0;

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

    public void newGame()
    {
        pacx = 50;
        pacy = 400; //just some starting coordinates
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameOver = false;

        coinsInitialized = false;

        gameView.invalidate(); //redraw screen
    }

    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public void initializePhysicalGrid() {
        gridHeight = h / gridRatio;
        gridWidth = w / gridRatio;
//        Log.d("initialiseGrid", "gridHeigh: " + gridHeight + ", gridWidth: " + gridWidth);
        gameGrid = new GoldCoin[gridHeight][gridWidth];
    }

    public boolean initializeCoins() {
//        Log.d("Game - initialize coins","h = "+h+", w = "+w);
        if (gameGrid == null) {
            initializePhysicalGrid();
        }
        coins.clear();

        for (int h = 0; h < gridHeight; h++) {
            for (int w = 0; w < gridWidth; w++) {
                GoldCoin gc = new GoldCoin(w, h, 10);
                gameGrid[h][w] = gc;
                coins.add(gc);
            }
        }

        coinsInitialized = true;

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
        int gridX = pacx / gridRatio;
        int gridY = pacy / gridRatio;
        GoldCoin gc = gameGrid[gridY][gridX];
        Log.d("collisionCheck", "grid & coin:" + gridX + "," + gridY + "->" + gc);
        if (gc != null) {
            int drawX = gc.getX() * gridRatio;
            int drawY = gc.getY() * gridRatio;
            double distance = distance(pacx, pacy, drawX, drawY);
            Log.d("collisionCheck", "pacx,pacy:" + pacx + "," + pacy + " | drawX,drawY:" + drawX + "," + drawY + " | distance:" + distance);
            if (distance <= 20) {
                points += gc.getValue();
                gc.take();
                coins.remove(gc);
                gameGrid[gridY][gridX] = null;
                pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

                if (coins.size() == 0) {
                    gameOver = true;
                }
            }
        }
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
        return Bitmap.createScaledBitmap(pacBitmap, gridRatio, gridRatio, true);
    }

    public int getGridRatio() {
        return gridRatio;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private double distance(int x1, int y1, int x2, int y2) {
        // d = sqrt((x2 - x1)^2 + (y2 - y1)^2)
        double x = Math.pow((x2 - x1), 2);
        double y = Math.pow((y2 - y1),2);
        return Math.sqrt(x + y);
    }
}
