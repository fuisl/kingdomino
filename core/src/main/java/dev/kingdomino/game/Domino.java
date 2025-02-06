package dev.kingdomino.game;

/**
 * Represents a domino in the game, consisting of two tiles and a controller.
 * 
 * @see Tile
 * @see DominoController
 * 
 * @author @fuisl
 * @version 1.0
 */
public class Domino {
    private final int id;
    private Tile tileA;
    private Tile tileB;
    private final DominoController dominoController;

    /**
     * Constructs a Domino with the specified id, tiles, and controller.
     *
     * @param id               the unique identifier of the domino
     * @param tileA            the first tile of the domino
     * @param tileB            the second tile of the domino
     * @param dominoController the controller for the domino
     */
    public Domino(int id, Tile tileA, Tile tileB, DominoController dominoController) {
        this.id = id;
        this.tileA = tileA;
        this.tileB = tileB;
        this.dominoController = dominoController;
    }

    /**
     * Constructs a copy of the specified domino.
     *
     * @param other the domino to copy
     */
    public Domino(Domino other) {
        this.id = other.id;
        this.tileA = other.tileA.copy();
        this.tileB = other.tileB.copy();
        this.dominoController = other.dominoController.copy();
    }

    /**
     * Creates and returns a copy of this domino.
     *
     * @return a new Domino object that is a copy of this domino
     */
    public Domino copy() {
        return new Domino(this);
    }

    /**
     * Returns the unique identifier of the domino.
     *
     * @return the id of the domino
     */
    public int getId() {
        return id;
    }

    /**
     * Returns the first tile of the domino.
     *
     * @return the first tile
     */
    public Tile getTileA() {
        return tileA;
    }

    /**
     * Returns the second tile of the domino.
     *
     * @return the second tile
     */
    public Tile getTileB() {
        return tileB;
    }

    /**
     * Returns the controller of the domino.
     *
     * @return the domino controller
     */
    public DominoController getDominoController() {
        return dominoController;
    }

    /**
     * Returns the position of the first tile.
     *
     * @return the position of the first tile
     */
    public Position getPosTileA() {
        return dominoController.getPosTileA();
    }

    /**
     * Returns the position of the second tile.
     *
     * @return the position of the second tile
     */
    public Position getPosTileB() {
        return dominoController.getPosTileB();
    }

    /**
     * Rotates the domino in the specified direction.
     *
     * @param clockwise true to rotate clockwise, false to rotate counterclockwise
     */
    public void rotateDomino(boolean clockwise) {
        dominoController.rotateDomino(clockwise, true);
    }

    /**
     * Rotates the domino in the specified direction, with an option to offset.
     * 
     * Currently offset not implemented.
     *
     * @param clockwise   true to rotate clockwise, false to rotate counterclockwise
     * @param shouldOffset true to apply offset, false otherwise
     */
    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        dominoController.rotateDomino(clockwise, shouldOffset);
    }

    /**
     * Sets the position of this domino based on another domino's position.
     *
     * @param domino the domino whose position will be used
     */
    public void setPosDomino(Domino domino) {
        setPosTileA(domino.getPosTileA());
        setPosTileB(domino.getPosTileB());
    }

    /**
     * Sets the position of the first tile.
     *
     * @param posTileA the new position for the first tile
     */
    public void setPosTileA(Position posTileA) {
        dominoController.setPosTileA(posTileA);
    }

    /**
     * Sets the position of the second tile.
     *
     * @param posTileB the new position for the second tile
     */
    public void setPosTileB(Position posTileB) {
        dominoController.setPosTileB(posTileB);
    }

    /**
     * Moves the domino in the specified direction.
     *
     * @param offset the direction to move the domino
     * @see Direction
     */
    public void moveDomino(Direction offset) {
        dominoController.moveDomino(offset);
    }

    /**
     * Sets the first tile of the domino.
     *
     * @param tileA the new first tile
     */
    public void setTileA(Tile tileA) {
        this.tileA = tileA;
    }

    /**
     * Sets the second tile of the domino.
     *
     * @param tileB the new second tile
     */
    public void setTileB(Tile tileB) {
        this.tileB = tileB;
    }

    /**
     * Undoes the last action performed on the domino.
     */
    public void undo() {
        dominoController.undo();
    }
}
