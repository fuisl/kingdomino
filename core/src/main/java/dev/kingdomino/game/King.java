package dev.kingdomino.game;

public class King {
    private int id;
    private Board board; // add a reference to the board

    public King(int id) {
        this.id = id;
        this.board = new Board();
    }

    public int getId() {
        return id;
    }

    public void update() {
        
    }
}
