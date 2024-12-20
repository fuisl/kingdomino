package dev.kingdomino.game;

public class Board {
    private Tile[][] land;
    private Point point; // scores

    public Board() {
        land = new Tile[9][9];
    }

    public void setTile(Tile tile, int x, int y) {
        land[x][y] = tile; // TODO: boundary check
    }

    public Tile getTile(int x, int y) {
        return land[x][y]; // TODO: boundary check
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