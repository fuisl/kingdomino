package dev.kingdomino.game;

public class Player {
    private String name;
    private int point;

    public Player(String name) {
        this.name = name;
        this.point = 0;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return point;
    }
}
