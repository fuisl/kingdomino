package dev.kingdomino.effects;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;

import dev.kingdomino.game.Ease;
import dev.kingdomino.game.Ease.EaseType;
import dev.kingdomino.game.Event;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.EventManager;

/**
 * Manages background effects such as color transitions, spinning, and screen shake.
 * 
 * @author @fuisl
 * @version 1.0
 */
public class BackgroundManager {
    private static final Map<String, Float> refTable = BackgroundShader.refTable; // shader use refTable for updating realtime
    private static final Map<String, Color> colorTable = BackgroundShader.colorTable; // shader use colorTable for updating realtime
    public static final Map<String, Color[]> colorMap = new HashMap<>(); // color map for different color schemes
    private static final EventManager eventManager = EventManager.getInstance();
    private static final Map<String, Map<String, Float>> tempTables = new HashMap<>();
    private static OrthographicCamera sharedCamera;

    static {
        // TODO: populate color map
        colorMap.put("default_blue",
                new Color[] { Color.valueOf("#020608"), Color.valueOf("#12445d"), Color.valueOf("#2382b2") });
        colorMap.put("default_red",
                new Color[] { Color.valueOf("#1c1a19"), Color.valueOf("#790f14"), Color.valueOf("#9e1b32") });
        colorMap.put("default_green",
                new Color[] { Color.valueOf("#1c2434"), Color.valueOf("#045c4b"), Color.valueOf("#00373a") }); //
        colorMap.put("default_yellow",
                new Color[] { Color.valueOf("#103f78"), Color.valueOf("#f09526"), Color.valueOf("#f09526") });
    }

    static {
        // init refTable
        refTable.put("u_time", 0f); // control overall color bleeding. CURRENTLY UNUSED
        refTable.put("u_spinTime", 0.0f); // control spining. [-1, 1] should go with the spinAmount
        refTable.put("u_spinAmount", 0.18f); // control the shape of the spin. 0.2f is the sweet spot
        refTable.put("u_contrast", 1.5f); // control contrast

        // refTable for screen shake
        refTable.put("u_shake", 0.0f);

        // init colorTable. current colors for background = u_color1, 2 and 3.
        colorTable.put("u_color1", Color.valueOf("020608"));
        colorTable.put("u_color2", Color.valueOf("020608"));
        colorTable.put("u_color3", Color.valueOf("020608"));
    }

