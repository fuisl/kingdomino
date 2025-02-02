package dev.kingdomino.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public class ShakeAction extends Action {
    private float duration; // Total shake time in seconds
    private float time; // Accumulated time
    private float magnitude; // Maximum offset in pixels

    private float originalX; // Actor's original X position
    private float originalY; // Actor's original Y position;

    public ShakeAction(float duration, float magnitude) {
        this.duration = duration;
        this.magnitude = magnitude;
    }

    @Override
    public boolean act(float delta) {
        // On the first frame, record the actor's original position.
        if (time == 0) {
            originalX = actor.getX();
            originalY = actor.getY();
        }
        time += delta;
        if (time > duration) {
            // Shake finished; reset the position.
            actor.setPosition(originalX, originalY);
            return true;
        }
        // Calculate a damping factor (shake gradually decreases)
        float currentMagnitude = magnitude * ((duration - time) / duration);

        // Generate random offsets in both X and Y directions.
        float offsetX = MathUtils.random(-currentMagnitude, currentMagnitude);
        float offsetY = MathUtils.random(-currentMagnitude, currentMagnitude);

        actor.setPosition(originalX + offsetX, originalY + offsetY);
        return false;
    }
}
