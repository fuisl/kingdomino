package dev.kingdomino.game;

/**
 * Represents a position in a 2D coordinate system.
 */
public class Position {
    private int x;
    private int y;

    /**
     * Constructs a Position with the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a Position with default coordinates (-1, -1).
     */
    public Position() {
        this(-1, -1);
    }

    /**
     * Returns the x-coordinate of this position.
     *
     * @return the x-coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Returns the y-coordinate of this position.
     *
     * @return the y-coordinate
     */
    public int y() {
        return y;
    }

    /**
     * Sets this position to the coordinates of another position.
     *
     * @param other the position to copy coordinates from
     */
    public void set(Position other) {
        x = other.x;
        y = other.y;
    }

    /**
     * Returns a new position that is the sum of this position and another position.
     *
     * @param other the position to add
     * @return a new position with the summed coordinates
     */
    public Position add(Position other) {
        return new Position(x + other.x, y + other.y);
    }

    /**
     * Returns a string representation of this position.
     *
     * @return a string in the format "(x, y)"
     */
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }
}
