package dev.kingdomino.game;

public enum Offset {
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    UP(0, -1);

    private Position position;

    Offset(int x, int y) {
        this.position = new Position(x, y);
    }

    public Position get() {
        return position;
    }

    public Position apply(Position pos) {
        return pos.add(position);
    }
}
