package dev.kingdomino.game;

import java.util.Map;
import java.util.function.Function;

public class Ease {

    /**
     * Easing type enumeration mirroring 'lerp', 'elastic', 'quad'.
     */
    public enum EaseType {
        LERP, ELASTIC, QUAD
    }

    private EaseType easeType;

    private Map<String, Float> refTable;

    // Equivalent to self.ease.ref_value (key in the refTable)
    private String refValue;

    // Start and end VALUES for the easing
    private float startVal;
    private float endVal;

    // Start and end TIMES for the easing
    private float startTime;
    private float endTime;

    // Delay from the original event, used to compute endTime = startTime + delay
    private float delay;

    // Whether we've initialized the ease
    private boolean initialized;

    // Optional function that modifies the interpolated value
    // If not provided, we default to the identity function: x -> x
    private Function<Float, Float> func;

    // flag complete
    private boolean complete;

    /**
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
     * Update the ease with the current time.
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
            float percentDone = (endTime - currentTime) / (endTime - startTime);
            float interpolatedValue;

            switch (easeType) {
                case LERP:
                    interpolatedValue = percentDone * startVal + (1 - percentDone) * endVal;
                    break;

                case ELASTIC:
                    // The code:
                    // percent_done = -2^(10*(percent_done-1)) * sin((percent_done*10 -
                    // 10.75)*2*pi/3) replicates an "elastic" easing.
                    // The snippet slightly modifies percent_done first, then uses it in a lerp-like
                    // expression.
                    float tmpElastic = -(float) Math.pow(2, 10 * (percentDone - 1))
                            * (float) Math.sin((percentDone * 10 - 10.75) * (2 * Math.PI / 3));
                    interpolatedValue = tmpElastic * startVal + (1 - tmpElastic) * endVal;
                    break;

                case QUAD:
                    // The code:
                    // percent_done = percent_done * percent_done
                    // self.ease.ref_table[self.ease.ref_value] = self.func(percent_done*start_val +
                    // (1-percent_done)*end_val)
                    float tmpQuad = percentDone * percentDone;
                    interpolatedValue = tmpQuad * startVal + (1 - tmpQuad) * endVal;
                    break;

                default:
                    // Fallback to LERP or do nothing
                    interpolatedValue = percentDone * startVal + (1 - percentDone) * endVal;
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
     * @return true if the ease has finished.
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
}
