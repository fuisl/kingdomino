package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public enum ScreenEnum {
    // TODO: Attach each created screen to the Enum
    GameScreen {
        public AbstractScreen getScreen(SpriteBatch spriteBatch) {
            return new GameScreen(spriteBatch);
        }
    };
    
    public abstract AbstractScreen getScreen(SpriteBatch spriteBatch);
}
