package dev.kingdomino.game;

public class DominoController implements IDominoController {
    private int rotationIndex;
    private Position posTileA; // tileA is always at the center
    private Position posTileB; // tileB is always at the right of tileA -> relative position
    private boolean isPlaced;
    private ITileRotator tileRotator;

    public DominoController(ITileRotator tileRotator) {
        this.rotationIndex = 0;
        this.isPlaced = false;
        this.posTileA = new Position(0, 0);
        this.posTileB = new Position(1, 0);
        this.tileRotator = tileRotator;
    }

    boolean testOffset(int oldRotationIndex, int newRotationIndex) {
        return true; // true if test passed
    }

    @Override
    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        int oldRotationIndex = rotationIndex;
        rotationIndex = (rotationIndex + (clockwise ? 1 : 3)) % 4; // 0, 1, 2, 3 handle negative rotation

        // rotate the 2nd Tile with 1st Tile as center
        tileRotator.rotate(posTileA, posTileB, rotationIndex);

        // if shouldOffset is true, test if the new rotation is valid

        // if shouldOffset is true, and the new rotation is invalid, revert the rotation
    }

    @Override
    public boolean isPlaced() {
        return isPlaced;
    }

    @Override
    public void setPlaced(boolean placed) {
        isPlaced = placed;
    }

    @Override
    public Position getPosTileA() {
        return posTileA;
    }

    @Override
    public Position getPosTileB() {
        return posTileB;
    }
}
