package dev.kingdomino.game;

/**
 * Controller for handling the rotation and placement of dominos in the game.
 */
public class DominoController {
    private int rotationIndex;
    private final Position posTileA; // tileA is always at the center
    private final Position posTileB; // tileB is always at the right of tileA -> relative position
    private final TileRotator tileRotator;
    private Direction lastDirection; // last direction moved
    private int lastRotationIndex;
    private int lastAction; // last action performed {0: move, 1: rotate}

    /**
     * Constructs a new DominoController with the specified tile rotator.
     */
    public DominoController() {
        this.rotationIndex = 0;
        this.posTileA = new Position(0, 0);
        this.posTileB = new Position(posTileA.x() + 1, posTileA.y()); // tileB is always at the right of tileA
        this.tileRotator = new TileRotator();
    }

    /**
     * Constructs a new DominoController by copying another DominoController.
     *
     * @param other the DominoController to copy
     */
    public DominoController(DominoController other) {
        this.rotationIndex = other.rotationIndex;
        this.posTileA = new Position(other.posTileA);
        this.posTileB = new Position(other.posTileB);
        this.tileRotator = new TileRotator();
    }

    /**
     * Creates a copy of this DominoController.
     *
     * @return a new DominoController that is a copy of this one
     */
    public DominoController copy() {
        return new DominoController(this);
    }

    /**
     * Tests if the offset between the old and new rotation indices is valid.
     *
     * @param oldRotationIndex the old rotation index
     * @param newRotationIndex the new rotation index
     * @return true if the offset is valid, false otherwise
     */
    @SuppressWarnings("unused")
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
        lastRotationIndex = rotationIndex;
        rotationIndex = (rotationIndex + (clockwise ? 1 : 3)) % 4; // 0, 1, 2, 3 handle negative rotation

        // rotate the 2nd Tile with 1st Tile as center
        tileRotator.rotate(posTileA, posTileB, rotationIndex, shouldOffset);

        lastAction = 1; // last action is rotate
    }

    /**
     * Undoes the last rotation of the domino.
     */
    public void undoRotateDomino() {
        rotationIndex = lastRotationIndex;
        tileRotator.rotate(posTileA, posTileB, rotationIndex, true);
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
     * @param newPosTileA the new position of tile A
     */
    public void setPosTileA(Position newPosTileA) {
        this.posTileA.set(newPosTileA);
    }

    /**
     * Sets the position of tile B.
     *
     * @param newPosTileB the new position of tile B
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
     * Moves the domino by the specified direction.
     *
     * @param direction the direction to move the domino
     */
    public void moveDomino(Direction direction) {
        Position newPosTileA = direction.apply(getPosTileA());
        setPosDomino(newPosTileA);
        lastDirection = direction;
        lastAction = 0;
    }

    /**
     * Moves the domino by the specified direction, optionally in the opposite
     * direction.
     *
     * @param direction the direction to move the domino
     * @param opposite  true to move in the opposite direction, false otherwise
     */
    public void moveDomino(Direction direction, boolean opposite) {
        Position newposTileA = opposite ? direction.opposite().apply(getPosTileA())
                : direction.apply(getPosTileA());
        setPosDomino(newposTileA);
        lastDirection = direction;
        lastAction = 0;
    }

    /**
     * Undoes the last move of the domino.
     */
    public void undoMoveDomino() {
        moveDomino(lastDirection, true);
    }

    /**
     * Gets the current rotation index of the domino.
     *
     * @return the current rotation index
     */
    public int getRotationIndex() {
        return this.rotationIndex;
    }

    /**
     * Undoes the last action performed on the domino.
     */
    public void undo() {
        if (lastAction == 0) {
            undoMoveDomino();
        } else if (lastAction == 1) {
            undoRotateDomino();
        }
    }
}
