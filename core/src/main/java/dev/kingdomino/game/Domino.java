package dev.kingdomino.game;

public class Domino {
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
    //     this(id, tileA, tileB, new DominoController(new TileRotator()));
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
}
