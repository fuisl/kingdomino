package dev.kingdomino.game;

/**
 * Controller for handling the rotation and placement of dominos in the game.
 */
public class DominoController {
    private int rotationIndex;
    private Position posTileA; // tileA is always at the center
    private Position posTileB; // tileB is always at the right of tileA -> relative position
    private TileRotator tileRotator;

    /**
     * Constructs a new DominoController with the specified tile rotator.
     *
     * @param tileRotator the tile rotator to use for rotating tiles
     */
    public DominoController() {
        this.rotationIndex = 0;
        this.posTileA = new Position(0, 0);
        this.posTileB = new Position(posTileA.x() + 1, posTileA.y()); // tileB is always at the right of tileA
        this.tileRotator = new TileRotator();
    }

    /**
     * Tests if the offset between the old and new rotation indices is valid.
     *
     * @param oldRotationIndex the old rotation index
     * @param newRotationIndex the new rotation index
     * @return true if the offset is valid, false otherwise
     */
    boolean testOffset(int oldRotationIndex, int newRotationIndex) {
        return true; // true if test passed
    }

    /**
     * Rotates the domino either clockwise or counterclockwise.
     *
     * @param clockwise    true to rotate clockwise, false to rotate
     *                     counterclockwise
     * @param shouldOffset true to test if the new rotation is valid
     */
    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        // int oldRotationIndex = rotationIndex;
        rotationIndex = (rotationIndex + (clockwise ? 1 : 3)) % 4; // 0, 1, 2, 3 handle negative rotation

        // rotate the 2nd Tile with 1st Tile as center
        tileRotator.rotate(posTileA, posTileB, rotationIndex, shouldOffset);

        // if shouldOffset is true, test if the new rotation is valid

        // if shouldOffset is true, and the new rotation is invalid, revert the rotation
    }

    /**
     * Gets the position of tile A.
     *
     * @return the position of tile A
     */
    public Position getPosTileA() {
        return posTileA;
    }

    /**
     * Gets the position of tile B.
     *
     * @return the position of tile B
     */
    public Position getPosTileB() {
        return posTileB;
    }

    // helper functions
    private Position getTileBOffset() {
        switch (rotationIndex) {
            case 0:
                return Direction.RIGHT.get();
            case 1:
                return Direction.DOWN.get();
            case 2:
                return Direction.LEFT.get();
            case 3:
                return Direction.UP.get();
            default:
                return null;
        }
    }

    private Position getTileBPos() {
        return posTileA.add(getTileBOffset());
    }

    /**
     * Sets the position of tile A.
     *
     * @param posTileA the new position of tile A
     */
    public void setPosTileA(Position newPosTileA) {
        this.posTileA.set(newPosTileA);
    }

    /**
     * Sets the position of tile B.
     *
     * @param posTileB the new position of tile B
     */
    public void setPosTileB(Position newPosTileB) {
        this.posTileB.set(newPosTileB);
    }

    /**
     * Sets the position of the domino relative to tile A.
     *
     * @param posTileA the new position of tile A
     */
    public void setPosDomino(Position posTileA) {
        this.posTileA.set(posTileA);
        this.posTileB.set(getTileBPos());
    }

    /**
     * Moves the domino by the specified offset.
     *
     * @param offset the offset to apply to the current position
     * @see Direction
     */
    public void moveDomino(Direction direction) {
        Position posTileA = direction.apply(getPosTileA());
        setPosDomino(posTileA);
    }
}
