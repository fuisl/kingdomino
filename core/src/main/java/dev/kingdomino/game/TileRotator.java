package dev.kingdomino.game;

public class TileRotator implements ITileRotator {
    private static final Position[] offsets = {
            Offset.RIGHT.get(),
            Offset.DOWN.get(),
            Offset.LEFT.get(),
            Offset.UP.get()
    };

    @Override
    public void rotate(Position center, Position tilePos, int rotationIndex) {
        Position newPos = center.add(offsets[rotationIndex]);
        tilePos.set(newPos);
    }
}
