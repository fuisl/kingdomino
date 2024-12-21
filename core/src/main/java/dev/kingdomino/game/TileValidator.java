package dev.kingdomino.game;

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

    public TileValidator(Tile[][] land, int size) {
        this.land = land; // hold a reference to the board
        this.size = size;
    }

    public TileValidator(Tile[][] land) {
        this(land, 5); // default size is 5x5
    }

    public void updateSpanningTiles(int x, int y) {
        minX = Math.min(minX, x);
        maxX = Math.max(maxX, x);
        minY = Math.min(minY, y);
        maxY = Math.max(maxY, y);
    }

    @Override
    public boolean isTilePlaceable(Tile tile, int x, int y) {
        return isTileFree(x, y) && isTileAdjacent(x, y)
                && (isTileConnectable(tile, x, y) || isTileAdjacentCastle(x, y))
                && isTileWithinBound(x, y);
    }

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

    @Override
    public boolean isTileWithinLand(int x, int y) {
        return x >= 0 && x < (size * 2 - 1) && y >= 0 && y < (size * 2 - 1); // 5x5 or 7x7
    }

    @Override
    public boolean isTileFree(int x, int y) {
        if (isTileWithinLand(x, y)) {
            return land[x][y] == null;
        }
        return false;
    }

    @Override
    public boolean isTileAdjacent(int x, int y) {
        return (x > 0 && land[x - 1][y] != null) ||
                (x < land.length - 1 && land[x + 1][y] != null) ||
                (y > 0 && land[x][y - 1] != null) ||
                (y < land[0].length - 1 && land[x][y + 1] != null);
    }

    @Override
    public boolean isTileConnectable(Tile tile, int x, int y) {
        // check if adjacent tiles have the same terrain
        return (x > 0 && land[x - 1][y] != null && land[x - 1][y].getTerrain() == tile.getTerrain()) ||
                (x < land.length - 1 && land[x + 1][y] != null && land[x + 1][y].getTerrain() == tile.getTerrain()) ||
                (y > 0 && land[x][y - 1] != null && land[x][y - 1].getTerrain() == tile.getTerrain()) ||
                (y < land[0].length - 1 && land[x][y + 1] != null && land[x][y + 1].getTerrain() == tile.getTerrain());
    }

    @Override
    public boolean isTileAdjacentCastle(int x, int y) {
        // castle always in the middle (4, 4); magic numbers here.
        return (x == 4 && y == 3) || (x == 4 && y == 5) || (x == 3 && y == 4) || (x == 5 && y == 4);
    }

}