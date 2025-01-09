package dev.kingdomino.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ScreenManager {
    // Handles the switching and construction of screens.

    private Game game;

    // Only a single ScreenManager may exists.
    private static ScreenManager instance;

    private ScreenManager() {
        return;
    }

    public static ScreenManager getInstance() {
        if (instance == null) {
            instance = new ScreenManager();
        }

        return instance;
    }

    // Initialize with instance of Game

    public void initialize(Game game) {
        this.game = game;
    }

    public void showScreen(ScreenEnum screenEnum, SpriteBatch spriteBatch, AssetManager assetManager) {
        Screen currentScreen = game.getScreen();

        AbstractScreen newScreen = screenEnum.getScreen(spriteBatch, assetManager);
        newScreen.initScreen();
        game.setScreen(newScreen);

        if (currentScreen != null) currentScreen.dispose();
    }
}
