package dev.kingdomino.game;

public class Domino {
    private int id;
    private Tile tileA;
    private Tile tileB;
    private int x;
    private int y;
    private int direction;
    private boolean isPlaced;

    public Domino(int id, Tile tileA, Tile tileB) {
        this.id = id;
        this.tileA = tileA;
        this.tileB = tileB;
        this.x = 0;
        this.y = 0;
        this.direction = 0;  // 0: up, 1: right, 2: down, 3: left
        this.isPlaced = false;
    }

    public int getId() {
        return id;
    }

    public Tile getTileA() {
        return tileA;
    }

    public Tile getTileB() {
        return tileB;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getDirection() {
        return direction;
    }

    public boolean isPlaced() {
        return isPlaced;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }
}
