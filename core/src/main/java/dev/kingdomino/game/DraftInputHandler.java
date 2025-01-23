package dev.kingdomino.game;

import dev.kingdomino.game.Event.TriggerType;

public class DraftInputHandler extends AbstractInputProcessor {
    private final EventManager eventManager = EventManager.getInstance();
    private final GameManager gameManager;
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
                while (true) {
                    selectionIndex = (selectionIndex + remainingDrafts - 1) % remainingDrafts;

                    if (!nextTurn.isSelected(selectionIndex))
                        break;
                }

                updated = true;
                break;
            case MOVE_DOWN:
                while (true) {
                    selectionIndex = (selectionIndex + 1) % remainingDrafts;

                    if (!nextTurn.isSelected(selectionIndex))
                        break;
                }

                updated = true;
                break;
            case SELECT_DOMINO:
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
}
