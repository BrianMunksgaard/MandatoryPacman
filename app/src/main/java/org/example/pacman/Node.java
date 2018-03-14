package org.example.pacman;

/**
 * The node class represents a single square
 * on the pacman virtual grid.
 */
public class Node {

    private GoldCoin coin;
    private Wall wall;

    public Node() {
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

    public Wall getWall() {
        return wall;
    }

    public void placeWall(Wall wall) {
        this.wall = wall;
    }

    public boolean isObstructed() {
        return (wall != null);
    }
}
