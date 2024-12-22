package dev.kingdomino.game;

/**
 * Interface for rotating tiles in Kingdomino.
 */
public interface ITileRotator {

    /**
     * Rotates a tile around a center position.
     *
     * @param center        the center position around which the tile will be
     *                      rotated
     * @param tilePos       the position of the tile to be rotated
     * @param rotationIndex the index representing the rotation angle
     */
    void rotate(Position center, Position tilePos, int rotationIndex);
}
