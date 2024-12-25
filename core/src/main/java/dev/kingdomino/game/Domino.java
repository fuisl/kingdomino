package dev.kingdomino.game;

/**
 * Represents a domino in the game, consisting of two tiles and a controller.
 */
public class Domino implements IDominoController {
    private int id;
    private Tile tileA;
    private Tile tileB;
    private DominoController dominoController;

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

    // public Domino(int id, Tile tileA, Tile tileB) {
    // this(id, tileA, tileB, new DominoController(new TileRotator()));
    // }

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

    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        dominoController.rotateDomino(clockwise, shouldOffset);
    }

    public void setPosTileA(Position posTileA) {
        dominoController.setPosTileA(posTileA);
    }

    public void setPosTileB(Position posTileB) {
        dominoController.setPosTileB(posTileB);
    }

    public void moveDomino(Direction offset) {
        dominoController.moveDomino(offset);
    }
}
