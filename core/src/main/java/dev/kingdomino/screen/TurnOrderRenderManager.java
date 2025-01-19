package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

public class TurnOrderRenderManager extends AbstractRenderManager {
    public TurnOrderRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        super(gameManager, kingAvatar, skin);
    }

    @Override
    public void setLayout(Table layout) {
        layout.add(new Label("Turn Order", skin)).colspan(kingCount);
        layout.row();
        for (int i = 0; i < kingCount; i++) {
            layout.add(generateContainer(playerIconActors[i]))
                .expand()
                .fill();
        }
    }

    @Override
    public void act(float delta) {
        King[] turnOrder = gameManager.getCurrentTurn().getKings();
        int position = 0;

        for (King king : turnOrder) {
            playerIconActors[position].setKingID(king.getId());
            position++;
        }
    }
}
