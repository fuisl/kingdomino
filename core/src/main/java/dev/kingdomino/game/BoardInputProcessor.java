package dev.kingdomino.game;

import com.badlogic.gdx.Input.Keys;

import dev.kingdomino.game.Event.TriggerType;

/**
 * Handles input during the placement phase of the game. Only one should exist at any given time,
 * however Singleton Pattern is actively discouraged due to unexpected behavior in mobile platform,
 * thus make sure that there is no instance of this before generating a new one.
 * 
 * @author fuisl
 */
public class BoardInputProcessor extends AbstractInputProcessor {
    private EventManager eventManager = EventManager.getInstance();
    private GameManager gameManager;
    private Board board;
    private Domino currentDomino;
    private Position[] tileBOffsets = {
        Direction.RIGHT.get(),
        Direction.DOWN.get(),
        Direction.LEFT.get(),
        Direction.UP.get()
    };

    public boolean updated;
    public boolean exit;
    public boolean valid;
    public boolean keylocked = false;

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
    public boolean keyDown(int keycode) {
        if (gameManager.getCurrentState() != GameManager.GameState.TURN_PLACING) {
            return false;
        }
        update(); // update states

        Event e = null;
        switch (keycode) {
            case Keys.W: {
                if (currentDomino.getPosTileA().y() == 0 || currentDomino.getPosTileB().y() == 0) return true;

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
            }
            case Keys.S: {
                if (currentDomino.getPosTileA().y() == 8 || currentDomino.getPosTileB().y() == 8) return true;

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
            }
            case Keys.A: {
                if (currentDomino.getPosTileA().x() == 0 || currentDomino.getPosTileB().x() == 0) return true;

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
            }
            case Keys.D: {
                if (currentDomino.getPosTileA().x() == 8 || currentDomino.getPosTileB().x() == 8) return true;

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
            }
            case Keys.E: {
                int rotationIndex = currentDomino.getDominoController().getRotationIndex();
                int newIndex = (rotationIndex + 1) % 4;
                int tileAx = currentDomino.getPosTileA().x();
                int tileAy = currentDomino.getPosTileA().y();
                
                if (tileAx + tileBOffsets[newIndex].x() > 8 || tileAx + tileBOffsets[newIndex].x() < 0) return true;
                if (tileAy + tileBOffsets[newIndex].y() > 8 || tileAy + tileBOffsets[newIndex].y() < 0) return true;

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
            }

            case Keys.Q: {
                int rotationIndex = currentDomino.getDominoController().getRotationIndex();
                int newIndex = (rotationIndex + 3) % 4;
                int tileAx = currentDomino.getPosTileA().x();
                int tileAy = currentDomino.getPosTileA().y();
                
                if (tileAx + tileBOffsets[newIndex].x() > 8 || tileAx + tileBOffsets[newIndex].x() < 0) return true;
                if (tileAy + tileBOffsets[newIndex].y() > 8 || tileAy + tileBOffsets[newIndex].y() < 0) return true;

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
            }

        case Keys.X: // 'x'
                if (keylocked) {
                    break;
                }
                e = new Event(
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
                break;
            case Keys.C: // 'c'
                e = new Event(
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
            keylocked = false;
        }, null, null, null);
        blink_back.name = "blink_back";

        eventManager.addEvent(blink_x, "other", false);
        eventManager.addEvent(blink_back, "other", false);
    }, null, null, null);
}
