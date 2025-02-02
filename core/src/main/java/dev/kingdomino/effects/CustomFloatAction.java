package dev.kingdomino.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public class CustomFloatAction extends Action {
    private float amplitude; // Maximum vertical offset (in pixels)
    private float frequency; // Frequency in radians per second
    private float elapsedTime = 0;
    private float originalY;
    private boolean firstRun = true;

    /**
     * @param amplitude Maximum offset in pixels.
     * @param frequency Oscillation frequency in radians per second.
     */
    public CustomFloatAction(float amplitude, float frequency) {
        this.amplitude = amplitude;
        this.frequency = frequency;
    }

    @Override
    public boolean act(float delta) {
        if (firstRun) {
            // Record the actor’s starting Y position.
            originalY = actor.getY();
            firstRun = false;
        }
        elapsedTime += delta;
        // Calculate the vertical offset using a sine wave.
        float offset = amplitude * MathUtils.sin(frequency * elapsedTime);
        actor.setY(originalY + offset);
        // Return false to indicate the action should continue forever.
        return false;
    }
}
