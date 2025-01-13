package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.GameManager;

public abstract class AbstractRenderManager {
    protected GameManager gameManager;
    protected PlayerIconActor[] playerIconActors;
    protected int kingCount;
    protected Skin skin;

    public AbstractRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        this.gameManager = gameManager;
        this.kingCount = gameManager.getKingCount();
        this.playerIconActors = new PlayerIconActor[kingCount];
        this.skin = skin;
        
        for (int i = 0; i < kingCount; i++) {
            playerIconActors[i] = new PlayerIconActor(kingAvatar);
        }
    }

    public abstract void setLayout(Table layout);

    public abstract void informActors();
}
