package org.example.pacman;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Random;

/**
 * The game class holds all the game logic such as
 * the current score, the number of enemies, whether
 * or not the games has finished etc.
 */
public class Game {

    /*
     * Context is a reference to the main activity.
     */
    private Context context;

    /*
     * The current number of points (the score).
     */
    private int points = 0; //how points do we have

    /*
     * Whether or not the game has finished.
     */
    boolean gameOver = false;

    /*
     * Whether or not the game has been initialized.
     */
    private boolean isGameInitialized = false;

    /*
     * Our hero
     */
    private Pacman pacman;

    /*
     * Pacman enemies.
     */
    private ArrayList<Ghost> enemies = new ArrayList<>();

    /*
     * Gold coins.
     */
    private ArrayList<GoldCoin> coins = new ArrayList<>();

    /*
     * The grid ratio, that is, the ratio between the
     * physical game view and the virtual grid.
     */
    private static final int gridRatio = 100;

    /*
     * All positions/squares in the virtual
     * grid.
     */
    private Node[][] gameGrid;

    /*
     * The height of the virtual grid.
     */
    private int gridHeight = 0;

    /*
     * The width of the virtual grid.
     */
    private int gridWidth = 0;

    /*
     * Reference to the view used to display the
     * current score.
     */
    private TextView pointsView;

    /*
     * Reference to the physical game view, that is,
     * the actual android game view.
     */
    private GameView gameView;

    /*
     * The physical pixel height and width of the screen.
     */
    private int h,w;

    /**
     * Initialize the Game class.
     */
    public Game(Context context, TextView view)
    {
        this.context = context;
        this.pointsView = view;
    }

    /**
     * Prepare to start new game.
     */
    public void newGame()
    {
        // Game has not yet been initialized.
        isGameInitialized = false;

        // Redraw physical screen.
        // This will also result in a call
        // to initializeGame.
        gameView.invalidate();
    }

    /**
     * Associate the game controller and the
     * physical game view.
     */
    public void setGameView(GameView view)
    {
        this.gameView = view;
    }

    /**
     * Store size of the physical game view.
     */
    public void setSize(int h, int w)
    {
        this.h = h;
        this.w = w;
    }

    public boolean initializeGame() {
        if (gameGrid == null) {
            initializeVirtualGrid();
        }

        // Reset score.
        points = 0;
        pointsView.setText(context.getResources().getString(R.string.points) + " " + points);
        gameOver = false;

        coins.clear();
        enemies.clear();

        // Place the 2 walls
        // Wall 1:
        gameGrid[2][2].placeWall(new Wall(2 * gridRatio, 2 * gridRatio));
        gameGrid[3][2].placeWall(new Wall(2 * gridRatio, 3 * gridRatio));
        gameGrid[4][2].placeWall(new Wall(2 * gridRatio, 4 * gridRatio));

        // Wall 2:
        gameGrid[7][7].placeWall(new Wall(7 * gridRatio, 7 * gridRatio));
        gameGrid[8][7].placeWall(new Wall(7 * gridRatio, 8 * gridRatio));
        gameGrid[9][7].placeWall(new Wall(7 * gridRatio, 9 * gridRatio));
        gameGrid[10][7].placeWall(new Wall(7 * gridRatio, 10 * gridRatio));

        for (int height = 0; height < gridHeight; height++) {
            for (int width = 0; width < gridWidth; width++) {
                if (!gameGrid[height][width].isObstructed()) {
                    int x = width * gridRatio;
                    int y = height * gridRatio;
                    GoldCoin gc = new GoldCoin(x, y, 10);
                    gameGrid[height][width].setCoin(gc);
                    coins.add(gc);
                }
            }
        }

        // Place enemy in the grid
        {
            Ghost enemy = new Ghost(this.context, 500, 800);
            enemy.setSpeed(10);
            enemy.setDirection(Direction.RIGHT);
            enemy.setNoOfStepsLeft(50);
            enemies.add(enemy);
            gameGrid[8][5].addEnemy(enemy);
        }

        // Place another enemy in the grid.
        {
            Ghost enemy = new Ghost(this.context, 500, 800);
            enemy.setSpeed(10);
            enemy.setDirection(Direction.DOWN);
            enemy.setNoOfStepsLeft(50);
            enemies.add(enemy);
            gameGrid[8][5].addEnemy(enemy);
        }

        // Initialise the Pacman.
        pacman = new Pacman(context, 100, 400);
        pacman.setSpeed(10);
        gameGrid[4][1].addPlayer(pacman);

        isGameInitialized = true;

        return true;
    }

