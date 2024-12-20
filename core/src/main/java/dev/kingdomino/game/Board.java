package dev.kingdomino.game;

public class Board {
    private Tile[][] land;
    private Point point; // scores
    private final TileValidator validator;

    public Board() {
        land = new Tile[9][9];
        land[4][4] = new Tile(TerrainType.CASTLE, 0);
        validator = new TileValidator(land);  
    }

    public boolean isTilePlaceable(Tile tile, int x, int y) {
        return validator.isTilePlaceable(tile, x, y);
    }

    public void setTile(Tile tile, int x, int y) {
        if (isTilePlaceable(tile, x, y)) {
            land[x][y] = tile;
        }
    }

    public Tile getTile(int x, int y) {
        if (validator.isTileWithinBound(x, y)) {
            return land[x][y];
        }
        return null;
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