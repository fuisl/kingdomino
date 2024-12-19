package dev.kingdomino.game;

enum terrainType {
    WHEATFIELD,
    FOREST,
    LAKE,
    GRASSLAND,
    SWAMP,
    MINE,
    MOUNTAIN,
    DESERT
}

public class Tile {
    private terrainType terrain;
    private int crown;

    public Tile(terrainType terrain, int crown) {
        this.terrain = terrain;
        this.crown = crown;
    }

    public terrainType getTerrain() {
        return terrain;
    }

    public int getCrown() {
        return crown;
    }
}
