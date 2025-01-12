package dev.kingdomino.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import dev.kingdomino.screen.ScreenEnum;
import dev.kingdomino.screen.ScreenManager;


public class Kingdomino extends Game {
    private SpriteBatch spriteBatch;
    private AssetManager assetManager;

    @Override
    public void create() {
        assetManager = new AssetManager();
        spriteBatch = new SpriteBatch();

        queueAssets(assetManager);

        // TODO move assetManager loading to its own separate screen.
        // Right now this will BLOCK execution until all asset are loaded.
        assetManager.finishLoading();

        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.GAMESCREEN, spriteBatch, assetManager);
    }

    private void queueAssets(AssetManager assetManager) {
        /* 
         * there should be a better way to do this but oh well.
         * it is critical to load ALL asset via the assetManager as we can clean them all up in one swoop.
         * the game is not big enough to warrant partial loading.
         * At least, hopefully, I dont want to deal with that mess
         */
        assetManager.load("tileTextures.atlas", TextureAtlas.class);
        assetManager.load("skin/uiskin.json", Skin.class);
    }

    @Override
    public void dispose() {
        // clear all loaded asset via asset manager
        assetManager.dispose();
    }
}
