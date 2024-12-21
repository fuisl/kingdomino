package dev.kingdomino.game;

public class Position {
    private int x;
    private int y;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Position() {
        this(-1, -1);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public void set(Position other) {
        x = other.x;
        y = other.y;
    }

    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
