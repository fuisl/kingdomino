package dev.kingdomino.game;

/**
 * Validates the placement of tiles on the game board.
 */
public class TileValidator implements ITileValidator {
    private final int CENTER = 4;
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
        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);
        minY = Math.min(minY, y);
        maxY = Math.max(maxY, y);
    }

    /**
     * Checks if a tile can be placed at the specified coordinates.
     *
     * @param tile the tile to be placed
     * @param x    the x-coordinate
     * @param y    the y-coordinate
     * @return true if the tile can be placed, false otherwise
     */
    @Override
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
    @Override
    public boolean isTileWithinBound(int x, int y) {
        // lock one axis if already spanned max
        if (!((maxX - minX + 1) < size)) {
            if (x < minX || x > maxX) {
                return false;
            }
        }
        if (!((maxY - minY + 1) < size)) {
            if (y < minY || y > maxY) {
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
    @Override
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
    @Override
    public boolean isTileFree(int x, int y) {
        if (isTileWithinLand(x, y)) {
            return land[x][y] == null;
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
    @Override
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
    @Override
    public boolean isTileAdjacentSame(Tile tile, int x, int y) {
        // check if adjacent tiles have the same terrain
        return (x > 0 && land[x - 1][y] != null && land[x - 1][y].getTerrain() == tile.getTerrain()) ||
                (x < land.length - 1 && land[x + 1][y] != null && land[x + 1][y].getTerrain() == tile.getTerrain()) ||
                (y > 0 && land[x][y - 1] != null && land[x][y - 1].getTerrain() == tile.getTerrain()) ||
                (y < land[0].length - 1 && land[x][y + 1] != null && land[x][y + 1].getTerrain() == tile.getTerrain());
    }

    /**
     * Checks if a tile is adjacent to the castle (wildcard).
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the tile is adjacent to the castle, false otherwise
     */
    @Override
    public boolean isTileAdjacentCastle(int x, int y) {
        // castle always in the middle (4, 4); magic numbers here.
        return (x == 4 && y == 3) || (x == 4 && y == 5) || (x == 3 && y == 4) || (x == 5 && y == 4);
    }

}