package dev.kingdomino.effects;

import java.util.Map;

import com.badlogic.gdx.graphics.Color;

import dev.kingdomino.game.Ease;
import dev.kingdomino.game.Ease.EaseType;
import dev.kingdomino.game.Event;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.EventManager;

public class BackgroundManager {
    private static final Map<String, Float> refTable = BackgroundShader.refTable; // ref to BackgroundShader.refTable
    private static final Map<String, Color> colorTable = BackgroundShader.colorTable;
    private static final EventManager eventManager = EventManager.getInstance();

    static {
        // init refTable
        refTable.put("u_time", 0f); // control overall color bleeding. CURRENTLY UNUSED
        refTable.put("u_spinTime", 0.0f); // control spining. [-1, 1] should go with the spinAmount
        refTable.put("u_spinAmount", 0.18f); // control the shape of the spin. 0.2f is the sweet spot
        refTable.put("u_contrast", 1.5f); // control contrast

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
}
