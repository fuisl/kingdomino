package dev.kingdomino.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ScreenEnum {
    // TODO: Attach each created screen to the Enum
    GAMESCREEN {
        public AbstractScreen getScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
            return new GameScreen(spriteBatch, assetManager);
        }
    };
    
    public abstract AbstractScreen getScreen(SpriteBatch spriteBatch, AssetManager assetManager);
}
