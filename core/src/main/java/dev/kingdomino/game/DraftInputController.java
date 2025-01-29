package dev.kingdomino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;

import dev.kingdomino.game.DraftInputHandler.Action;
import dev.kingdomino.game.GameManager.InputDevice;

public class DraftInputController extends AbstractController {
    private final DraftInputHandler draftInputHandler;
    private ControllerMapping map;

    private static final float DEAD_ZONE = 0.995f;
    private float axisY = 0f; // joystickYLeft

    public DraftInputController(DraftInputHandler draftInputHandler) {
        this.draftInputHandler = draftInputHandler;
    }

    @Override
    public void connected(Controller controller) {
        map = controller.getMapping();
        Gdx.app.log("Controller", "Connected: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        Gdx.app.log("Controller", "Disconnected: " + controller.getName());
    }

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
