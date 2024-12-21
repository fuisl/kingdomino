package dev.kingdomino.game;

public interface IBoard {

    boolean isTilePlaceable(Tile tile, int x, int y);

    void setTile(Tile tile, int x, int y);

    Tile getTile(int x, int y);

    // Domino
    boolean isDominoPlaceable(Domino domino);

    void setDomino(Domino domino);

    // Point related methods
    int getFinalPoint();

    int getTotalPoint();

    int getAdditionalPoint();

    void addTotalPoint(int totalPoint);

    void addBonusPoint(int bonusPoint);

    void reset();
}