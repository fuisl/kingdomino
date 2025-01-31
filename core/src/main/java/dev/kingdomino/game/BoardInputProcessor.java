package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.BoardInputHandler.Action;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Handles input during the placement phase of the game. Only one should exist
 * at any given time,
 * however Singleton Pattern is actively discouraged due to unexpected behavior
 * in mobile platform,
 * thus make sure that there is no instance of this before generating a new one.
 * 
 * @author @fuisl
 * @version 1.0
 * 
 * refactored by @LunaciaDev
 */
public class BoardInputProcessor extends AbstractInputProcessor {
    private final BoardInputHandler boardInputHandler;

    /**
     * Constructs a new BoardInputProcessor.
     * 
     * @param boardInputHandler the board input handler instance
     */
    public BoardInputProcessor(BoardInputHandler boardInputHandler) {
        this.boardInputHandler = boardInputHandler;
    }

    /**
     * Handles key up events.
     * 
     * @param keycode the keycode of the key that was released
     * @return true if the event was handled
     */
    @Override
    public boolean keyUp(int keycode) {
        return boardInputHandler.keyUp(translateKeycodeToAction(keycode));
    }

    /**
     * Handles key down events.
     * 
     * @param keycode the keycode of the key that was pressed
     * @return true if the event was handled
     */
    @Override
    public boolean keyDown(int keycode) {
        GameManager.setInputDevice(InputDevice.KEYBOARD);
        return boardInputHandler.keyDown(translateKeycodeToAction(keycode));
    }

    /**
     * Translates a keycode to an action.
     * 
     * @param keycode the keycode to translate
     * @return the corresponding action
     */
    private Action translateKeycodeToAction(int keycode) {
        switch (keycode) {
            case Keys.W:
                return Action.MOVE_UP;
            case Keys.S:
                return Action.MOVE_DOWN;
            case Keys.A:
                return Action.MOVE_LEFT;
            case Keys.D:
                return Action.MOVE_RIGHT;
            case Keys.X:
                return Action.PLACE_DOMINO;
            case Keys.C:
                return Action.DISCARD_DOMINO;
            case Keys.E:
                return Action.ROTATE_CLOCKWISE;
            case Keys.Q:
                return Action.ROTATE_COUNTERCLOCKWISE;
            default:
                return Action.NONE; // cannot be null
        }
    }
}
