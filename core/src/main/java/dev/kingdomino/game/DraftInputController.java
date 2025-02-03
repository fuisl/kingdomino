package dev.kingdomino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;

import dev.kingdomino.game.DraftInputHandler.Action;
import dev.kingdomino.game.GameManager.InputDevice;

/**
 * Handles input from a game controller for the draft phase of the game.
 * 
 * This class listens for controller input events and translates them into game
 * actions.
 *
 * @see AbstractController
 * @see DraftInputHandler
 * @see Controller
 * 
 * @author fuisl
 * @version 1.0
 */
public class DraftInputController extends AbstractController {
    private final DraftInputHandler draftInputHandler;
    private ControllerMapping map;

    private static final float DEAD_ZONE = 0.995f;
    private float axisY = 0f; // joystickYLeft

    /**
     * Constructs a new DraftInputController.
     *
     * @param draftInputHandler the handler for draft input actions
     */
    public DraftInputController(DraftInputHandler draftInputHandler) {
        this.draftInputHandler = draftInputHandler;
    }

    /**
     * Called when a controller is connected.
     *
     * @param controller the connected controller
     */
    @Override
    public void connected(Controller controller) {
        map = controller.getMapping();
        Gdx.app.log("Controller", "Connected: " + controller.getName());
    }

    /**
     * Called when a controller is disconnected.
     *
     * @param controller the disconnected controller
     */
    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log("Controller", "Disconnected: " + controller.getName());
    }

    /**
     * Called when an axis on the controller is moved.
     *
     * @param controller the controller
     * @param axisCode   the axis code
     * @param value      the axis value
     * @return true if the axis movement was handled, false otherwise
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        map = controller.getMapping();

        if (axisCode == map.axisLeftY || axisCode == map.axisRightY) {
            axisY = -value; // Update Y-axis value (inverted)
        } else {
            return false; // Unhandled axis
        }

        // Determine action based on the current axis value
        Action action = translateAxisToAction(axisY);

        if (action != Action.NONE) {
            draftInputHandler.keyDown(action); // Trigger movement
        } else {
            draftInputHandler.keyUp(action); // Stop movement
        }

        return true;
    }

    /**
     * Called when a button on the controller is pressed.
     *
     * @param controller the controller
     * @param buttonCode the button code
     * @return true if the button press was handled, false otherwise
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        map = controller.getMapping();
        Action action = translateButtonToAction(buttonCode);
        if (action != Action.NONE) {
            draftInputHandler.keyDown(action);
        }
        return true;
    }

    /**
     * Called when a button on the controller is released.
     *
     * @param controller the controller
     * @param buttonCode the button code
     * @return true if the button release was handled, false otherwise
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        // map = controller.getMapping();
        // Action action = translateButtonToAction(buttonCode);
        // if (action != Action.NONE) {
        // draftInputHandler.keyUp(action);
        // }
        draftInputHandler.keyUp(Action.NONE);
        return true;
    }

    /**
     * Translates an axis value to a corresponding action.
     *
     * @param axisY the Y-axis value
     * @return the corresponding action
     */
    private Action translateAxisToAction(float axisY) {
        // Handle dead zone
        if (Math.abs(axisY) < DEAD_ZONE) {
            return Action.NONE; // Ignore small inputs
        }

        // Vertical movement
        if (axisY > DEAD_ZONE) {
            return Action.MOVE_UP;
        } else if (axisY < -DEAD_ZONE) {
            return Action.MOVE_DOWN;
        }

        return Action.NONE;
    }

    /**
     * Translates a button code to a corresponding action.
     *
     * @param buttonCode the button code
     * @return the corresponding action
     */
    private Action translateButtonToAction(int buttonCode) {
        if (buttonCode == map.buttonA || buttonCode == map.buttonX || buttonCode == map.buttonLeftStick) {
            return Action.SELECT_DOMINO;
        } else if (buttonCode == map.buttonDpadUp) {
            return Action.MOVE_UP;
        } else if (buttonCode == map.buttonDpadDown) {
            return Action.MOVE_DOWN;
        } else {
            return Action.NONE;
        }
    }
}
