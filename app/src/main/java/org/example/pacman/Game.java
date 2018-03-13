package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
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
        //reset the points
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points)+" "+points);
        gameOver = false;

        // Initialise the player/Pacman
        pacman = new Pacman(context, 50, 400);
        pacman.setSpeed(10);

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
        enemy.setSpeed(10);
        enemy.setDirection(Direction.RIGHT);
        enemies.add(enemy);
        gameGrid[8][5].addEnemy(enemy);

        coinsInitialized = true;

        return true;
    }

    public boolean areCoinsInitialized() {
        return coinsInitialized;
    }

    public void performMovement(Direction playerDirection) {
        movePacman(playerDirection);
        moveEnemies();
    }

    public void movePacman(Direction direction)
    {
        boolean movementAllowed = true;
        Direction currentDirection = pacman.getDirection();
        if (isDirectionChange(pacman, direction)) {
            movementAllowed = canChangeDirection(pacman, direction);
        }

        if (movementAllowed) {
            currentDirection = direction;
        }

        int _pacx = pacman.getLocation().pixelX;
        int _pacy = pacman.getLocation().pixelY;

        switch(currentDirection) {
            case UP:
                if (pacman.getLocation().pixelY - pacman.getSpeed()  > 0)
                    pacman.setLocation(pacman.getLocation().pixelX, pacman.getLocation().pixelY - pacman.getSpeed());
                break;
            case DOWN:
                if (pacman.getLocation().pixelY + pacman.getSpeed() + pacman.getCharacterBitmap().getHeight() < h)
                    pacman.setLocation(pacman.getLocation().pixelX, pacman.getLocation().pixelY + pacman.getSpeed());
                break;
            case LEFT:
                if (pacman.getLocation().pixelX - pacman.getSpeed()  > 0)
                    pacman.setLocation(pacman.getLocation().pixelX - pacman.getSpeed(), pacman.getLocation().pixelY);
                break;
            case RIGHT:
                if (pacman.getLocation().pixelX + pacman.getSpeed() + pacman.getCharacterBitmap().getWidth() < w)
                    pacman.setLocation(pacman.getLocation().pixelX + pacman.getSpeed(), pacman.getLocation().pixelY);
                break;
            case STOP:
                break;
        }

        if(pacman.getLocation().pixelX != _pacx || pacman.getLocation().pixelY != _pacy) {
            doCollisionCheck(pacman);
            pacman.setDirection(currentDirection);
            gameView.invalidate();
        }
    }

    public void moveEnemies() {

        /**
         * TODO
         *  Create a method that can test if it is possible from the current location to change
         *  direction. If it is, then select a random direction for the next move.
         */
        for (Ghost enemy : getEnemies()) {
            Location loc = enemy.getLocation();
            int startX = loc.pixelX;
            int startY = loc.pixelY;

            if (enemy.getNoOfStepsLeft() == 0) {
                // Change direction
                Random rnd = new Random();
                Direction newDirection = Direction.STOP;
                switch (rnd.nextInt(4) + 1) {
                    case 1: //UP
                        newDirection = Direction.UP;
                        break;
                    case 2: //DOWN
                        newDirection = Direction.DOWN;
                        break;
                    case 3: //LEFT
                        newDirection = Direction.LEFT;
                        break;
                    case 4: //RIGHT
                        newDirection = Direction.RIGHT;
                        break;
                }

                // Make sure that the enemy can change direction
                if (canChangeDirection(enemy, newDirection)) {
                    enemy.setDirection(newDirection);
                    enemy.setNoOfStepsLeft(50); //rnd.nextInt(5) + 5);
                } else {
                    enemy.setNoOfStepsLeft(1);
                }
            } else {
                // Move along the current direction, unless you are at the end of the road
                enemy.setNoOfStepsLeft(enemy.getNoOfStepsLeft() - 1);
            }

            // Set the target location and move there
            int newX;
            int newY;
            switch (enemy.getDirection()) {
                case UP:
                    newX = startX;
                    newY = startY - enemy.getSpeed();
                    if (newY <= 0) {
                        newY = startY + enemy.getSpeed();
                        enemy.setDirection(Direction.DOWN);
                    }
                    enemy.setLocation(newX, newY);
                    break;
                case DOWN:
                    newX = startX;
                    newY = startY + enemy.getSpeed();
                    if (newY + enemy.getCharacterBitmap().getHeight() >= h) {
                        newY = startY - enemy.getSpeed();
                        enemy.setDirection(Direction.UP);
                    }
                    enemy.setLocation(newX, newY);
                    break;
                case LEFT:
                    newX = startX - enemy.getSpeed();
                    newY = startY;
                    if (newX <= 0) {
                        newX = startX + enemy.getSpeed();
                        enemy.setDirection(Direction.RIGHT);
                    }
                    enemy.setLocation(newX, newY);
                    break;
                case RIGHT:
                    newX = startX + enemy.getSpeed();
                    newY = startY;
                    if (newX + enemy.getCharacterBitmap().getWidth() >= w) {
                        newX = startX - enemy.getSpeed();
                        enemy.setDirection(Direction.LEFT);
                    }
                    enemy.setLocation(newX, newY);
                    break;
            }

            if (enemy.getLocation().pixelX != startX || enemy.getLocation().pixelY != startY) {
                doCollisionCheck(enemy);
                gameView.invalidate();
            }
        }
    }

    public void doCollisionCheck(Character character)
    {
        Location charLocation = character.getLocation();
        if (character instanceof Ghost) {
            Location playerLocation = pacman.getLocation();
            // If an enemy "runs into" the player it is game over!
            if (charLocation.equalsTo(playerLocation)) {
                gameOver = true;
            }
        } else {
            // Calculate the grid coordinates for the pacman
            int gridX = convertToGrid(charLocation.pixelX);
            int gridY = convertToGrid(charLocation.pixelY);
            // Find the coin at the same grid location as the pacman
            // If there is a coin here, figure out if we should take it
            if (gameGrid[gridY][gridX].hasCoin()) {
                GoldCoin gc = gameGrid[gridY][gridX].getCoin();
                // Check that the distance between the character and the coin is within the limit
                if (charLocation.distanceTo(gc.getLocation()) <= 30) {
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
                if (enemy.getLocation().equalsTo(charLocation)) {
                    gameOver = true;
                    break;
                }
            }
        }
    }

    public Location getPacmanLocation() {
        return pacman.getLocation();
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
        return Bitmap.createScaledBitmap(pacman.getCharacterBitmap(), gridRatio, gridRatio, true);
    }

    public boolean isGameOver() {
        return gameOver;
    }

    /**
     *
     * @param character
     * @param direction
     * @return
     */
    private boolean canChangeDirection(Character character, Direction direction) {
        boolean retValue = false;
        Location charLocation = character.getLocation();
        int charSpeed = character.getSpeed();
        Direction charDirection = character.getDirection();

        int drawX = convertToGrid(charLocation.pixelX) * gridRatio;
        int drawY = convertToGrid(charLocation.pixelY) * gridRatio;

//        Log.d("changeDirection", "pacx,pacy:" + pacman.getLocation().pixelX + "," + pacman.getLocation().pixelY + "|drawx,drawy:" + drawX + "," + drawY + "|w,h:" + w + "," + h + "|bitmap:" + pacman.getCurrentBitmap().getWidth() + "," + pacman.getCurrentBitmap().getHeight());
        if ((charDirection == Direction.UP || charDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
             //Check for correct distance
            retValue = charLocation.equalsTo(new Location(drawX, drawY))
                    || charLocation.pixelY == charSpeed
                    || charLocation.pixelY + charSpeed + character.getCharacterBitmap().getHeight() == h
                    || (charLocation.pixelX == charSpeed && charLocation.pixelY == drawY)
                    || (charLocation.pixelX + charSpeed + character.getCharacterBitmap().getWidth() == w && charLocation.pixelY == drawY);
        } else if ((charDirection == Direction.LEFT || charDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            // Check for correct distance
            retValue = charLocation.equalsTo(new Location(drawX, drawY))
                    || charLocation.pixelX == charSpeed
                    || charLocation.pixelX + charSpeed + character.getCharacterBitmap().getWidth() == w
                    || (charLocation.pixelY == charSpeed && charLocation.pixelX == drawX)
                    || (charLocation.pixelY + charSpeed + character.getCharacterBitmap().getHeight() == h && charLocation.pixelX == drawX);
        }

        return retValue;
    }

    public boolean isDirectionChange(Character character, Direction direction) {
        Direction charDirection = character.getDirection();
        if ((charDirection == Direction.UP || charDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
            return true;
        } else if ((charDirection == Direction.LEFT || charDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            return true;
        }
        return false;
    }

    private int convertToGrid(int pixel) {
        return pixel / gridRatio;
    }
}
