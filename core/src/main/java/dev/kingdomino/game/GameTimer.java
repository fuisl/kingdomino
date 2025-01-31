package dev.kingdomino.game;

import dev.kingdomino.effects.BackgroundShader;

/**
 * Singleton class that manages game timing.
 * 
 * Though singleton classes are generally discouraged, this class is an
 * exception (maybe)
 * 
 * @see EventManager
 * @see BackgroundShader
 * 
 * @author @fuisl
 * @version 1.0
 * 
 *          Work with the GameTimer class to manage game timing.
 */
public class GameTimer {
    public float realTime = 0f;
    public float totalTime = 0f;
    public float backgroundTime = 0f;

    private static GameTimer instance;

    /**
     * Updates the game timer.
     * 
     * @param dt The delta time since the last update.
     */
    public void update(float dt) {
        realTime += dt;
        totalTime += dt;

        @SuppressWarnings("null")
        float spinTime = BackgroundShader.refTable.get("u_spinTime") != null
                ? BackgroundShader.refTable.get("u_spinTime")
                : 0;
        backgroundTime += dt * spinTime;
    }

    private GameTimer() {
    }

    /**
     * Returns the singleton instance of the GameTimer.
     * 
     * @return The singleton instance.
     */
    public static synchronized GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

}
