package dev.kingdomino.game;

import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;

public abstract class AbstractController implements ControllerListener {

    @Override
    public void connected(Controller controller) {
        System.out.println("Controller connected: " + controller.getName());
    }

    @Override
    public void disconnected(Controller controller) {
        System.out.println("Controller disconnected: " + controller.getName());
    }

    @Override
    public boolean buttonDown(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean buttonUp(Controller controller, int buttonCode) {
        return false;
    }

    @Override
    public boolean axisMoved(Controller controller, int axisCode, float value) {
        return false;
    }
}
