package dev.kingdomino.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public abstract class AbstractScreen extends Stage implements Screen {
    protected SpriteBatch spriteBatch;

    // TODO: populate with basic works shared by all screen
    protected AbstractScreen(SpriteBatch spriteBatch) {
        // sharing a SpriteBatch instead of each screen creating their own;
        // SpriteBatch is expensive!
        this.spriteBatch = spriteBatch;
    }

    // All screen will build their Stage in here.
    public abstract void buildStage();
}
