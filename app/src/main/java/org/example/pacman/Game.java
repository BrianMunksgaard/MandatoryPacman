package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

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

    // Our hero
    private Pacman pacman;
    // List of enemies
    private ArrayList<Ghost> enemies = new ArrayList<>();
    // List of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();

    // Game grid
    private final int gridRatio = 100;
    private GoldCoin[][] gameGrid;
    private int gridHeight = 0;
    private int gridWidth = 0;

    //textview reference to points
    private TextView pointsView;
    //a reference to the gameview
    private GameView gameView;
    private int h,w; //height and width of screen

    private Direction currentDirection = Direction.STOP;

    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
    }

    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    public void newGame()
    {
        pacman = new Pacman(context, 50, 400);
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameOver = false;

        currentDirection = Direction.STOP;

        coinsInitialized = false;

        Ghost enemy = new Ghost(this.context, 500, 800);
        enemies.add(enemy);

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
        boolean movementAllowed = true;
        if (isDirectionChange(direction)) {
            movementAllowed = canChangeDirection(direction);
        }

        if (movementAllowed) {
            currentDirection = direction;
        }

        int _pacx = pacman.location.x;
        int _pacy = pacman.location.y;

        switch(currentDirection) {
            case UP:
                if (pacman.location.y - pixels  > 0)
                    pacman.updateLocation(pacman.location.x, pacman.location.y - pixels);
                break;
            case DOWN:
                if (pacman.location.y + pixels + pacman.getCurrentBitmap().getHeight() < h)
                    pacman.updateLocation(pacman.location.x, pacman.location.y + pixels);
                break;
            case LEFT:
                if (pacman.location.x - pixels  > 0)
                    pacman.updateLocation(pacman.location.x - pixels, pacman.location.y);
                break;
            case RIGHT:
                if (pacman.location.x + pixels + pacman.getCurrentBitmap().getWidth() < w)
                    pacman.updateLocation(pacman.location.x + pixels, pacman.location.y);
                break;
            case STOP:
                break;
        }

        if(pacman.location.x != _pacx || pacman.location.y != _pacy) {
            doCollisionCheck();
            pacman.setPacBitmap(currentDirection);
            gameView.invalidate();
        }
    }

    public void moveEnemy(Ghost enemy, int pixel) {

        Location loc = enemy.location;
        int startX = loc.x;
        int startY = loc.y;

        if (loc.x != startX || loc.y != startY) {
            doCollisionCheck();
            enemy.updateLocation(0, 0);
            gameView.invalidate();
        }

    }

    public void doCollisionCheck()
    {
        // Calculate the grid coordinates for the pacman
        int gridX = pacman.location.x / gridRatio;
        int gridY = pacman.location.y / gridRatio;
        // Find the coin at the same grid location as the pacman
        GoldCoin gc = gameGrid[gridY][gridX];
        // If there is a coin here, figure out if we should take it
        if (gc != null) {
            int drawX = gc.getLocation().x * gridRatio;
            int drawY = gc.getLocation().y * gridRatio;
            // Check that the distance between the pacman and the coin is within the limit
            if (pacman.location.distanceTo(new Location(drawX, drawY)) <= 30) {
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

        // TODO Do collision check for enemies as well
    }

    public Location getPacmanLocation() {
        return pacman.location;
    }

    public int getPoints()
    {
        return points;
    }

    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    public ArrayList<Ghost> getEnemies() {
        return enemies;
    }

    public Bitmap getPacBitmap()
    {
        return Bitmap.createScaledBitmap(pacman.getCurrentBitmap(), gridRatio, gridRatio, true);
    }

    public int getGridRatio() {
        return gridRatio;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean canChangeDirection(Direction direction) {
        boolean retValue = false;
        int gridX = pacman.location.x / gridRatio;
        int gridY = pacman.location.y / gridRatio;
        int drawX = gridX * gridRatio;
        int drawY = gridY * gridRatio;

        if ((currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
             //Check for correct distance
            retValue = pacman.location.equalsTo(new Location(drawX, drawY));
        } else if ((currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            // Check for correct distance
            retValue = pacman.location.equalsTo(new Location(drawX, drawY));
        }

        return retValue;
    }

    public boolean isDirectionChange(Direction direction) {
        if ((currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
            return true;
        } else if ((currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            return true;
        }
        return false;
    }
}
