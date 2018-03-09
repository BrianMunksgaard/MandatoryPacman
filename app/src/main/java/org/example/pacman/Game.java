package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
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

    // Our hero
    private Pacman pacman;
    // List of enemies
    private ArrayList<Ghost> enemies = new ArrayList<>();
    // List of goldcoins - initially empty
    private ArrayList<GoldCoin> coins = new ArrayList<>();

    // Game grid
    private static final int gridRatio = 100;
    private Node[][] gameGrid;
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
        gameGrid = new Node[gridHeight][gridWidth];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                gameGrid[y][x] = new Node();
            }
        }
    }

    public boolean initializeCoins() {
        if (gameGrid == null) {
            initializePhysicalGrid();
        }
        coins.clear();

        for (int height = 0; height < gridHeight; height++) {
            for (int width = 0; width < gridWidth; width++) {
                int x = width * gridRatio;
                int y = height * gridRatio;
                GoldCoin gc = new GoldCoin(x, y, 10);
                gameGrid[height][width].setCoin(gc);
                coins.add(gc);
            }
        }

        // TODO Place the enemy correct in the grid
        Ghost enemy = new Ghost(this.context, 500, 800);
        enemies.add(enemy);
        gameGrid[8][5].addEmeny(enemy);

        coinsInitialized = true;

        return true;
    }

    public boolean areCoinsInitialized() {
        return coinsInitialized;
    }

    public void movePacman(Direction direction)
    {
        boolean movementAllowed = true;
        if (isDirectionChange(direction)) {
            movementAllowed = canChangeDirection(direction);
        }

        if (movementAllowed) {
            currentDirection = direction;
        }

        int _pacx = pacman.location.pixelX;
        int _pacy = pacman.location.pixelY;

        switch(currentDirection) {
            case UP:
                if (pacman.location.pixelY - pacman.speed  > 0)
                    pacman.updateLocation(pacman.location.pixelX, pacman.location.pixelY - pacman.speed);
                break;
            case DOWN:
                if (pacman.location.pixelY + pacman.speed + pacman.getCurrentBitmap().getHeight() < h)
                    pacman.updateLocation(pacman.location.pixelX, pacman.location.pixelY + pacman.speed);
                break;
            case LEFT:
                if (pacman.location.pixelX - pacman.speed  > 0)
                    pacman.updateLocation(pacman.location.pixelX - pacman.speed, pacman.location.pixelY);
                break;
            case RIGHT:
                if (pacman.location.pixelX + pacman.speed + pacman.getCurrentBitmap().getWidth() < w)
                    pacman.updateLocation(pacman.location.pixelX + pacman.speed, pacman.location.pixelY);
                break;
            case STOP:
                break;
        }

        if(pacman.location.pixelX != _pacx || pacman.location.pixelY != _pacy) {
            doCollisionCheck();
            pacman.setPacBitmap(currentDirection);
            gameView.invalidate();
        }
    }

    public void moveEnemies() {

        /**
         * TODO
         *  If enemy.getNoOfStepsLeft() == 0 then chose a new direction and set the no of steps to be taken in that direction
         *  If enemy.getNoOfStepsLeft() > 0 then move a step i the enemy.getCurrentDireciton() and set enemy.setNoOfStepsLeft(enemy.getNoOfStepsLeft() - 1)
         */
        for (Ghost enemy : getEnemies()) {
            Location loc = enemy.location;
            int startX = loc.pixelX;
            int startY = loc.pixelY;

            int newX = startX + enemy.speed;
            int newY = startY;
            if (newX + enemy.getGhostBitmap().getHeight() < w) {
                enemy.updateLocation(newX, newY);
            }

            if (enemy.location.pixelX != startX || enemy.location.pixelY != startY) {
                doCollisionCheckEnemy(enemy);
                gameView.invalidate();
            }
        }
    }

    public void doCollisionCheck()
    {
        // Calculate the grid coordinates for the pacman
        int gridX = convertToGrid(pacman.location.pixelX);
        int gridY = convertToGrid(pacman.location.pixelY);
        // Find the coin at the same grid location as the pacman
        // If there is a coin here, figure out if we should take it
        if (gameGrid[gridY][gridX].hasCoin()) {
            GoldCoin gc = gameGrid[gridY][gridX].getCoin();
            // Check that the distance between the pacman and the coin is within the limit
            if (pacman.location.distanceTo(gc.getLocation()) <= 30) {
                points += gc.getValue();
                gc.take();
                coins.remove(gc);
                gameGrid[gridY][gridX].removeCoin();
                pointsView.setText(context.getResources().getString(R.string.points)+" "+points);

                if (coins.size() == 0) {
                    gameOver = true;
                }
            }
        }

        for (Ghost enemy : getEnemies()) {
            if (enemy.location.equalsTo(pacman.location)) {
                gameOver = true;
                break;
            }
        }
    }

    public void doCollisionCheckEnemy(Ghost ghost) {
        Location pacmanLocation = pacman.location;
        if (ghost.location.equalsTo(pacmanLocation)) {
            gameOver = true;
        }
    }

    public Location getPacmanLocation() {
        return pacman.location;
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

    public static int getGridRatio() {
        return gridRatio;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    private boolean canChangeDirection(Direction direction) {
        boolean retValue = false;
        int drawX = convertToGrid(pacman.location.pixelX) * gridRatio;
        int drawY = convertToGrid(pacman.location.pixelY) * gridRatio;

//        Log.d("changeDirection", "pacx,pacy:" + pacman.location.pixelX + "," + pacman.location.pixelY + "|drawx,drawy:" + drawX + "," + drawY + "|w,h:" + w + "," + h + "|bitmap:" + pacman.getCurrentBitmap().getWidth() + "," + pacman.getCurrentBitmap().getHeight());
        if ((currentDirection == Direction.UP || currentDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
             //Check for correct distance
            retValue = pacman.location.equalsTo(new Location(drawX, drawY))
                    || pacman.location.pixelY == pacman.speed
                    || pacman.location.pixelY + pacman.speed + pacman.getCurrentBitmap().getHeight() == h
                    || (pacman.location.pixelX == pacman.speed && pacman.location.pixelY == drawY)
                    || (pacman.location.pixelX + pacman.speed + pacman.getCurrentBitmap().getWidth() == w && pacman.location.pixelY == drawY);
        } else if ((currentDirection == Direction.LEFT || currentDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            // Check for correct distance
            retValue = pacman.location.equalsTo(new Location(drawX, drawY))
                    || pacman.location.pixelX == pacman.speed
                    || pacman.location.pixelX + pacman.speed + pacman.getCurrentBitmap().getWidth() == w
                    || (pacman.location.pixelY == pacman.speed && pacman.location.pixelX == drawX)
                    || (pacman.location.pixelY + pacman.speed + pacman.getCurrentBitmap().getHeight() == h && pacman.location.pixelX == drawX);
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

    private int convertToGrid(int pixel) {
        return pixel / gridRatio;
    }
}
