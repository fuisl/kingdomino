package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.game.King;

/**
 * A RenderManager specialize in showing the turn order on the left side,
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class TurnOrderRenderManager extends AbstractRenderManager {
    private Label score;

    public TurnOrderRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        super(gameManager, kingAvatar, skin);
        score = new Label("[PH]", skin);
        score.setColor(Color.RED);
    }

    public Table getLayout() {
        Table layout = new Table();

        Label header = new Label("Current Player", skin);
        header.setAlignment(Align.center);

        layout.add(header)
                .width(Value.percentWidth(0.9f, layout))
                .pad(5);

        layout.row();

        Table currentPlayer = new Table();

        currentPlayer.add(generateContainer(playerIconActors[0]))
                .height(Value.percentHeight(0.3f, layout))
                .width(Value.percentWidth(0.5f, currentPlayer))
                .align(Align.center)
                .fill()
                .pad(5);

        Table playerScore = new Table();

        playerScore.add(new Label("Score", skin))
                .pad(5);

        playerScore.row();
        playerScore.add(score)
                .pad(5);

        currentPlayer.add(playerScore);

        layout.add(currentPlayer).fill();
        layout.row();

        Table upcomingPlayers = new Table();

        upcomingPlayers.add(new Label("Next:", skin))
                .pad(5);

        for (int i = 1; i < kingCount; i++) {
            upcomingPlayers.add(generateContainer(playerIconActors[i]))
                    .height(Value.percentHeight(0.15f, layout))
                    .align(Align.center)
                    .expand()
                    .fill()
                    .pad(5);
        }

        layout.add(upcomingPlayers).fill().pad(10, 0, 0, 0);

        return layout;
    }

    protected Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    @Override
    public void act(float delta) {
        if (gameManager.getNextTurn() == null)
            return;

        King[] currentTurnOrder = gameManager.getCurrentTurn().getKings();
        int currentTurnIndex = gameManager.getCurrentTurn().getCurrentIndex();
        King[] nextTurnOrder = gameManager.getNextTurn().getKings();
        int nextTurnIndex = 0;

        // TODO figure out how to animate this mess
        for (int i = 0; i < kingCount; i++) {
            if (currentTurnIndex < kingCount) {
                playerIconActors[i].setKingID(currentTurnOrder[currentTurnIndex].getId());
                currentTurnIndex++;
                continue;
            }

            while (nextTurnOrder[nextTurnIndex] == null) {
                nextTurnIndex++;

                if (nextTurnIndex == kingCount) {
                    for (; i < kingCount; i++) {
                        playerIconActors[i].setKingID(-1);
                    }

                    return;
                }
            }

            playerIconActors[i].setKingID(nextTurnOrder[nextTurnIndex].getId());
        }
    }
}
