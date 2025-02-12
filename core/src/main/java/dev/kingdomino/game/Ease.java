package dev.kingdomino.game;

import java.util.Map;
import java.util.function.Function;

/**
 * Handles easing animations (refTable values) for game events.
 * 
 * @see Event
 * @see EventManager
 * 
 * @author @fuisl
 * @version 1.0
 * 
 *          Adapted from Balatro game.
 */
public class Ease {

    /**
     * Easing type enumeration mirroring 'lerp', 'elastic', 'quad'.
     */
    public enum EaseType {
        LERP, ELASTIC, QUAD
    }

    private final EaseType easeType;

    private final Map<String, Float> refTable;

    // Equivalent to self.ease.ref_value (key in the refTable)
    private final String refValue;

    // Start and end VALUES for the easing
    private float startVal;
    private final float endVal;

    // Start and end TIMES for the easing
    private float startTime;
    private float endTime;

    // Delay from the original event, used to compute endTime = startTime + delay
    private final float delay;

    // Whether we've initialized the ease
    private boolean initialized;

    // Optional function that modifies the interpolated value
    // If not provided, we default to the identity function: x -> x
    private final Function<Float, Float> func;

    // flag complete
    private boolean complete;

    /**
     * Constructs an Ease object with specified parameters.
     * 
     * @param easeType The type of easing, e.g., LERP, ELASTIC, QUAD
     * @param refTable The table or map storing the float value to be eased
     * @param refValue The key in refTable whose value will be adjusted
     * @param endVal   The target value after the ease finishes
     * @param delay    How long (in seconds) the ease should take
     * @param func     A function applied to the interpolated result. If null, uses
     *                 identity.
     */
    public Ease(EaseType easeType,
            Map<String, Float> refTable,
            String refValue,
            float endVal,
            float delay,
            Function<Float, Float> func) {

        this.easeType = easeType;
        this.refTable = refTable;
        this.refValue = refValue;
        this.endVal = endVal;
        this.delay = delay;
        this.func = (func != null) ? func : (x -> x);

        this.initialized = false;
        this.complete = false;
    }

    /**
     * Updates the ease with the current time.
     *
     * @param currentTime The current global time, e.g. from GameTimer or
     *                    Gdx.graphics.getDeltaTime accumulations
     */
    public void update(float currentTime) {
        if (complete) {
            return; // Already finished
        }

        // Initialize if needed (mirrors "if not self.ease.start_time")
        if (!initialized) {
            startTime = currentTime;
            endTime = currentTime + delay;
            Float initialVal = refTable.get(refValue);
            // If missing, assume 0; or handle as needed
            startVal = (initialVal != null) ? initialVal : 0f;
            initialized = true;
        }

        // If we haven't reached endTime, interpolate.
        if (currentTime < endTime) {
            // percent_done = ((end_time - current_time) / (end_time - start_time))
            // But to keep it consistent with typical "progress" from 0 to 1,
            // we can do either:
            // progress = (current_time - start_time) / (end_time - start_time)
            // or keep the original definition (which inverts the fraction).
            float percent = (endTime - currentTime) / (endTime - startTime); // percent_done
            float interpolatedValue;

            switch (easeType) {
                case LERP:
                    interpolatedValue = percent * startVal + (1 - percent) * endVal;
                    break;

                case ELASTIC:
                    // The code:
                    // percent_done = -2^(10*(percent_done-1)) * sin((percent_done*10 -
                    // 10.75)*2*pi/3) replicates an "elastic" easing.
                    // The snippet slightly modifies percent_done first, then uses it in a lerp-like
                    // expression.
                    float tmpElastic = -(float) Math.pow(2, 10 * (percent - 1))
                            * (float) Math.sin((percent * 10 - 10.75) * (2 * Math.PI / 3));
                    interpolatedValue = tmpElastic * startVal + (1 - tmpElastic) * endVal;
                    break;

                case QUAD:
                    // The code:
                    // percent_done = percent_done * percent_done
                    // self.ease.ref_table[self.ease.ref_value] = self.func(percent_done*start_val +
                    // (1-percent_done)*end_val)
                    float tmpQuad = percent * percent;
                    interpolatedValue = tmpQuad * startVal + (1 - tmpQuad) * endVal;
                    break;

                default:
                    // Fallback to LERP or do nothing
                    interpolatedValue = percent * startVal + (1 - percent) * endVal;
            }

            // Apply the user-supplied function to the final result
            float finalValue = func.apply(interpolatedValue);

            // Store back into the reference table
            refTable.put(refValue, finalValue);
        } else {
            // If we've passed endTime, set the final value = endVal
            float finalValue = func.apply(endVal);
            refTable.put(refValue, finalValue);

            // Mark complete
            complete = true;
        }
    }

    /**
     * Checks if the ease has finished.
     * 
     * @return true if the ease has finished, false otherwise
     */
    public boolean isComplete() {
        return complete;
    }

    // expose for testing
    public float getStartVal() {
        return startVal;
    }

    public float getEndVal() {
        return endVal;
    }

    public float getStartTime() {
        return startTime;
    }

    public float getEndTime() {
        return endTime;
    }

    public float getDelay() {
        return delay;
    }

    public EaseType getEaseType() {
        return easeType;
    }
}
