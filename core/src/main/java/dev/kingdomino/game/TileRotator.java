package dev.kingdomino.game;

/**
 * Rotates tiles around a center position.
 */
public class TileRotator implements ITileRotator {
    private static final Position[] offsets = {
            Offset.RIGHT.get(),
            Offset.DOWN.get(),
            Offset.LEFT.get(),
            Offset.UP.get()
    };

    /**
     * Rotates a tile around the center position based on the rotation index.
     *
     * @param center        the center position
     * @param tilePos       the position of the tile to be rotated
     * @param rotationIndex the index of the rotation (0-3)
     */
    @Override
    public void rotate(Position center, Position tilePos, int rotationIndex) {
        Position newPos = center.add(offsets[rotationIndex]);
        tilePos.set(newPos);
    }
}
