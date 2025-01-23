package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.DraftInputHandler.Action;
import dev.kingdomino.game.GameManager.InputDevice;

public class DraftInputProcessor extends AbstractInputProcessor {
    private final DraftInputHandler draftInputHandler;

    public DraftInputProcessor(DraftInputHandler draftInputHandler) {
        this.draftInputHandler = draftInputHandler;
    }

    @Override
    public boolean keyDown(int keycode) {
        GameManager.setInputDevice(InputDevice.KEYBOARD);
        return draftInputHandler.keyDown(translateKeycodeToAction(keycode));
    }

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