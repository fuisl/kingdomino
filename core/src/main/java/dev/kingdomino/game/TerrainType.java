package dev.kingdomino.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Enum representing the different terrain types in the game.
 */
public enum TerrainType {
    WHEATFIELD,
    FOREST,
    LAKE,
    GRASSLAND,
    SWAMP,
    MINE,
    MOUNTAIN,
    DESERT,
    CASTLE,
    INVALID;
    //ERROR,

    // Extending the enum with its associated texture.
    private TextureRegion texture;

    public TextureRegion getTexture() {return this.texture;}

    public void setTexture(TextureRegion texture) {this.texture = texture;}
}
