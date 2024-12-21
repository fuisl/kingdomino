package dev.kingdomino.game;

public interface IDominoController {
    void rotateDomino(boolean clockwise, boolean shouldOffset);
    boolean isPlaced();
    void setPlaced(boolean placed);
    Position getPosTileA();
    Position getPosTileB();
}
