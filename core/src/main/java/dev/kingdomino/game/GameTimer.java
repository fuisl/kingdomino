package dev.kingdomino.game;

public class GameTimer {
    public float realTime = 0f;
    public float totalTime = 0f;
    private static GameTimer instance;

    public void update(float dt) {
        realTime += dt;
        totalTime += dt;
    }

    private GameTimer() {
    }

    public static GameTimer getInstance() {
        if (instance == null) {
            instance = new GameTimer();
        }
        return instance;
    }

}
