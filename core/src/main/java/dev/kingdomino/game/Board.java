package dev.kingdomino.game;

public class Board implements IBoard {

    private final int CENTER = 4; // TODO: change this later for other game modes
    private Tile[][] land;
    private Point point; // scores
    private final TileValidator validator;

    public Board() {
        land = new Tile[9][9];
        land[CENTER][CENTER] = new Tile(TerrainType.CASTLE, 0);
        validator = new TileValidator(land);
    }

    @Override
    public boolean isTilePlaceable(Tile tile, int x, int y) {
        // TODO: change x, y to Position
        return validator.isTilePlaceable(tile, x, y);
    }

    @Override
    public void setTile(Tile tile, int x, int y) {
        // TODO: change x, y to Position
        land[x][y] = tile;

        // update spanning tiles if the tile is placed
        validator.updateSpanningTiles(x, y);
    }

    @Override
    public Tile getTile(int x, int y) {
        if (validator.isTileWithinLand(x, y)) {
            return land[x][y];
        }
        return null;
    }

    // Domino
    @Override
    public boolean isDominoPlaceable(Domino domino) {
        return validator.isTilePlaceable(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y()) &&
                validator.isTilePlaceable(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y()) &&
                (validator.isTileConnectable(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y()) ||
                        validator.isTileConnectable(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y()));
    }

    @Override
    public void setDomino(Domino domino) {
        if (isDominoPlaceable(domino)) {
            setTile(domino.getTileA(), domino.getPosTileA().x(), domino.getPosTileA().y());
            setTile(domino.getTileB(), domino.getPosTileB().x(), domino.getPosTileB().y());
        }
    }

    // Point related methods
    public int getFinalPoint() {
        return point.getFinalPoint();
    }

    public int getTotalPoint() {
        return point.getTotalPoint();
    }

    public int getAdditionalPoint() {
        return point.getAdditionalPoint();
    }

    public void addTotalPoint(int totalPoint) {
        point.addTotalPoint(totalPoint);
    }

    public void addBonusPoint(int bonusPoint) {
        point.addAdditionalPoint(bonusPoint);
    }

    public void reset() {
        point.reset();
    }

}