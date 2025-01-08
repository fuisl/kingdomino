package dev.kingdomino.game;

public class King {
    private int id;
    private Board board; // add a reference to the board

    public King(int id, Board board) {
        this.id = id;
        this.board = board;
    }

    public int getId() {
        return id;
    }

    public void update() {
    }

    public Board getBoard() {
        return board;
    }

    public King(King other) {
        this.id = other.id;
        this.board = other.board.copy();
    }

    @Override
    public String toString() {
        return "King{" +
                "id=" + id +
                '}';
    }
}
