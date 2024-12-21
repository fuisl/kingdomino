package dev.kingdomino.game;

public class TileRotator implements ITileRotator {
    private final Position[] offsets = {
            new Position(1, 0), // right
            new Position(0, 1), // down
            new Position(-1, 0), // left
            new Position(0, -1) // up
    };

    @Override
    public void rotate(Position center, Position tilePos, int rotationIndex) {
        Position newPos = center.add(offsets[rotationIndex]);
        tilePos.set(newPos);
    }
}
