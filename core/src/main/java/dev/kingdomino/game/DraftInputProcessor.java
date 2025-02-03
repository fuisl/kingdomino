package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.DraftInputHandler.Action;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Processes input for the draft phase of the game.
 * 
 * This class listens for input events and translates them into game actions.
 * 
 * @see AbstractInputProcessor
 * @see DraftInputHandler
 * 
 * @author @fuisl
 * @version 1.0
 */
public class DraftInputProcessor extends AbstractInputProcessor {
    private final DraftInputHandler draftInputHandler;

    /**
     * Constructs a new DraftInputProcessor.
     *
     * @param draftInputHandler the DraftInputHandler instance
     */
    public DraftInputProcessor(DraftInputHandler draftInputHandler) {
        this.draftInputHandler = draftInputHandler;
    }

    /**
     * Handles key down events.
     *
     * @param keycode the key code of the pressed key
     * @return true if the event was handled, false otherwise
     */
    @Override
    public boolean keyDown(int keycode) {
        GameManager.setInputDevice(InputDevice.KEYBOARD);
        return draftInputHandler.keyDown(translateKeycodeToAction(keycode));
    }

    /**
     * Translates a key code to a draft action.
     *
     * @param keycode the key code
     * @return the corresponding draft action
     */
    private Action translateKeycodeToAction(int keycode) {
        switch (keycode) {
            case Keys.LEFT:
            case Keys.UP:
            case Keys.W:
                return Action.MOVE_UP;
            case Keys.RIGHT:
            case Keys.DOWN:
            case Keys.S:
                return Action.MOVE_DOWN;
            case Keys.ENTER:
            case Keys.X:
                return Action.SELECT_DOMINO;
            default:
                return Action.NONE;
        }
    }
}