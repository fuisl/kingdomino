package dev.kingdomino.game;

public class GameTimer {
    private float realTime = 0f;
    private float totalTime = 0f;

    public float getCurrentTime() {
        return realTime;
    }

    public float getTotalTime() {
        return totalTime;
    }

    public void update(float dt) {
        realTime += dt;
        totalTime += dt;
    }
}
