package dev.kingdomino.game;

import dev.kingdomino.effects.AudioManager;
import dev.kingdomino.effects.AudioManager.SoundType;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.GameManager.InputDevice;

public class DraftInputHandler extends AbstractInputProcessor {
    private final EventManager eventManager = EventManager.getInstance();
    private final GameManager gameManager;
    private final AudioManager audioManager = AudioManager.getInstance();
    private Turn nextTurn;
    public int selectionIndex;
    public int remainingDrafts;

    public boolean updated;
    public boolean exit;
    public boolean show;

    public enum Action {
        MOVE_UP,
        MOVE_DOWN,
        SELECT_DOMINO,
        NONE
    }

    public DraftInputHandler(GameManager gm) {
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

    public boolean keyUp(Action action) {
        eventManager.clearQueue("input", false);
        return true;
    }

    public boolean keyDown(Action action) {
        if (gameManager.getCurrentState() == GameManager.GameState.TURN_START) {
            show = false;
            return true;
        }
        if (gameManager.getCurrentState() != GameManager.GameState.TURN_CHOOSING) {
            updated = true;
            return false;
        }
        Event e = null;

        switch (action) {
            case MOVE_UP:
                audioManager.playSound(SoundType.SELECTING);
                e = createMoveUpEvent();
                break;
            case MOVE_DOWN:
                audioManager.playSound(SoundType.SELECTING);
                e = createMoveDownEvent();
                break;
            case SELECT_DOMINO:
                audioManager.playSound(SoundType.CONFIRMSELECTING);
                e = createSelectDominoEvent();
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

    private Event createSelectDominoEvent() {
        return new Event(
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
    }

    private Event createMoveUpEvent() {
        float delay = 0.0f;
        if (GameManager.currentInputDevice == InputDevice.CONTROLLER) {
            delay = 0.1f;
        }
        return new Event(
                TriggerType.BEFORE,
                true,
                true,
                delay,
                () -> {
                    while (true) {
                        selectionIndex = (selectionIndex + remainingDrafts - 1) % remainingDrafts;

                        if (!nextTurn.isSelected(selectionIndex))
                            break;
                    }
                    updated = true;
                },
                null,
                null,
                null);
    }

    private Event createMoveDownEvent() {
        float delay = 0.0f;
        if (GameManager.currentInputDevice == InputDevice.CONTROLLER) {
            delay = 0.1f;
        }
        return new Event(
                TriggerType.BEFORE,
                true,
                true,
                delay,
                () -> {
                    while (true) {
                        selectionIndex = (selectionIndex + 1) % remainingDrafts;

                        if (!nextTurn.isSelected(selectionIndex))
                            break;
                    }
                    updated = true;
                },
                null,
                null,
                null);
    }
}
