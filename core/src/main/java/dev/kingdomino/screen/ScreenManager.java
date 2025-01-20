package dev.kingdomino.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenManager {
    // Handles the switching and construction of screens.
    private Game game;

    public ScreenManager(Game game) {
        this.game = game;
    }

    public void showScreen(ScreenEnum screenEnum, SpriteBatch spriteBatch, AssetManager assetManager) {
        Screen currentScreen = game.getScreen();

        // screenEnum.getScreen() create a new instance of Screen
        // which mean dispose must be implemented.
        AbstractScreen newScreen = screenEnum.getScreen(spriteBatch, assetManager);
        newScreen.initScreen();
        game.setScreen(newScreen);

        if (currentScreen != null) currentScreen.dispose();
    }
}
