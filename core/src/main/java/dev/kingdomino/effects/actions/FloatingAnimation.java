package dev.kingdomino.effects.actions;

import java.util.Random;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;

public class FloatingAnimation extends Action {
    private float duration;
    private float maxDistOffset;
    private float maxAngleOffset;
    private float timeElapsed;

    private float originalY;

    private float distOffset;
    private float angleOffset;

    private boolean begin = false;

    /**
     * Create a new Floating Animation as an {@link Action}. The {@link Actor} will rise up
     * and down in range of [-maxDistOffset, maxDistOffset] from the original location
     * and rotate in range of [-maxAngleOffset, maxAngleOffset]. This happens 
     * indefinitely until the Action is manually removed. The animation is interpolated
     * via a sine function.
     * 
     * @param duration How long should one full movement take
     * @param maxDistOffset The maximum distance the actor will move away from its origin
     * @param maxAngleOffset The maximum angle the actor will rotate
     * 
     * @author LunaciaDev
     */
    public FloatingAnimation(float duration, float maxDistOffset, float maxAngleOffset) {
        this.duration = duration;
        this.maxDistOffset = maxDistOffset;
        this.maxAngleOffset = maxAngleOffset;

        this.timeElapsed = new Random().nextFloat(this.duration);
    }

    @Override
    public boolean act(float dt) {
        if (!begin) {
            begin();
            begin = true;
        }

        timeElapsed += dt;

        // avoid floating point overflow
        if (timeElapsed > duration)
            timeElapsed -= duration;

        // is *blazing fast* because sine value are cached in MathUtils
        float sineValue = MathUtils.sinDeg(timeElapsed / duration * 360);
        distOffset = sineValue * maxDistOffset;
        angleOffset = sineValue * maxAngleOffset;

        actor.setPosition(actor.getX(), originalY + distOffset);
        actor.setRotation(angleOffset);

        // loop indefinitely
        return false;
    }

    private void begin() {
        originalY = actor.getY();
        actor.setOrigin(actor.getWidth() / 2, actor.getHeight() / 2);
    }
}
