package dev.kingdomino.game;

public class Domino implements IDominoController {
    private int id;
    private Tile tileA;
    private Tile tileB;
    private IDominoController dominoController;

    public Domino(int id, Tile tileA, Tile tileB, IDominoController dominoController) {
        this.id = id;
        this.tileA = tileA;
        this.tileB = tileB;
        this.dominoController = dominoController;
    }

    // public Domino(int id, Tile tileA, Tile tileB) {
    // this(id, tileA, tileB, new DominoController(new TileRotator()));
    // }

    public int getId() {
        return id;
    }

    public Tile getTileA() {
        return tileA;
    }

    public Tile getTileB() {
        return tileB;
    }

    public IDominoController getDominoController() {
        return dominoController;
    }

    public Position getPosTileA() {
        return dominoController.getPosTileA();
    }

    public Position getPosTileB() {
        return dominoController.getPosTileB();
    }

    public void rotate(boolean clockwise) {
        dominoController.rotateDomino(clockwise, true);
    }

    @Override
    public void rotateDomino(boolean clockwise, boolean shouldOffset) {
        dominoController.rotateDomino(clockwise, shouldOffset);
    }

    @Override
    public void setPosTileA(Position posTileA) {
        dominoController.setPosTileA(posTileA);
    }

    @Override
    public void setPosTileB(Position posTileB) {
        dominoController.setPosTileB(posTileB);
    }

    @Override
    public void moveDomino(Offset offset) {
        dominoController.moveDomino(offset);
    }
}
