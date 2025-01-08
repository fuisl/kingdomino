package dev.kingdomino.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.TerrainType;

public class GameScreen extends AbstractScreen {
    private GameManager gameManager;
    private TextureRegion[] crownOverlay;

    protected GameScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch, assetManager);
    }

    @Override
    public void buildStage() {
        TextureAtlas atlas = assetManager.get("tileTextures.atlas");

        for (TerrainType name : TerrainType.values()) {
            name.setTexture(atlas.findRegion(name.name().toLowerCase()));
        }

        crownOverlay = new TextureRegion[4];
        crownOverlay[0] = atlas.findRegion("nocrown");
        crownOverlay[1] = atlas.findRegion("onecrown");
        crownOverlay[2] = atlas.findRegion("twocrown");
        crownOverlay[3] = atlas.findRegion("threecrown");
    };

    @Override
    public void show() {
        gameManager = new GameManager();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        gameManager.update(delta);

        spriteBatch.begin();
        // TODO remove this as we are now rendering from the UI
        // keep for debugging purpose only.
        gameManager.render(spriteBatch);
        spriteBatch.draw(TerrainType.WHEATFIELD.getTexture(), 10, 10);
        spriteBatch.end();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }

    @Override
    // we keep this method empty because ALL asset are managed via AssetManager.
    public void dispose() {}
}