    /**
     * Starts the background spinning.
     */
    public static void startSpin() {
        Event startSpinning = new Event(TriggerType.EASE, false, false, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.5f, 0.5f, null));
        eventManager.addEvent(startSpinning.copy(), "background", false);
    }

    /**
     * Rewinds the background spinning and starts spinning again. (DJ like)
     */
    public static void rewindSpin() {
        Event stopSpinning = new Event(TriggerType.EASE, true, false, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", -5.0f, 0.15f, null));

        Event startSpinning = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.5f, 0.35f, null));

        eventManager.addEvent(stopSpinning.copy(), "background", false);
        eventManager.addEvent(startSpinning.copy(), "background", false);
    }

    /**
     * Stops the background spinning.
     */
    public static void stopSpinning() {
        Event resetSpinTime = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.0f, 0.5f, null));
        eventManager.addEvent(resetSpinTime.copy(), "background", false);
    }

    // TODO: code the effect for the spin curve. CURRENTLY NOT IN USE!
    /**
     * Resets the spin curve effect.
     */
    public static void resetSpinCurve() {
        Event resetSpinCurve = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinAmount", 0.14f, 0.5f, null));
        eventManager.addEvent(resetSpinCurve.copy(), "background", false);
    }

    /**
     * Starts the spin curve effect.
     */
    public static void startSpinCurve() {
        Event startSpinCurve = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinAmount", 0.15f, 0.5f, null));
        eventManager.addEvent(startSpinCurve.copy(), "background", false);
    }

    /**
     * Updates a single color in the color table.
     * 
     * @param colorName the name of the color to update
     * @param tempTable the temporary table containing the new color values
     */
    public static void updateColor(String colorName, Map<String, Float> tempTable) {
        colorTable.get(colorName).set(tempTable.get("r"), tempTable.get("g"), tempTable.get("b"), tempTable.get("a"));
    }

    /**
     * Performs a linear interpolation (LERP) between the current and target colors over a duration.
     * 
     * @param current the current color
     * @param target the target color
     * @param duration the duration of the interpolation
     * @return a temporary table containing the interpolated color values
     */
    public static HashMap<String, Float> colorLerp(Color current, Color target, float duration) {
        HashMap<String, Float> tempTable = new HashMap<>();
        tempTable.put("r", current.r);
        tempTable.put("g", current.g);
        tempTable.put("b", current.b);
        tempTable.put("a", current.a);

        Ease redEase = new Ease(EaseType.LERP, tempTable, "r", target.r, duration, null);
        Ease greenEase = new Ease(EaseType.LERP, tempTable, "g", target.g, duration, null);
        Ease blueEase = new Ease(EaseType.LERP, tempTable, "b", target.b, duration, null);
        Ease alphaEase = new Ease(EaseType.LERP, tempTable, "a", target.a, duration, null);

        Event redEvent = new Event(TriggerType.EASE, true, false, null, null, null, null, redEase);
        Event greenEvent = new Event(TriggerType.EASE, true, false, null, null, null, null, greenEase);
        Event blueEvent = new Event(TriggerType.EASE, true, false, null, null, null, null, blueEase);
        Event alphaEvent = new Event(TriggerType.EASE, true, false, null, null, null, null, alphaEase);

        eventManager.addEvent(redEvent, "color", false);
        eventManager.addEvent(greenEvent, "color", false);
        eventManager.addEvent(blueEvent, "color", false);
        eventManager.addEvent(alphaEvent, "color", false);
        // eventManager.addEvent(changeColor.copy(), "color", false);
        return tempTable;
    }

    /**
     * Switches the background colors with a specified duration.
     * 
     * @param colors the array of new colors
     * @param duration the duration of the color switch
     */
    public static void colorSwitch(Color[] colors, float duration) {
        tempTables.put("u_color1", colorLerp(colorTable.get("u_color1"), colors[0], duration));
        tempTables.put("u_color2", colorLerp(colorTable.get("u_color2"), colors[1], duration));
        tempTables.put("u_color3", colorLerp(colorTable.get("u_color3"), colors[2], duration));
    }

    /**
     * Switches the background colors with a default duration.
     * 
     * @param colors the array of new colors
     */
    public static void colorSwitch(Color[] colors) {
        colorSwitch(colors, 0.2f);
    }

    /**
     * Updates all colors in the color table.
     */
    public static void updateColors() {
        for (Map.Entry<String, Map<String, Float>> entry : tempTables.entrySet()) {
            updateColor(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Sets the shared camera for background effects.
     * 
     * @param camera the OrthographicCamera to set
     */
    public static void setCamera(OrthographicCamera camera) {
        sharedCamera = camera;
    }

    /**
     * Updates the camera position based on the shake effect.
     */
    public static void updateCameraShakePosition() {
        float shake = refTable.get("u_shake");

        float x_random = MathUtils.random(shake) - (shake / 2);
        float y_random = MathUtils.random(shake) - (shake / 2);

        // update the camera position around [-shake/2, shake/2] for x and y
        sharedCamera.position.set(sharedCamera.position.x + x_random, sharedCamera.position.y + y_random,
                sharedCamera.position.z);
        sharedCamera.update();
    }

    /**
     * Initiates a screen shake effect.
     */
    public static void screenShake() {
        Event shakeEvent = new Event(TriggerType.EASE, true, false, null, null, null, null,
                new Ease(EaseType.ELASTIC, refTable, "u_shake", 5.0f, 0.1f, null));

        Event stopShakeEvent = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_shake", 0.0f, 0.1f, null));

        eventManager.addEvent(shakeEvent.copy(), "shake", true);
        eventManager.addEvent(stopShakeEvent.copy(), "shake", false);
        // event
    }

}
