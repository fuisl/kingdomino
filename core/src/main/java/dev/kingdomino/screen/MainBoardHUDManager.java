package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

/**
 * A RenderManager specialize in handling the player information at the top of
 * Game Screen, automatically updating that based on the current
 * {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class MainBoardHUDManager extends Actor {
    // not extending AbstractRenderManager here as we do not need to render all 4
    // player at once.

    private GameManager gameManager;
    private PlayerIconActor playerIconActor;
    // private Label playerName;
    private Label playerScore;

    public MainBoardHUDManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        this.gameManager = gameManager;

        this.playerIconActor = new PlayerIconActor(kingAvatar);
        // this.playerName = new Label("Placeholder", skin);
        this.playerScore = new Label("Placeholder", skin);
    }

    public Table getLayout() {
        Table layout = new Table();

        layout.add(generateContainer(playerIconActor))
                .width(Value.percentHeight(1f, layout))
                .expandY()
                .fill()
                .padLeft(15);
        // are we adding player name..?
        // layout.add(generateContainer(playerName)).expand().fill();
        layout.add(playerScore)
                .right()
                .expand()
                .pad(15);

        return layout;
    }

    /**
     * Generate a {@link Container} wrapping the given {@link Actor}.
     * 
     * @param actor The Actor that will be wrapped
     * @return A {@link Container} containing the given {@link Actor}
     */
    private Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    @Override
    public void act(float delta) {
        King currentKing = gameManager.getCurrentKing();

        playerIconActor.setKingID(currentKing.getId());
        playerScore.setText(currentKing.getBoard().getScoringSystem().getBoardTotal());
    }
}
