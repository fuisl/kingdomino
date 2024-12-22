package dev.kingdomino.game;

/**
 * Interface for validating tile placement in Kingdomino.
 */
public interface ITileValidator {

    /**
     * Checks if a tile can be placed at the specified coordinates.
     * This includes general checks for bounds, free space, and land type.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile can be placed, false otherwise
     */
    boolean isTilePlaceable(Tile tile, int x, int y);

    /**
     * Checks if the specified coordinates are within the bounds of the board (9x9).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    boolean isTileWithinBound(int x, int y);

    /**
     * Checks if the specified coordinates are within the land area of the board
     * (5x5 or 7x7).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within the land area, false otherwise
     */
    boolean isTileWithinLand(int x, int y);

    /**
     * Checks if the specified coordinates are free and within the land area.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are free and within the land area, false
     *         otherwise
     */
    boolean isTileFree(int x, int y);

    /**
     * Checks if a tile can be connected at the specified coordinates.
     * A tile is connectable if it is adjacent to the same terrain type or a castle.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile is connectable, false otherwise
     */
    boolean isTileConnectable(Tile tile, int x, int y);

    /**
     * Checks if a tile is adjacent to the same terrain type at the specified
     * coordinates.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile is adjacent to the same terrain type, false
     *         otherwise
     */
    boolean isTileAdjacentSame(Tile tile, int x, int y);

    /**
     * Checks if a tile is adjacent to a castle at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the tile is adjacent to a castle, false otherwise
     */
    boolean isTileAdjacentCastle(int x, int y);

}