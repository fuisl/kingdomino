package dev.kingdomino.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import dev.kingdomino.screen.ScreenEnum;
import dev.kingdomino.screen.ScreenManager;


public class Kingdomino extends Game {
    private SpriteBatch spriteBatch;
    private AssetManager assetManager;
    private ScreenManager screenManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        spriteBatch = new SpriteBatch();
        screenManager = new ScreenManager(this);

        queueAssets(assetManager);

        // TODO move assetManager loading to its own separate screen.
        // Right now this will BLOCK execution until all asset are loaded.
        assetManager.finishLoading();

        screenManager.showScreen(ScreenEnum.GAMESCREEN, spriteBatch, assetManager);
    }

    private void queueAssets(AssetManager assetManager) {
        /* 
         * there should be a better way to do this but oh well.
         * it is critical to load ALL asset via the assetManager as we can clean them all up in one swoop.
         * the game is not big enough to warrant partial loading.
         * At least, hopefully, I dont want to deal with that mess
         */
        assetManager.load("gameTextures.atlas", TextureAtlas.class);
        assetManager.load("PixelifySansHeader.fnt", BitmapFont.class);
        assetManager.load("PixelifySansBody.fnt", BitmapFont.class);
    }

    @Override
    public void dispose() {
        // clear all loaded asset via asset manager
        assetManager.dispose();
    }
}
