package org.example.pacman;

import java.util.ArrayList;

/**
 * Created by Jens Christian Rasch on 09-03-2018.
 */

public class Node {

    private GoldCoin coin;
    private ArrayList<Ghost> ghosts;
    private Wall wall;

    public Node() {
        ghosts = new ArrayList<>();
    }

    public void setCoin(GoldCoin coin) {
        this.coin = coin;
    }

    public GoldCoin getCoin() {
        return coin;
    }

    public void removeCoin() {
        coin = null;
    }

    public boolean hasCoin() {
        return coin != null;
    }

    public void addEnemy(Ghost ghost) {
        ghosts.add(ghost);
    }

    public void removeEnemy(Ghost ghost) {
        ghosts.remove(ghost);
    }

    public boolean hasEnemies() {
        return ghosts.size() > 0;
    }

    public ArrayList<Ghost> getEnemies() {
        return ghosts;
    }

    public Wall getWall() {
        return wall;
    }

    public void placeWall(Wall wall) {
        this.wall = wall;
    }

    public void removeWall() {
        wall = null;
    }

    public boolean isObstructed() {
        return (wall != null);
    }
}
