package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

/**
 * A RenderManager specialize in showing the turn order on the left side,
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class TurnOrderRenderManager extends Actor {
    protected GameManager gameManager;
    protected PlayerIconActor[] playerIconActors;
    protected int kingCount;
    protected Skin skin;

    public TurnOrderRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        this.gameManager = gameManager;
        this.kingCount = gameManager.getKingCount();
        this.playerIconActors = new PlayerIconActor[5];
        this.skin = skin;
        
        for (int i = 0; i < 5; i++) {
            playerIconActors[i] = new PlayerIconActor(kingAvatar);
        }
    }

    public void setLayout(Table layout) {
        layout.add(new Label("Turn Order", skin)).colspan(kingCount);
        layout.row();
        for (int i = 0; i < 5; i++) {
            layout.add(generateContainer(playerIconActors[i]))
                .expand()
                .fill();

            layout.row();
        }
    }

    protected Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    @Override
    public void act(float delta) {
        King[] currentTurnOrder = gameManager.getCurrentTurn().getKings();
        int currentTurnIndex = gameManager.getCurrentTurn().getCurrentIndex();
        King[] nextTurnOrder = gameManager.getNextTurn().getKings();
        int nextTurnIndex = 0;

        // TODO figure out how to animate this mess
        for (int i = 0; i < 5; i++) {
            if (currentTurnIndex < kingCount) {
                playerIconActors[i].setKingID(currentTurnOrder[currentTurnIndex].getId());
                currentTurnIndex++;
                continue;
            }

            while (nextTurnOrder[nextTurnIndex] == null) {
                nextTurnIndex++;

                if (nextTurnIndex == kingCount) {
                    for (; i < 5; i++) {
                        playerIconActors[i].setKingID(-1);
                    }
                    
                    return;
                }
            }

            playerIconActors[i].setKingID(nextTurnOrder[nextTurnIndex].getId());
        }
    }
}