    /*
     * Initialize the virtual grid.
     */
    private void initializeVirtualGrid() {
        gridHeight = h / gridRatio;
        gridWidth = w / gridRatio;
        gameGrid = new Node[gridHeight][gridWidth];
        for (int x = 0; x < gridWidth; x++) {
            for (int y = 0; y < gridHeight; y++) {
                gameGrid[y][x] = new Node();
            }
        }
    }

    /**
     * Whether or not the game has been initialized.
     */
    public boolean isGameInitialized() {
        return isGameInitialized;
    }

    /**
     * Move characters.
     * The playerDirection only involves the player,
     * not the ghosts.
     */
    public void performMovement(Direction playerDirection) {
        movePacman(playerDirection);
        moveEnemies();
    }

    /*
     * Move the Pacman.
     */
    private void movePacman(Direction direction)
    {
        boolean movementAllowed = true;
        Direction currentDirection = pacman.getDirection();
        if (isDirectionChange(pacman, direction)) {
            movementAllowed = canChangeDirection(pacman, direction);
        }

        if (movementAllowed) {
            currentDirection = direction;
        }

        if (checkForWall(pacman, direction)) {
            currentDirection = Direction.STOP;
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

        // Only if the Pacman has moved?
        if((pacman.getLocation().pixelX != _pacx || pacman.getLocation().pixelY != _pacy)) {
            doCollisionCheck(pacman);
            pacman.setDirection(currentDirection);
            gameGrid[convertToGrid(_pacy)][convertToGrid(_pacx)].removePlayer();
            gameGrid[convertToGrid(pacman.getLocation().pixelY)][convertToGrid(pacman.getLocation().pixelX)].addPlayer(pacman);
            gameView.invalidate();
        }
    }


    /*
     * Move all enemies.
     */
    private void moveEnemies() {

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
                Direction newDirection = selectRandomDirection(enemy.getDirection());
                if (checkForWall(enemy, newDirection)) {
                    newDirection = selectRandomDirection(newDirection);
                }

                // Make sure that the enemy can change direction
                if (canChangeDirection(enemy, newDirection)) {
                    enemy.setDirection(newDirection);
                    enemy.setNoOfStepsLeft(50); //rnd.nextInt(5) + 5);
                } else {
                    enemy.setNoOfStepsLeft(1);
                }
            } else {
                if (checkForWall(enemy, enemy.getDirection())) {
                    enemy.setDirection(selectRandomDirection(enemy.getDirection()));
                }
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

    /**
     * A collision detection method used by both Ghost and Pacman
     * @param character
     */
    public void doCollisionCheck(Character character)
    {
        Location charLocation = character.getLocation();
        // Calculate the grid coordinates for the pacman
        int gridX = convertToGrid(charLocation.pixelX);
        int gridY = convertToGrid(charLocation.pixelY);
        if (character instanceof Ghost) {
            Location playerLocation = pacman.getLocation();
            // If an enemy "runs into" the player it is game over!
            if (charLocation.equalsTo(playerLocation)) {
                gameOver = true;
            }
        } else {
            // Find the coin at the same grid location as the pacman
            // If there is a coin here, figure out if we should take it
            Node node = gameGrid[gridY][gridX];
            if (node.hasCoin()) {
                GoldCoin gc = node.getCoin();
                // Check that the distance between the character and the coin is within the limit
                if (charLocation.distanceTo(gc.getLocation()) <= 30) {
                    points += gc.getValue();
                    gc.take();
                    coins.remove(gc);
                    node.removeCoin();
                    pointsView.setText(context.getResources().getString(R.string.points) + " " + points);

                    if (coins.size() == 0) {
                        gameOver = true;
                    }
                }
            }

            for (Ghost enemy : getEnemies()) {
                if (enemy.getLocation().distanceTo(charLocation) <= 20) {
                    gameOver = true;
                    break;
                }
            }
        }
    }

    /**
     * Implementation of checking if the next grid location that the character is moving towards
     * is a wall and therefor is not a valid location to move to.
     *
     * INFO: The implementation could be better and use the other collision detection method.
     * @param character
     * @param directionCheck
     * @return
     */
    public boolean checkForWall(Character character, Direction directionCheck) {
        Location charLocation = character.getLocation();
        int gridX = convertToGrid(charLocation.pixelX);
        int gridY = convertToGrid(charLocation.pixelY);
        switch(directionCheck) {
            case UP:
                gridY -= 1;
                break;
            case DOWN:
                gridY += 1;
                break;
            case LEFT:
                gridX -= 1;
                break;
            case RIGHT:
                gridX += 1;
                break;
        }
        if (gridX < 0 || gridX >= gridWidth || gridY < 0 || gridY >= gridHeight) {
            return false;
        }
        double dist = charLocation.distanceTo(new Location(gridX * gridRatio, gridY * gridRatio));
        return gameGrid[gridY][gridX].isObstructed() && dist == 100;
    }

    /**
     * Return the physical location of the Pacman.
     */
    public Location getPacmanLocation() {
        return pacman.getLocation();
    }

    /**
     * Return a list with all the coins.
     */
    public ArrayList<GoldCoin> getCoins()
    {
        return coins;
    }

    /**
     * Returns a list with all enemies.
     */
    public ArrayList<Ghost> getEnemies() {
        return enemies;
    }

    /**
     * Returns the virtual game grid.
     */
    public Node[][] getGameGrid() {
        return gameGrid;
    }

    /**
     *
     * @return
     */
    public Bitmap getPacBitmap()
    {
        return Bitmap.createScaledBitmap(pacman.getCharacterBitmap(), gridRatio, gridRatio, true);
    }

    /**
     *
     * @return
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /*
     * Whether or not the character can change its direction.
     */
    private boolean canChangeDirection(Character character, Direction direction) {
        boolean retValue = false;
        Location charLocation = character.getLocation();
        int charSpeed = character.getSpeed();
        Direction charDirection = character.getDirection();

        int drawX = convertToGrid(charLocation.pixelX) * gridRatio;
        int drawY = convertToGrid(charLocation.pixelY) * gridRatio;

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


    /*
     * Whether or not a change in direction has been requested.
     */
    private boolean isDirectionChange(Character character, Direction direction) {
        Direction charDirection = character.getDirection();

        // A change from up/down to left/right?
        if ((charDirection == Direction.UP || charDirection == Direction.DOWN)
                && (direction == Direction.LEFT || direction == Direction.RIGHT)) {
            return true;
        }

        // A change from left/right to up/down?
        else if ((charDirection == Direction.LEFT || charDirection == Direction.RIGHT)
                && (direction == Direction.UP || direction == Direction.DOWN)) {
            return true;
        }
        return false;
    }

    /*
     * Convert from physical pixel number to virtual grid number.
     */
    private int convertToGrid(int pixel) {
        return pixel / gridRatio;
    }

    /*
     * Returns a random direction. Usefulf for enemies.
     */
    private Direction selectRandomDirection(Direction currentDirection) {
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

        if (newDirection == currentDirection) {
            newDirection = selectRandomDirection(currentDirection);
        }

        return newDirection;
    }
}
