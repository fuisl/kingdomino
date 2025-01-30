package dev.kingdomino.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameManager.GameState;

/**
 * A RenderManager specialize in handling the Control Hint at the bottom of
 * {@link GameScreen},
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class ControlHintManager extends Actor {
    // not extending AbstractRenderManager here as we do not need to render all 4
    // player at once.

    private GameManager gameManager;
    private Label controlHint;
    private GameState lastGameState;

    public ControlHintManager(GameManager gameManager, Label.LabelStyle bodyStyle) {
        this.gameManager = gameManager;
        this.controlHint = new Label("placeholder", bodyStyle);
        this.lastGameState = GameState.INIT;
        controlHint.setWrap(true);
        controlHint.setAlignment(Align.center);
    }

    public void setLayout(Table layout) {
        layout.add(controlHint).width(Value.percentWidth(0.95f, layout)).pad(5);
    }

    private boolean checkIdenticalGameState(GameState currentState) {
        if (lastGameState == currentState)
            return true;

        lastGameState = currentState;
        return false;
    }

    @Override
    public void act(float delta) {
        if (checkIdenticalGameState(gameManager.getCurrentState()))
            return;

        switch (gameManager.getCurrentState()) {
            case RESULTS:
                break;
            case TURN_CHOOSING:
                // TODO: add control hint for choosing domino
                controlHint.setText(
                        "[W, S] move, [X] confirm.\nThe position determine the play order next round.");
                break;
            case TURN_PLACING:
                controlHint.setText("[W, A, S, D] move the domino;\n[Q, E] rotate; [X] place; [C] discard.");
                break;
            case INIT:
            case SETUP:
            case GAME_OVER:
            case TURN_END:
            case TURN_START:
                break;
        }

        controlHint.pack();
    }
}
