package dev.kingdomino.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.kingdomino.screen.ScreenEnum;
import dev.kingdomino.screen.ScreenManager;


public class Kingdomino extends Game {
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        ScreenManager.getInstance().initialize(this);
        ScreenManager.getInstance().showScreen(ScreenEnum.GameScreen, spriteBatch);
    }   
}
