package dev.kingdomino.game;

public interface IDominoController {
    void rotateDomino(boolean clockwise, boolean shouldOffset);

    Position getPosTileA();

    Position getPosTileB();

    void setPosTileA(Position posTileA);

    void setPosTileB(Position posTileB);

    void moveDomino(Position offset);
}
