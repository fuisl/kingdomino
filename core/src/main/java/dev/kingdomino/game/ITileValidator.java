package dev.kingdomino.game;

public interface ITileValidator {

    boolean isTilePlaceable(Tile tile, int x, int y);

    boolean isTileWithinBound(int x, int y);

    boolean isTileWithinLand(int x, int y);

    boolean isTileFree(int x, int y);

    boolean isTileAdjacent(int x, int y);

    boolean isTileConnectable(Tile tile, int x, int y);

    boolean isTileAdjacentCastle(int x, int y);

}