package dev.kingdomino.screen;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class AbstractScreen implements Screen {
    protected SpriteBatch spriteBatch;
    protected AssetManager assetManager;

    // TODO: populate with basic works shared by all screen

    // TODO: convert this into variable Object parameter as it is only getting worse hardcoding EVERYTHING
    // I have to update like 3 different file to pass the assetManager in...
    protected AbstractScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        // sharing a SpriteBatch instead of each screen creating their own;
        // SpriteBatch is expensive!
        this.spriteBatch = spriteBatch;
        this.assetManager = assetManager;
    }

    // All screen will build their Stage in here.
    public abstract void initScreen();

    @Override
    public void dispose() {}

    @Override
    public void show() {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}
}
