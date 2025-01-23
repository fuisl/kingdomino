package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.Event.TriggerType;

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
    private EventManager eventManager = EventManager.getInstance();
    private GameManager gameManager;
    private Board board;
    private Domino currentDomino;
    private final Position[] tileBOffsets = {
            Direction.RIGHT.get(),
            Direction.DOWN.get(),
            Direction.LEFT.get(),
            Direction.UP.get()
    };

    public boolean updated;
    public boolean exit;
    public boolean valid;
    public boolean keylocked = false;

    private final float moveCooldown = 0.2f; // how many seconds between moves

    public BoardInputProcessor(GameManager gameManager) {
        this.gameManager = gameManager;
        this.currentDomino = gameManager.getCurrentDomino();
        this.updated = true;
        this.valid = false;
        this.exit = false;
    }

    public void update() {
        currentDomino = gameManager.getCurrentDomino();
        board = gameManager.getBoard();
    }

    public void reset() {
        this.updated = true;
        this.exit = false;
        this.valid = false;
    }

    @Override
    public boolean keyUp(int keycode) {
        eventManager.clearQueue("input", false);
        return true;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (gameManager.getCurrentState() != GameManager.GameState.TURN_PLACING) {
            return false;
        }
        update(); // update states

        Event e = null;
        switch (keycode) {
            case Keys.W: {
                if (canMove(Direction.UP)) {
                    e = createMoveEvent(Direction.UP);
                }
                break;
            }
            case Keys.S: {
                if (canMove(Direction.DOWN)) {
                    e = createMoveEvent(Direction.DOWN);
                }
                break;
            }
            case Keys.A: {
                if (canMove(Direction.LEFT)) {
                    e = createMoveEvent(Direction.LEFT);
                }
                break;
            }
            case Keys.D: {
                if (canMove(Direction.RIGHT)) {
                    e = createMoveEvent(Direction.RIGHT);
                }
                break;
            }
            case Keys.E: {
                if (canRotate(true)) {
                    e = createRotateEvent(true);
                }
                break;
            }
            case Keys.Q: {
                if (canRotate(false)) {
                    e = createRotateEvent(false);
                }
                break;
            }
            case Keys.X: { // 'x'
                if (!keylocked) {
                    e = createPlaceDominoEvent();
                }
                break;
            }
            case Keys.C: { // 'c'
                e = createDiscardDominoEvent();
                break;
            }
            default:
                break;
        }

        if (e != null) {
            eventManager.addEvent(e.copy(), "input", false);
            updated = true;
        }

        return true; // returning true indicates the event was handled
    }

    private boolean canMove(Direction direction) {
        int tileAx = currentDomino.getPosTileA().x();
        int tileAy = currentDomino.getPosTileA().y();
        int tileBx = currentDomino.getPosTileB().x();
        int tileBy = currentDomino.getPosTileB().y();

        switch (direction) {
            case UP:
                return tileAy > 0 && tileBy > 0;
            case DOWN:
                return tileAy < 8 && tileBy < 8;
            case LEFT:
                return tileAx > 0 && tileBx > 0;
            case RIGHT:
                return tileAx < 8 && tileBx < 8;
            default:
                return false;
        }
    }

    private boolean canRotate(boolean clockwise) {
        int rotationIndex = currentDomino.getDominoController().getRotationIndex();
        int newIndex = clockwise ? (rotationIndex + 1) % 4 : (rotationIndex + 3) % 4;
        int tileAx = currentDomino.getPosTileA().x();
        int tileAy = currentDomino.getPosTileA().y();

        return tileAx + tileBOffsets[newIndex].x() >= 0 && tileAx + tileBOffsets[newIndex].x() <= 8
                && tileAy + tileBOffsets[newIndex].y() >= 0 && tileAy + tileBOffsets[newIndex].y() <= 8;
    }

    private Event createMoveEvent(Direction direction) {
        return new Event(
                TriggerType.BEFORE,
                true,
                true,
                moveCooldown,
                () -> {
                    if (canMove(direction)) {
                        currentDomino.moveDomino(direction);
                    }
                    eventManager.addEvent(createMoveEvent(direction).copy(), "input", false);
                },
                null,
                null,
                null);
    }

    private Event createRotateEvent(boolean clockwise) {
        return new Event(
                TriggerType.IMMEDIATE,
                false,
                true,
                null,
                () -> currentDomino.rotateDomino(clockwise),
                null,
                null,
                null);
    }

    private Event createPlaceDominoEvent() {
        return new Event(
                TriggerType.IMMEDIATE,
                false,
                true,
                null,
                () -> {
                    if (board.setDomino(currentDomino) == 0) {
                        valid = true;
                        exit = true;
                    } else {
                        eventManager.addEvent(invalidEffect.copy(), "base", false);
                        valid = false;
                        keylocked = true;
                    }
                },
                null,
                null,
                null);
    }

    private Event createDiscardDominoEvent() {
        return new Event(
                TriggerType.IMMEDIATE,
                false,
                true,
                null,
                () -> {
                    exit = true;
                    valid = true;
                },
                null,
                null,
                null);
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
            keylocked = false;
        }, null, null, null);
        blink_back.name = "blink_back";

        eventManager.addEvent(blink_x, "other", false);
        eventManager.addEvent(blink_back, "other", false);
    }, null, null, null);
}
