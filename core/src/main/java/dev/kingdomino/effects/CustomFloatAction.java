package dev.kingdomino.effects;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public class CustomFloatAction extends Action {
    private float amplitude;           // Maximum vertical offset (in pixels)
    private float frequency;           // Oscillation frequency in radians per second
    private float rotationAmplitude;   // Maximum rotation offset in degrees
    private float elapsedTime = 0;
    private float originalY;
    private float originalRotation;
    private boolean firstRun = true;

    /**
     * @param amplitude         Maximum vertical offset in pixels.
     * @param frequency         Oscillation frequency in radians per second.
     * @param rotationAmplitude Maximum rotation offset in degrees.
     */
    public CustomFloatAction(float amplitude, float frequency, float rotationAmplitude) {
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.rotationAmplitude = rotationAmplitude;
    }

    @Override
    public boolean act(float delta) {
        if (firstRun) {
            // Ensure that the actor's origin is at its center.
            if (actor.getWidth() > 0 && actor.getHeight() > 0) {
                actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
            }
            originalY = actor.getY();
            originalRotation = actor.getRotation();
            // Add randomness: start at a random phase so that different actors don't oscillate in sync.
            elapsedTime = MathUtils.random(0, MathUtils.PI2);
            firstRun = false;
        }
        elapsedTime += delta;

        // Calculate vertical offset using a sine wave.
        float offset = amplitude * MathUtils.sin(frequency * elapsedTime);
        actor.setY(originalY + offset);

        // Calculate rotational offset using a sine wave.
        float rotationOffset = rotationAmplitude * MathUtils.sin(frequency * elapsedTime);
        actor.setRotation(originalRotation + rotationOffset);

        // Continue the action indefinitely.
        return false;
    }
}
