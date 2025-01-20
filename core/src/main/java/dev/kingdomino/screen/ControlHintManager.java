package dev.kingdomino.screen;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import dev.kingdomino.game.GameManager;

/**
 * A RenderManager specialize in handling the Control Hint at the bottom of {@link GameScreen},
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class ControlHintManager extends Actor {
    // not extending AbstractRenderManager here as we do not need to render all 4 player at once.

    private GameManager gameManager;
    private Label controlHint;

    public ControlHintManager(GameManager gameManager, Skin skin) {
        this.gameManager = gameManager;
        this.controlHint = new Label("placeholder", skin);
    }

    public void setLayout(Table layout) {
        layout.add(controlHint).height(Value.percentHeight(0.05f, layout));
    }

    @Override
    public void act(float delta) {
        switch (gameManager.getCurrentState()) {
            case RESULTS:
                break;
            case TURN_CHOOSING:
                controlHint.setText("Use W, S to choose the domino to play on next round, X to confirm. The position determine the play order next round.");
                break;
            case TURN_PLACING:
                controlHint.setText("Use W, A, S, D to move the domino; Q, E to rotate; X to place; C to discard.");
                break;

            case INIT:
            case SETUP:
            case GAME_OVER:
            case TURN_END:
            case TURN_START:
                break;
        }
    }
}
