package dev.kingdomino.game;

/**
 * Rotates tiles around a center position.
 * 
 * @see Position
 * @see Direction
 * @see Tile
 * 
 * @author @fuisl
 * @version 1.0
 */
public class TileRotator {
    private static final Position[] directions = {
    Direction.RIGHT.get(),
    Direction.DOWN.get(),
    Direction.LEFT.get(),
    Direction.UP.get()
    };

    /**
     * Rotates a tile around the center position based on the rotation index.
     *
     * @param center        the center position (posTileA)
     * @param tilePos       the position of the tile to be rotated (posTileB)
     * @param rotationIndex the index of the rotation (0-3)
     */
    public void rotate(Position center, Position tilePos, int rotationIndex, boolean shouldOffset) {
        Position newPos = center.add(directions[rotationIndex]);
        tilePos.set(newPos);
    }
}
