package dev.kingdomino.game;

import dev.kingdomino.effects.AudioManager;
import dev.kingdomino.effects.AudioManager.SoundType;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Handles the input for the draft phase of the game.
 * 
 * This class listens for input events and translates them into game actions.
 * 
 * @see AbstractInputProcessor
 * @see GameManager
 * 
 * @author @fuisl
 * @version 1.0
 */
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

    /**
     * Enum representing possible actions during the draft phase.
     */
    public enum Action {
        MOVE_UP,
        MOVE_DOWN,
        SELECT_DOMINO,
        NONE
    }

    /**
     * Constructs a new DraftInputHandler.
     *
     * @param gm the GameManager instance
     */
    public DraftInputHandler(GameManager gm) {
        this.gameManager = gm;
        this.updated = true;
        this.exit = false;
        this.show = true;

    }

    /**
     * Resets the draft input handler for the next turn.
     */
    public void reset() {
        nextTurn = gameManager.getNextTurn();
        updated = true;
        exit = false;
        show = true;
        try {
            remainingDrafts = nextTurn.getDraft().length;

            selectionIndex = 0;

            while (nextTurn.isSelected(selectionIndex)) {
                selectionIndex = (selectionIndex + 1) % remainingDrafts;
            }
        } catch (NullPointerException e) {
        }
    }

    /**
     * Gets the current selection index.
     *
     * @return the selection index
     */
    public int getSelectionIndex() {
        return this.selectionIndex;
    }

    /**
     * Handles key up events.
     *
     * @param action the action to perform
     * @return true if the event was handled, false otherwise
     */
    public boolean keyUp(Action action) {
        eventManager.clearQueue("input", false);
        return true;
    }

    /**
     * Handles key down events.
     *
     * @param action the action to perform
     * @return true if the event was handled, false otherwise
     */
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

    /**
     * Creates an event for selecting a domino.
     *
     * @return the event
     */
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

    /**
     * Creates an event for moving up in the draft selection.
     *
     * @return the event
     */
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

    /**
     * Creates an event for moving down in the draft selection.
     *
     * @return the event
     */
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
