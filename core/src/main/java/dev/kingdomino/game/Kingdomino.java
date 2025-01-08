package dev.kingdomino.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import dev.kingdomino.screen.ScreenEnum;


public class Kingdomino extends Game {
    private SpriteBatch spriteBatch;

    @Override
    public void create() {
        spriteBatch = new SpriteBatch();
        setScreen(ScreenEnum.GameScreen.getScreen(spriteBatch));
    }   
}
