package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.BoardInputHandler.Action;

/**
 * Handles input during the placement phase of the game. Only one should exist
 * at any given time,
 * however Singleton Pattern is actively discouraged due to unexpected behavior
 * in mobile platform,
 * thus make sure that there is no instance of this before generating a new one.
 * 
 * @author fuisl
 */
public class BoardInputProcessor extends AbstractInputProcessor {
    private final BoardInputHandler boardInputHandler;

    public BoardInputProcessor(BoardInputHandler boardInputHandler) {
        this.boardInputHandler = boardInputHandler;
    }

    @Override
    public boolean keyUp(int keycode) {
        return boardInputHandler.keyUp(translateKeycodeToAction(keycode));
    }

    @Override
    public boolean keyDown(int keycode) {
        return boardInputHandler.keyDown(translateKeycodeToAction(keycode));
    }

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
                return Action.NONE;  // cannot be null
        }
    }
}
