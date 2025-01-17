package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

public class MainBoardHUDManager {
    // not extending AbstractRenderManager here as we do not need to render all 4 player at once.

    private GameManager gameManager;
    private PlayerIconActor playerIconActor;
    //private Label playerName;
    private Label playerScore;

    public MainBoardHUDManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        this.gameManager = gameManager;

        this.playerIconActor = new PlayerIconActor(kingAvatar);
        //this.playerName = new Label("Placeholder", skin);
        this.playerScore = new Label("Placeholder", skin);
    }

    public void setLayout(Table layout) {
        layout.add(generateContainer(playerIconActor)).width(Value.percentHeight(1f, layout)).expandY().fill();
        //layout.add(generateContainer(playerName)).expand().fill();
        layout.add(playerScore).expand().fill().align(Align.right);
    }

    private Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    public void informActors() {
        King currentKing = gameManager.getCurrentKing();

        playerIconActor.setKingID(currentKing.getId());
        playerScore.setText(currentKing.getBoard().getScoringSystem().getBoardTotal());
    }
}
