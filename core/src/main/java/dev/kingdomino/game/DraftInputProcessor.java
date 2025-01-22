package dev.kingdomino.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.Event.TriggerType;

public class DraftInputProcessor implements InputProcessor {
    private EventManager eventManager = EventManager.getInstance();
    private GameManager gameManager;
    private Turn nextTurn;
    public int selectionIndex;
    public int remainingDrafts;

    public boolean updated;
    public boolean exit;
    public boolean show;

    public DraftInputProcessor(GameManager gm) {
        this.gameManager = gm;
        this.updated = true;
        this.exit = false;
        this.show = true;
    }

    public void reset() {
        nextTurn = gameManager.getNextTurn();
        remainingDrafts = nextTurn.getDraft().length;
        updated = true;
        exit = false;
        show = true;
        
        selectionIndex = 0;
        while (nextTurn.isSelected(selectionIndex)) {
            selectionIndex = (selectionIndex + 1) % remainingDrafts;
        }
    }

    public int getSelectionIndex() {
        return this.selectionIndex;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (gameManager.getCurrentState() == GameManager.GameState.TURN_START) {
            show = false;
            return true;
        }
        if (gameManager.getCurrentState() != GameManager.GameState.TURN_CHOOSING) {
            updated = true;
            return false;
        }
        Event e = null;

        switch (keycode) {
            case Keys.LEFT:
            case Keys.UP:
            case Keys.W:
                while (true) {
                    selectionIndex = (selectionIndex + remainingDrafts - 1) % remainingDrafts;

                    if (!nextTurn.isSelected(selectionIndex)) break;
                }
                
                updated = true;
                break;
            case Keys.RIGHT:
            case Keys.DOWN:
            case Keys.S:
                while (true) {
                    selectionIndex = (selectionIndex + 1) % remainingDrafts;

                    if (!nextTurn.isSelected(selectionIndex)) break;
                }

                updated = true;
                break;
            case Keys.ENTER:
            case Keys.X:
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> {
                            gameManager.selectDomino(selectionIndex);
                            exit = true;
                        },
                        null,
                        null,
                        null);
                break;
            default:
                break;
        }

        if (e != null) {
            eventManager.addEvent(e, "base", false);
            updated = true;
        }

        return true;

    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchCancelled(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        return false;
    }
}