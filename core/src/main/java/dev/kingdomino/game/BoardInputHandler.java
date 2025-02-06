package dev.kingdomino.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.Controllers;

import dev.kingdomino.effects.AudioManager;
import dev.kingdomino.effects.BackgroundManager;
import dev.kingdomino.game.Event.TriggerType;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Handles input during the placement phase of the game. Only one should exist
 * at any given time,
 * however Singleton Pattern is actively discouraged due to unexpected behavior
 * in mobile platform,
 * thus make sure that there is no instance of this before generating a new one.
 * 
 * @author @fuisl
 * @version 1.0
 * 
 *          refactored by @LunaciaDev
 */
public class BoardInputHandler {
    private EventManager eventManager = EventManager.getInstance();
    private GameManager gameManager;
    private Board board;
    private Domino currentDomino;
    private final AudioManager audioManager = AudioManager.getInstance();
    private final Position[] tileBOffsets = {
            Direction.RIGHT.get(),
            Direction.DOWN.get(),
            Direction.LEFT.get(),
            Direction.UP.get()
    };

    /**
     * Indicates if the board has been updated.
     */
    public boolean updated;

    /**
     * Indicates if the exit condition has been met.
     */
    public boolean exit;

    /**
     * Indicates if the current state is valid.
     */
    public boolean valid;

    /**
     * Indicates if the keys are locked.
     */
    public boolean keylocked = false;

    public enum Action {
        MOVE_UP,
        MOVE_DOWN,
        MOVE_LEFT,
        MOVE_RIGHT,
        ROTATE_CLOCKWISE,
        ROTATE_COUNTERCLOCKWISE,
        PLACE_DOMINO,
        DISCARD_DOMINO,
        NONE
    }

    /**
     * Constructs a new BoardInputHandler.
     * 
     * @param gameManager the game manager instance
     */
    public BoardInputHandler(GameManager gameManager) {
        this.gameManager = gameManager;
        this.currentDomino = gameManager.getCurrentDomino();
        this.updated = true;
        this.valid = false;
        this.exit = false;
    }

    /**
     * Updates the current domino and board states.
     */
    public void update() {
        currentDomino = gameManager.getCurrentDomino();
        board = gameManager.getBoard();
    }

    /**
     * Resets the handler state.
     */
    public void reset() {
        this.updated = true;
        this.exit = false;
        this.valid = false;
    }

    /**
     * Handles key up events.
     * 
     * @param action the action to perform
     * @return true if the event was handled
     */
    public boolean keyUp(Action action) {
        eventManager.clearQueue("input", false);
        eventManager.clearQueue("sound", false);
        reset();
        return true;
    }

