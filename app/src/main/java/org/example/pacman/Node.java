package org.example.pacman;

import java.util.ArrayList;

/**
 * Created by Jens Christian Rasch on 09-03-2018.
 */

public class Node {

    private GoldCoin coin;
    private ArrayList<Ghost> ghosts;
    private boolean hasWall = false;

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

    public void addEmeny(Ghost ghost) {
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

    public void placeWall() {
        hasWall = true;
    }

    public void removeWall() {
        hasWall = false;
    }

    public boolean isObstructed() {
        return hasWall;
    }
}
