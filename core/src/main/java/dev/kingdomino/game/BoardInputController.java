package dev.kingdomino.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerMapping;

import dev.kingdomino.game.BoardInputHandler.Action;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.game.GameManager.InputDevice;

public class BoardInputController extends AbstractController {
    private final BoardInputHandler boardInputHandler;
    private ControllerMapping map;

    private static final float DEAD_ZONE = 0.925f;
    private float axisX = 0f; // joystickXLeft
    private float axisY = 0f; // joystickYLeft

    public BoardInputController(BoardInputHandler boardInputHandler) {
        this.boardInputHandler = boardInputHandler;
    }

    @Override
    public void connected(Controller controller) {
        map = controller.getMapping();
        GameManager.setInputDevice(InputDevice.CONTROLLER);
        Gdx.app.log("Controller", "Connected: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        GameManager.setInputDevice(InputDevice.KEYBOARD);
        Gdx.app.log("Controller", "Disconnected: " + controller.getName());
    }

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
