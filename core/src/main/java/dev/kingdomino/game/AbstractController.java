package dev.kingdomino.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

/**
 * Abstract base class for handling controller events.
 * Implements the {@link ControllerListener} interface.
 * 
 * @author @fuisl
 * @version 1.0
 */
public abstract class AbstractController implements ControllerListener {

    /**
     * Called when a controller is connected.
     *
     * @param controller the connected controller
     */
    @Override
    public void connected(Controller controller) {
        System.out.println("Controller connected: " + controller.getName());
    }

    /**
     * Called when a controller is disconnected.
     *
     * @param controller the disconnected controller
     */
    @Override
    public void disconnected(Controller controller) {
        System.out.println("Controller disconnected: " + controller.getName());
    }

    /**
     * Called when a button is pressed on the controller.
     *
     * @param controller the controller
     * @param buttonCode the code of the button that was pressed
     * @return false to indicate the event was not handled
     */
    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    /**
     * Called when a button is released on the controller.
     *
     * @param controller the controller
     * @param buttonCode the code of the button that was released
     * @return false to indicate the event was not handled
     */
    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    /**
     * Called when an axis is moved on the controller.
     *
     * @param controller the controller
     * @param axisCode the code of the axis that was moved
     * @param value the value of the axis
     * @return false to indicate the event was not handled
     */
    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
