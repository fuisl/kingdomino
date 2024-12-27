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

        switch (keycode) {
            case Keys.W: // 'w'
                currentDomino.moveDomino(Direction.UP);
                updated = true;
                break;
            case Keys.S: // 's'
                currentDomino.moveDomino(Direction.DOWN);
                updated = true;
                break;
            case Keys.A: // 'a'
                currentDomino.moveDomino(Direction.LEFT);
                updated = true;
                break;
            case Keys.D: // 'd'
                currentDomino.moveDomino(Direction.RIGHT);
                updated = true;
                break;
            case Keys.E: // 'e'
                currentDomino.rotateDomino(true);
                updated = true;
                break;
            case Keys.Q: // 'q'
                currentDomino.rotateDomino(false);
                updated = true;
                break;
            case Keys.X: // 'x'
                if (board.setDomino(currentDomino) == 0) {
                    valid = true;
                    updated = true;
                    break;
                } else {
                    eventManager.addEvent(invalidEffect.copy(), "other", false);
                    valid = false;
                    updated = true;
                    break;
                }
            case Keys.C: // 'c'
                System.out.println("Exiting game or returning to menu");
                exit = true;
                break;
            default:
                break;
        }
        return true;
        // returning true indicates the event was handled
    }

    Event invalidEffect = new Event(TriggerType.IMMEDIATE, false, false, null, () -> {
        Domino temp = currentDomino.copy();
        Event blink_x = new Event(TriggerType.BEFORE, true, true, 1f, () -> {
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

        // eventManager.addEvent(blink_x, "other", false);
        // eventManager.addEvent(blink_back, "other", false);
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
