package dev.kingdomino.game;

import com.badlogic.gdx.InputProcessor;

/**
 * Implement {@link InputProcessor} with all input event being rejected. Extend this class and override
 * specific input that will be handled instead of implementing {@link InputProcessor} directly.
 * 
 * @author LunaciaDev
 */
public abstract class AbstractInputProcessor implements InputProcessor {
    @Override
    public boolean keyDown(int keycode) {return false;}

    @Override
    public boolean keyUp(int keycode) {return false;}

    @Override
    public boolean keyTyped(char character) {return false;}

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {return false;}

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {return false;}

    @Override
    public boolean mouseMoved(int screenX, int screenY) {return false;}

    @Override
    public boolean scrolled(float amountX, float amountY) {return false;}
}
