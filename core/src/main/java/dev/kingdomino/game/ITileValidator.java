package dev.kingdomino.game;

public interface ITileValidator {

    // is placeable in general (bound, free, land)
    boolean isTilePlaceable(Tile tile, int x, int y);

    // bound of the board 9x9
    boolean isTileWithinBound(int x, int y);

    // land of the board 5x5 (or 7x7)
    boolean isTileWithinLand(int x, int y);

    // also check for within land
    boolean isTileFree(int x, int y);

    // (!update) connectable = adjacent same terrain type or adjacent castle
    boolean isTileConnectable(Tile tile, int x, int y);

    // adjacent same terrain type
    boolean isTileAdjacentSame(Tile tile, int x, int y);

    // adjacent castle
    boolean isTileAdjacentCastle(int x, int y);

}