package dev.kingdomino.game;

public class Board {
    private Tile[][] land;
    private int totalPoint;
    private int additionalPoint;

    public Board() {
        land = new Tile[9][9];
        totalPoint = 0;
        additionalPoint = 0;
    }

    public void setTile(Tile tile, int x, int y) {
        land[x][y] = tile;
    }

    public Tile getTile(int x, int y) {
        return land[x][y];
    }

    public int getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(int totalPoint) {
        this.totalPoint = totalPoint;
    }

    public int getAdditionalPoint() {
        return additionalPoint;
    }

    public void setAdditionalPoint(int additionalPoint) {
        this.additionalPoint = additionalPoint;
    }
}