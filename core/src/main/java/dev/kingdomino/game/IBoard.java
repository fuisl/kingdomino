package dev.kingdomino.game;

/**
 * Interface representing a game board in Kingdomino.
 */
public interface IBoard {

    /**
     * Checks if a tile can be placed at the specified coordinates.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile can be placed, false otherwise
     */
    boolean isTilePlaceable(Tile tile, int x, int y);

    /**
     * Places a tile at the specified coordinates.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     */
    void setTile(Tile tile, int x, int y);

    /**
     * Retrieves the tile at the specified coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the tile at the specified coordinates
     */
    Tile getTile(int x, int y);

    /**
     * Checks if a domino can be placed on the board.
     *
     * @param domino the domino to be placed
     * @return true if the domino can be placed, false otherwise
     */
    boolean isDominoPlaceable(Domino domino);

    /**
     * Places a domino on the board.
     *
     * @param domino the domino to be placed
     * @return 0 if the domino was successfully placed. Otherwise, return a non-zero
     *         value.
     */
    int setDomino(Domino domino);
}