    /**
     * Handles key down events.
     * 
     * @param action the action to perform
     * @return true if the event was handled
     */
    public boolean keyDown(Action action) {
        if (gameManager.getCurrentState() != GameManager.GameState.TURN_PLACING) {
            return false;
        }
        update(); // update states

        Event e = null;
        Event soundEvent = null;
        Event vibration = null;

        switch (action) {
            case MOVE_UP: {
                soundEvent = createSoundEvent(AudioManager.SoundType.MOVING);
                if (canMove(Direction.UP)) {
                    e = createMoveEvent(Direction.UP);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case MOVE_DOWN: {
                soundEvent = createSoundEvent(AudioManager.SoundType.MOVING);
                if (canMove(Direction.DOWN)) {
                    e = createMoveEvent(Direction.DOWN);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case MOVE_LEFT: {
                soundEvent = createSoundEvent(AudioManager.SoundType.MOVING);
                if (canMove(Direction.LEFT)) {
                    e = createMoveEvent(Direction.LEFT);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case MOVE_RIGHT: {
                soundEvent = createSoundEvent(AudioManager.SoundType.MOVING);
                if (canMove(Direction.RIGHT)) {
                    e = createMoveEvent(Direction.RIGHT);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case ROTATE_CLOCKWISE: {
                soundEvent = createSoundEvent(AudioManager.SoundType.ROTATING);
                if (canRotate(true)) {
                    e = createRotateEvent(true);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case ROTATE_COUNTERCLOCKWISE: {
                soundEvent = createSoundEvent(AudioManager.SoundType.ROTATING);
                if (canRotate(false)) {
                    e = createRotateEvent(false);
                } else {
                    soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
                    vibration = createVibrationEvent();
                    BackgroundManager.screenShake();
                }
                break;
            }
            case PLACE_DOMINO: {
                BackgroundManager.screenShake();
                vibration = createVibrationEvent();
                soundEvent = createSoundEvent(AudioManager.SoundType.PLACING);
                // if (!keylocked) {
                    e = createPlaceDominoEvent();
                // }
                break;
            }
            case DISCARD_DOMINO: {
                BackgroundManager.screenShake();
                vibration = createVibrationEvent();
                soundEvent = createSoundEvent(AudioManager.SoundType.CANCEL);
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
        if (soundEvent != null) {
            eventManager.addEvent(soundEvent, "sound", false);
        }

        if (vibration != null && GameManager.currentInputDevice == InputDevice.CONTROLLER) {
            eventManager.addEvent(vibration, "effects", false);
        }

        return true; // returning true indicates the event was handled
    }

    /**
     * Checks if the domino can move in the specified direction.
     * 
     * @param direction the direction to move
     * @return true if the domino can move
     */
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

    /**
     * Checks if the domino can rotate.
     * 
     * @param clockwise true if rotating clockwise, false otherwise
     * @return true if the domino can rotate
     */
    private boolean canRotate(boolean clockwise) {
        int rotationIndex = currentDomino.getDominoController().getRotationIndex();
        int newIndex = clockwise ? (rotationIndex + 1) % 4 : (rotationIndex + 3) % 4;
        int tileAx = currentDomino.getPosTileA().x();
        int tileAy = currentDomino.getPosTileA().y();

        return tileAx + tileBOffsets[newIndex].x() >= 0 && tileAx + tileBOffsets[newIndex].x() <= 8
                && tileAy + tileBOffsets[newIndex].y() >= 0 && tileAy + tileBOffsets[newIndex].y() <= 8;
    }

    /**
     * Creates a move event.
     * 
     * @param direction the direction to move
     * @return the move event
     */
    private Event createMoveEvent(Direction direction) {
        float moveCooldown = 0.0f;
        if (GameManager.currentInputDevice == InputDevice.CONTROLLER) {
            moveCooldown = 0.15f;
        }
        return new Event(
                TriggerType.BEFORE,
                true,
                true,
                moveCooldown,
                () -> {
                    if (canMove(direction)) {
                        currentDomino.moveDomino(direction);
                    }
                    // eventManager.addEvent(createMoveEvent(direction).copy(), "input", false);
                },
                null,
                null,
                null);
    }

    /**
     * Creates a rotate event.
     * 
     * @param clockwise true if rotating clockwise, false otherwise
     * @return the rotate event
     */
    private Event createRotateEvent(boolean clockwise) {
        return new Event(
                TriggerType.IMMEDIATE,
                false,
                false,
                null,
                () -> currentDomino.rotateDomino(clockwise),
                null,
                null,
                null);
    }

    /**
     * Creates a place domino event.
     * 
     * @return the place domino event
     */
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
                        audioManager.playSound(AudioManager.SoundType.HIGHLIGHT);
                        eventManager.addEvent(invalidEffect.copy(), "input", false);
                        valid = false;
                        // keylocked = true;
                    }
                },
                null,
                null,
                null);
    }

    /**
     * Creates a discard domino event.
     * 
     * @return the discard domino event
     */
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

    /**
     * Creates a sound event.
     * 
     * @param soundType the type of sound to play
     * @return the sound event
     */
    private Event createSoundEvent(AudioManager.SoundType soundType) {
        float moveCooldown = 0.0f;
        if (GameManager.currentInputDevice == InputDevice.CONTROLLER) {
            moveCooldown = 0.15f;
        }
        return new Event(
                TriggerType.BEFORE,
                true,
                true,
                moveCooldown,
                () -> audioManager.playSound(soundType),
                null,
                null,
                null);
    }

    private Event createVibrationEvent() {
        return new Event(
                TriggerType.IMMEDIATE,
                false,
                true,
                0.5f,
                () -> {
                    Controller controller = Controllers.getCurrent();
                    if (controller != null) {
                        controller.startVibration(100, 1f);
                    }
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
            // keylocked = false;
        }, null, null, null);
        blink_back.name = "blink_back";

        eventManager.addEvent(blink_x, "other", false);
        eventManager.addEvent(blink_back, "other", false);
    }, null, null, null);
}
