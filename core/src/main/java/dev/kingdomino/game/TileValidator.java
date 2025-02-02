package dev.kingdomino.game;

import static java.lang.Math.abs;

/**
 * Validates the placement of tiles on the game board.
 * 
 * @see Tile
 * @see Board
 * @see TerrainType
 * 
 * @author @fuisl
 * @version 1.0
 * 
 */
public class TileValidator {
    private int CENTER = 4;
    private Tile[][] land;
    private int size; // size of the land (5x5 or 7x7)
    private int minX, maxX, minY, maxY;
    {
        minX = CENTER;
        maxX = CENTER;
        minY = CENTER;
        maxY = CENTER;
    }

    /**
     * Constructs a TileValidator with the specified land and size.
     *
     * @param land the game board
     * @param size the size of the board (5x5 or 7x7)
     */
    public TileValidator(Tile[][] land, int size) {
        this.land = land; // hold a reference to the board
        this.size = size;
        this.CENTER = size - 1;
    }

    /**
     * Constructs a TileValidator with the specified land and a default size of 5x5.
     *
     * @param land the game board
     */
    public TileValidator(Tile[][] land) {
        this(land, 5); // default size is 5x5
    }

    /**
     * Updates the spanning tiles based on the given coordinates.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void updateSpanningTiles(int x, int y) {
        if (x < minX) {
            minX = x;
        } else if (x > maxX) {
            maxX = x;
        }

        if (y < minY) {
            minY = y;
        } else if (y > maxY) {
            maxY = y;
        }
    }

    /**
     * Checks if a tile can be placed at the specified coordinates.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile can be placed, false otherwise
     */
    public boolean isTilePlaceable(Tile tile, int x, int y) {
        return isTileFree(x, y) && isTileWithinBound(x, y);
    }

    /**
     * Checks if the specified coordinates are within the bounds of the board.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within bounds, false otherwise
     */
    public boolean isTileWithinBound(int x, int y) {
        // lock one axis if already spanned max
        if (x < minX || x > maxX) {
            if (abs(x - minX) + 1 > size || abs(x - maxX) + 1 > size) {
                return false;
            }
        }

        if (y < minY || y > maxY) {
            if (abs(y - minY) + 1 > size || abs(y - maxY) + 1 > size) {
                return false;
            }
        }

        return true;
    }

    /**
     * Checks if the specified coordinates are within the land area.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are within the land area, false otherwise
     */
    public boolean isTileWithinLand(int x, int y) {
        return x >= 0 && x < (size * 2 - 1) && y >= 0 && y < (size * 2 - 1); // 5x5 or 7x7
    }

    /**
     * Checks if the specified coordinates are free (not occupied by another tile).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the coordinates are free, false otherwise
     */
    public boolean isTileFree(int x, int y) {
        if (isTileWithinLand(x, y)) {
            return land[y][x] == null;
        }
        return false;
    }

    /**
     * Checks if a tile can be connected to another tile at the specified
     * coordinates.
     *
     * @param tile the tile to be connected
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile can be connected, false otherwise
     */
    public boolean isTileConnectable(Tile tile, int x, int y) {
        return isTileAdjacentSame(tile, x, y) || isTileAdjacentCastle(x, y);
    }

    /**
     * Checks if a tile is adjacent to another tile with the same terrain type.
     *
     * @param tile the tile to be checked
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile is adjacent to another tile with the same terrain
     *         type, false otherwise
     */
    public boolean isTileAdjacentSame(Tile tile, int x, int y) {
        // check if adjacent tiles have the same terrain
        return (x > 0 && land[y][x - 1] != null && land[y][x - 1].getTerrain() == tile.getTerrain()) ||
                (x < land[0].length - 1 && land[y][x + 1] != null && land[y][x + 1].getTerrain() == tile.getTerrain())
                ||
                (y > 0 && land[y - 1][x] != null && land[y - 1][x].getTerrain() == tile.getTerrain()) ||
                (y < land.length - 1 && land[y + 1][x] != null && land[y + 1][x].getTerrain() == tile.getTerrain());
    }

    /**
     * Checks if a tile is adjacent to the castle (wildcard).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the tile is adjacent to the castle, false otherwise
     */
    public boolean isTileAdjacentCastle(int x, int y) {
        // Check 4 tiles adj to the center -> castle
        return (x == CENTER && y == CENTER - 1) || (x == CENTER && y == CENTER + 1) ||
                (x == CENTER - 1 && y == CENTER) || (x == CENTER + 1 && y == CENTER);
    }

}