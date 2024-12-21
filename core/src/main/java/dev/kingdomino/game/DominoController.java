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
        this.posTileB = new Position(posTileA.x() + 1, posTileA.y());  // tileB is always at the right of tileA
        this.tileRotator = tileRotator;
    }

    boolean testOffset(int oldRotationIndex, int newRotationIndex) {
        return true; // true if test passed
    }

    @Override
    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        // int oldRotationIndex = rotationIndex;
        rotationIndex = (rotationIndex + (clockwise ? 1 : 3)) % 4; // 0, 1, 2, 3 handle negative rotation

        // rotate the 2nd Tile with 1st Tile as center
        tileRotator.rotate(posTileA, posTileB, rotationIndex);

        // if shouldOffset is true, test if the new rotation is valid

        // if shouldOffset is true, and the new rotation is invalid, revert the rotation
    }

    public boolean isPlaced() {
        return isPlaced;
    }

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

    // helper functions
    private Position getTileBOffset() {
        switch (rotationIndex) {
            case 0:
                return new Position(1, 0);
            case 1:
                return new Position(0, 1);
            case 2:
                return new Position(-1, 0);
            case 3:
                return new Position(0, -1);
            default:
                return null;
        }
    }

    private Position getTileBPos() {
        return posTileA.add(getTileBOffset());
    }

    // setters
    @Override
    public void setPosTileA(Position posTileA) {
        posTileA.set(posTileA);
    }

    @Override
    public void setPosTileB(Position posTileB) {
        posTileB.set(posTileB);
    }

    /*
     * Set the position of the 2nd tile relative to the 1st tile.
     * 
     * Will not place yet, only set the position.
     */
    public void setPosDomino(Position posTileA) {
        this.posTileA.set(posTileA);
        this.posTileB.set(getTileBPos());
    }

    public void moveDomino(Offset offset) {
        Position posTileA = offset.apply(getPosTileA());
        setPosDomino(posTileA);
    }
}
