package dev.kingdomino.game;

/**
 * Interface for controlling the domino in the game.
 */
public interface IDominoController {
    /**
     * Rotates the domino.
     *
     * @param clockwise    if true, rotates the domino clockwise; otherwise,
     *                     counterclockwise.
     * @param shouldOffset if true, applies an offset after rotation.
     */
    void rotateDomino(boolean clockwise, boolean shouldOffset);

    /**
     * Gets the position of tile A.
     *
     * @return the position of tile A.
     */
    Position getPosTileA();

    /**
     * Gets the position of tile B.
     *
     * @return the position of tile B.
     */
    Position getPosTileB();

    /**
     * Sets the position of tile A.
     *
     * @param posTileA the new position of tile A.
     */
    void setPosTileA(Position posTileA);

    /**
     * Sets the position of tile B.
     *
     * @param posTileB the new position of tile B.
     */
    void setPosTileB(Position posTileB);

    /**
     * Moves the domino by the specified offset.
     *
     * @param offset the offset to move the domino.
     */
    void moveDomino(Offset offset);
}
