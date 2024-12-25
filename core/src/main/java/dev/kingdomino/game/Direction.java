package dev.kingdomino.game;

/**
 * Enum representing the possible offsets for tile rotation.
 */
public enum Direction {
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0),
    UP(0, -1);

    private Position position;

    /**
     * Constructs an Offset with the specified x and y values.
     *
     * @param x the x offset
     * @param y the y offset
     */
    Direction(int x, int y) {
        this.position = new Position(x, y);
    }

    /**
     * Returns the position corresponding to this offset.
     *
     * @return the position
     */
    public Position get() {
        return position;
    }

    /**
     * Applies this offset to the given position and returns the result.
     *
     * @param pos the position to apply the offset to
     * @return the new position resulting from the offset
     */
    public Position apply(Position pos) {
        return pos.add(position);
    }
}
