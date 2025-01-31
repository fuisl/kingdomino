package dev.kingdomino.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Enum representing the different terrain types in the game.
 */
public enum TerrainType {
    // TODO: minimize the number of terrain types
    FIELD,
    FOREST,
    LAKE,
    PLAINS,
    SWAMPS,
    MINE,
    CASTLE,
    INVALID;
    //ERROR,

    // Extending the enum with its associated texture.
    private TextureRegion texture;
    private TextureRegion[] castleTextures;

    public TextureRegion getTexture() {return this.texture;}
    public TextureRegion getCastleTexture(int id) {return this.castleTextures[id];}

    public void setTexture(TextureRegion texture) {this.texture = texture;}
    public void setCastleTexture(TextureRegion[] castleTextures) {this.castleTextures = castleTextures; }
}
