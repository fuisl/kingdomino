package dev.kingdomino.game;

public interface IBoard {

    boolean isTilePlaceable(Tile tile, int x, int y);

    void setTile(Tile tile, int x, int y);

    Tile getTile(int x, int y);

    // Domino
    boolean isDominoPlaceable(Domino domino);

    void setDomino(Domino domino);
}