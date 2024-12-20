package dev.kingdomino.game;

public class Tile {
    private TerrainType terrain;
    private int crown;

    public Tile(TerrainType terrain, int crown) {
        this.terrain = terrain;
        this.crown = crown;
    }

    public TerrainType getTerrain() {
        return terrain;
    }

    public int getCrown() {
        return crown;
    }
}
