package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;
import dev.kingdomino.game.Turn;

public class NextDominoRenderManager extends AbstractRenderManager {
    private DominoActor[] dominoActors;

    public NextDominoRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin, TextureRegion[] crownOverlay) {
        super(gameManager, kingAvatar, skin);

        dominoActors = new DominoActor[kingCount];

        for (int i = 0; i < kingCount; i++) {
            dominoActors[i] = new DominoActor(crownOverlay);
        }
    }

    @Override
    public void setLayout(Table layout) {
        layout.add(new Label("Next Dominoes", skin)).colspan(2);
        layout.row();

        for (int i = 0; i < kingCount; i++) {
            layout.add(generateContainer(dominoActors[i])).expand().fill();
            layout.add(generateContainer(playerIconActors[i])).expand().fill();
            layout.row();
        }
    }

    @Override
    public void act(float delta) {
        Turn nextTurn = gameManager.getNextTurn();

        if (nextTurn == null) {
            // TODO implement end of turn view
            // right now it break at this point
            return;
        }

        Domino[] nextTurnDomino = nextTurn.getDraft();
        King[] nextTurnPick = nextTurn.getKings();

        for (int i = 0; i < kingCount; i++) {
            dominoActors[i].setDomino(nextTurnDomino[i]);

            if (nextTurnPick[i] == null) {
                playerIconActors[i].setKingID(-1);
                continue;
            }

            playerIconActors[i].setKingID(nextTurnPick[i].getId());
        }
    }
}
