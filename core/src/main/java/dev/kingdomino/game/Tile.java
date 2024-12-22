package dev.kingdomino.game;

/**
 * Represents a tile in the game with a terrain type and crown count.
 */
public class Tile {
    private TerrainType terrain;
    private int crown;

    /**
     * Constructs a Tile with the specified terrain type and crown count.
     *
     * @param terrain the terrain type of the tile
     * @param crown   the number of crowns on the tile
     */
    public Tile(TerrainType terrain, int crown) {
        this.terrain = terrain;
        this.crown = crown;
    }

    /**
     * Returns the terrain type of the tile.
     *
     * @return the terrain type
     */
    public TerrainType getTerrain() {
        return terrain;
    }

    /**
     * Returns the number of crowns on the tile.
     *
     * @return the crown count
     */
    public int getCrown() {
        return crown;
    }
}
