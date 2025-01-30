package dev.kingdomino.effects;

import java.util.HashMap;
import java.util.Map;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import dev.kingdomino.game.Ease;
import dev.kingdomino.game.Ease.EaseType;
import dev.kingdomino.game.Event;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.EventManager;

public class BackgroundManager {
    private static final Map<String, Float> refTable = BackgroundShader.refTable; // ref to BackgroundShader.refTable
    private static final Map<String, Color> colorTable = BackgroundShader.colorTable;
    public static final Map<String, Color[]> colorMap = new HashMap<>();
    private static final EventManager eventManager = EventManager.getInstance();
    private static final Map<String, Map<String, Float>> tempTables = new HashMap<>();
    private static OrthographicCamera sharedCamera;
    private static Vector3 basePosition;

    static {
        // TODO: populate color map
        colorMap.put("default_blue",
                new Color[] { Color.valueOf("02394A"), Color.valueOf("043565"), Color.valueOf("5158BB") });
        colorMap.put("default_red",
                new Color[] { Color.valueOf("4A0707"), Color.valueOf("5C0B0B"), Color.valueOf("A61B1B") });
        colorMap.put("default_green",
                new Color[] { Color.valueOf("0B4A0B"), Color.valueOf("0B5C0B"), Color.valueOf("1BA61B") });
        colorMap.put("default_yellow",
                new Color[] { Color.valueOf("4A4A07"), Color.valueOf("5C5C0B"), Color.valueOf("A6A61B") });
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
        colorTable.put("u_color1", Color.valueOf("02394A"));
        colorTable.put("u_color2", Color.valueOf("043565"));
        colorTable.put("u_color3", Color.valueOf("5158BB"));
    }

    public static void startSpin() {
        Event startSpinning = new Event(TriggerType.EASE, false, false, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.8f, 0.5f, null));
        eventManager.addEvent(startSpinning.copy(), "background", false);
    }

    public static void rewindSpin() {
        Event stopSpinning = new Event(TriggerType.EASE, true, false, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", -5.0f, 0.2f, null));

        Event startSpinning = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.8f, 0.5f, null));

        eventManager.addEvent(stopSpinning.copy(), "background", false);
        eventManager.addEvent(startSpinning.copy(), "background", false);
    }

    public static void stopSpinning() {
        Event resetSpinTime = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinTime", 0.0f, 0.5f, null));
        eventManager.addEvent(resetSpinTime.copy(), "background", false);
    }

    // TODO: code the effect for the spin curve. CURRENTLY NOT WORKING!
    public static void resetSpinCurve() {
        Event resetSpinCurve = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinAmount", 0.14f, 0.5f, null));
        eventManager.addEvent(resetSpinCurve.copy(), "background", false);
    }

    public static void startSpinCurve() {
        Event startSpinCurve = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_spinAmount", 0.15f, 0.5f, null));
        eventManager.addEvent(startSpinCurve.copy(), "background", false);
    }

    // update single color
    public static void updateColor(String colorName, Map<String, Float> tempTable) {
        colorTable.get(colorName).set(tempTable.get("r"), tempTable.get("g"), tempTable.get("b"), tempTable.get("a"));
    }

    // update all colors

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

    public static void colorSwitch(Color[] colors, float duration) {
        tempTables.put("u_color1", colorLerp(colorTable.get("u_color1"), colors[0], duration));
        tempTables.put("u_color2", colorLerp(colorTable.get("u_color2"), colors[1], duration));
        tempTables.put("u_color3", colorLerp(colorTable.get("u_color3"), colors[2], duration));
    }

    public static void colorSwitch(Color[] colors) {
        colorSwitch(colors, 0.2f);
    }

    public static void updateColors() {
        for (Map.Entry<String, Map<String, Float>> entry : tempTables.entrySet()) {
            updateColor(entry.getKey(), entry.getValue());
        }
    }

    public static void setCamera(OrthographicCamera camera) {
        sharedCamera = camera;
    }

    public static void updateCameraShakePosition() {
        float shake = refTable.get("u_shake");

        float x_random = MathUtils.random(shake) - (shake / 2);
        float y_random = MathUtils.random(shake) - (shake / 2);

        // update the camera position around [-shake/2, shake/2] for x and y
        sharedCamera.position.set(sharedCamera.position.x + x_random, sharedCamera.position.y + y_random,
                sharedCamera.position.z);
        sharedCamera.update();
    }

    public static void screenShake() {
        Event shakeEvent = new Event(TriggerType.EASE, true, false, null, null, null, null,
                new Ease(EaseType.ELASTIC, refTable, "u_shake", 2.0f, 0.18f, null));

        Event stopShakeEvent = new Event(TriggerType.EASE, false, true, null, null, null, null,
                new Ease(EaseType.LERP, refTable, "u_shake", 0.0f, 0.18f, null));

        eventManager.addEvent(shakeEvent.copy(), "shake", true);
        eventManager.addEvent(stopShakeEvent.copy(), "shake", false);
        // event
    }

}
