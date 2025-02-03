package dev.kingdomino.game;

/**
 * Represents a king in the game.
 */
public class King {
    private final int id;
    private final Board board; // add a reference to the board

    /**
     * Constructs a new King with the specified id and board.
     *
     * @param id the id of the king
     * @param board the board associated with the king
     */
    public King(int id, Board board) {
        this.id = id;
        this.board = board;
    }

    /**
     * Returns the id of the king.
     *
     * @return the id of the king
     */
    public int getId() {
        return id;
    }

    /**
     * Updates the state of the king.
     */
    public void update() {
    }

    /**
     * Returns the board associated with the king.
     *
     * @return the board associated with the king
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Constructs a new King by copying another King.
     *
     * @param other the King to copy
     */
    public King(King other) {
        this.id = other.id;
        this.board = other.board.copy();
    }

    /**
     * Returns a string representation of the king.
     *
     * @return a string representation of the king
     */
    @Override
    public String toString() {
        return "King{" +
                "id=" + id +
                '}';
    }
}
