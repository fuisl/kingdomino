package dev.kingdomino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;

import dev.kingdomino.game.BoardInputHandler.Action;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Controller class for handling board input in the game.
 * 
 * This class listens for controller input events and translates them into game actions.
 * 
 * @see AbstractController
 * @see BoardInputHandler
 * @see Controller
 * 
 * @author @fuisl
 * @version 1.0
 */
public class BoardInputController extends AbstractController {
    private final BoardInputHandler boardInputHandler;
    private ControllerMapping map;

    private static final float DEAD_ZONE = 0.925f;
    private float axisX = 0f; // joystickXLeft
    private float axisY = 0f; // joystickYLeft

    /**
     * Constructor for BoardInputController.
     *
     * @param boardInputHandler the handler for board input actions
     */
    public BoardInputController(BoardInputHandler boardInputHandler) {
        this.boardInputHandler = boardInputHandler;
    }

    /**
     * Called when a controller is connected.
     *
     * @param controller the connected controller
     */
    @Override
    public void connected(Controller controller) {
        map = controller.getMapping();
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        Gdx.app.log("Controller", "Connected: " + controller.getName());
    }

    /**
     * Called when a controller is disconnected.
     *
     * @param controller the disconnected controller
     */
    @Override
    public void disconnected(Controller controller) {
        GameManager.setInputDevice(InputDevice.KEYBOARD);
        Gdx.app.log("Controller", "Disconnected: " + controller.getName());
    }

    /**
     * Called when an axis on the controller is moved.
     *
     * @param controller the controller
     * @param axisCode the axis code
     * @param value the value of the axis
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        if (GameManager.currentState != GameState.TURN_PLACING) {
            return false; // Ignore input when not playing
        }
        map = controller.getMapping();

        if (axisCode == map.axisLeftX || axisCode == map.axisRightX) {
            axisX = value; // Update X-axis value
        } else if (axisCode == map.axisLeftY || axisCode == map.axisRightY) {
            axisY = -value; // Update Y-axis value (inverted)
        } else {
            return false; // Unhandled axis
        }

        // Determine action based on the current axis values
        Action action = translateAxisToAction(axisX, axisY);

        if (action != Action.NONE) {
            return boardInputHandler.keyDown(action); // Trigger movement
        } else {
            return boardInputHandler.keyUp(action); // Stop movement
        }
    }

    /**
     * Called when a button on the controller is pressed.
     *
     * @param controller the controller
     * @param buttonCode the button code
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        if (GameManager.currentState != GameState.TURN_PLACING) {
            return false; // Ignore input when not playing
        }

        map = controller.getMapping();
        Action action = translateButtonToAction(buttonCode);
        if (action != Action.NONE) {
            boardInputHandler.keyDown(action);
        }
        return true;
    }

    /**
     * Called when a button on the controller is released.
     *
     * @param controller the controller
     * @param buttonCode the button code
     * @return true if the input was handled, false otherwise
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        if (GameManager.currentState != GameState.TURN_PLACING) {
            return false; // Ignore input when not playing
        }

        map = controller.getMapping();
        Action action = translateButtonToAction(buttonCode);
        if (action != Action.NONE) {
            boardInputHandler.keyUp(action);
        }
        return true;
    }

    /**
     * Translates axis movements to game actions.
     *
     * @param axisX the X-axis value
     * @param axisY the Y-axis value
     * @return the corresponding action
     */
    private Action translateAxisToAction(float axisX, float axisY) {
        // Handle dead zone
        if (Math.sqrt(axisX * axisX + axisY * axisY) < DEAD_ZONE) {
            return Action.NONE; // Ignore small inputs
        }

        // Prioritize stronger axis
        if (Math.abs(axisX) > Math.abs(axisY)) {
            // Horizontal movement
            if (axisX > DEAD_ZONE) {
                return Action.MOVE_RIGHT;
            } else if (axisX < -DEAD_ZONE) {
                return Action.MOVE_LEFT;
            }
        } else {
            // Vertical movement
            if (axisY > DEAD_ZONE) {
                return Action.MOVE_UP;
            } else if (axisY < -DEAD_ZONE) {
                return Action.MOVE_DOWN;
            }
        }

        return Action.NONE;
    }

    /**
     * Translates button presses to game actions.
     *
     * @param buttonCode the button code
     * @return the corresponding action
     */
    private Action translateButtonToAction(int buttonCode) {
        if (buttonCode == map.buttonA) {
            return Action.PLACE_DOMINO;
        } else if (buttonCode == map.buttonB || buttonCode == map.buttonLeftStick) {
            return Action.DISCARD_DOMINO;
        } else if (buttonCode == map.buttonX || buttonCode == map.buttonR1) {
            return Action.ROTATE_CLOCKWISE;
        } else if (buttonCode == map.buttonY || buttonCode == map.buttonL1) {
            return Action.ROTATE_COUNTERCLOCKWISE;
        } else if (buttonCode == map.buttonDpadUp) {
            return Action.MOVE_UP;
        } else if (buttonCode == map.buttonDpadDown) {
            return Action.MOVE_DOWN;
        } else if (buttonCode == map.buttonDpadLeft) {
            return Action.MOVE_LEFT;
        } else if (buttonCode == map.buttonDpadRight) {
            return Action.MOVE_RIGHT;
        }
        return Action.NONE;
    }
}
