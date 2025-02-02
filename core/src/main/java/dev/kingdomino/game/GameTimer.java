package dev.kingdomino.game;

import dev.kingdomino.effects.BackgroundShader;

public class GameTimer {
    public float realTime = 0f;
    public float totalTime = 0f;
    public float backgroundTime = 0f;

    private static GameTimer instance;

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

    public static synchronized GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

}
