package dev.kingdomino.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.Event.TriggerType;

public class BoardInputProcessor implements InputProcessor {
    private EventManager eventManager = EventManager.getInstance();
    private GameManager gameManager;
    private Board board;
    private Domino currentDomino;

    public boolean updated;
    public boolean exit;
    public boolean valid;

    public BoardInputProcessor(GameManager gm) {
        this.gameManager = gm;
        this.board = gm.getBoard();
        this.currentDomino = gm.getCurrentDomino();
        this.updated = true;
        this.valid = true;
        this.exit = false;
    }

    public void update() {
        currentDomino = gameManager.getCurrentDomino();
    }

    @Override
    public boolean keyDown(int keycode) {
        update(); // update states

        Event e = null;
        switch (keycode) {
            case Keys.W: // 'w'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.moveDomino(Direction.UP),
                        null,
                        null,
                        null);
                break;
            case Keys.S: // 's'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.moveDomino(Direction.DOWN),
                        null,
                        null,
                        null);
                break;
            case Keys.A: // 'a'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.moveDomino(Direction.LEFT),
                        null,
                        null,
                        null);
                break;
            case Keys.D: // 'd'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.moveDomino(Direction.RIGHT),
                        null,
                        null,
                        null);
                break;
            case Keys.E: // 'e'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.rotateDomino(true),
                        null,
                        null,
                        null);
                break;
            case Keys.Q: // 'q'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> currentDomino.rotateDomino(false),
                        null,
                        null,
                        null);
                break;
            case Keys.X: // 'x'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> {
                            if (board.setDomino(currentDomino) == 0) {
                                valid = true;
                            } else {
                                eventManager.addEvent(invalidEffect.copy(), "base", false);
                                valid = false;
                            }
                        },
                        null,
                        null,
                        null);
                break;
            case Keys.C: // 'c'
                e = new Event(
                        TriggerType.IMMEDIATE,
                        false,
                        true,
                        null,
                        () -> exit = true,
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
        // returning true indicates the event was handled
    }

    Event invalidEffect = new Event(TriggerType.BEFORE, true, false, 0.5f, () -> {
        Domino temp = currentDomino.copy();
        Event blink_x = new Event(TriggerType.BEFORE, true, true, 0.5f, () -> {
            Tile invalidTile = new Tile(TerrainType.INVALID);
            Domino invalidDomino = new Domino(0, invalidTile, invalidTile,
                    new DominoController(currentDomino.getDominoController()));
            gameManager.setCurrentDomino(invalidDomino);
            updated = true;
        }, null, null, null);
        blink_x.name = "blink_x";

        Event blink_back = new Event(TriggerType.IMMEDIATE, false, true, null, () -> {
            gameManager.setCurrentDomino(temp);
            updated = true;
        }, null, null, null);
        blink_back.name = "blink_back";

        eventManager.addEvent(blink_x, "other", false);
        eventManager.addEvent(blink_back, "other", false);
    }, null, null, null);

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
