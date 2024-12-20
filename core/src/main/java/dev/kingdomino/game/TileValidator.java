package dev.kingdomino.game;

public class TileValidator {
    private Tile[][] land;

    public TileValidator(Tile[][] land) {
        this.land = land; // hold a reference to the board
    }

    public boolean isTilePlaceable(Tile tile, int x, int y) {
        return isTileFree(x, y) && isTileAdjacent(x, y)
                && (isTileConnectable(tile, x, y) || isTileAdjacentCastle(x, y));
    }

    public boolean isTileWithinBound(int x, int y) {
        // TODO: boundary check for size 5x5 and 7x7
        return x >= 0 && x < land.length && y >= 0 && y < land[0].length;
    }

    public boolean isTileFree(int x, int y) {
        return land[x][y] == null;
    }

    public boolean isTileAdjacent(int x, int y) {
        return (x > 0 && land[x - 1][y] != null) ||
                (x < land.length - 1 && land[x + 1][y] != null) ||
                (y > 0 && land[x][y - 1] != null) ||
                (y < land[0].length - 1 && land[x][y + 1] != null);
    }

    public boolean isTileConnectable(Tile tile, int x, int y) {
        // check if adjacent tiles have the same terrain
        return (x > 0 && land[x - 1][y] != null && land[x - 1][y].getTerrain() == tile.getTerrain()) ||
                (x < land.length - 1 && land[x + 1][y] != null && land[x + 1][y].getTerrain() == tile.getTerrain()) ||
                (y > 0 && land[x][y - 1] != null && land[x][y - 1].getTerrain() == tile.getTerrain()) ||
                (y < land[0].length - 1 && land[x][y + 1] != null && land[x][y + 1].getTerrain() == tile.getTerrain());
    }

    public boolean isTileAdjacentCastle(int x, int y) {
        // castle always in the middle (4, 4); magic numbers here.
        return (x == 4 && y == 3) || (x == 4 && y == 5) || (x == 3 && y == 4) || (x == 5 && y == 4);
    }
}