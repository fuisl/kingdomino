package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.GameManager;

/**
 * Defines shared implementation detail for RenderManager classes.
 * 
 * @author LunaciaDev
 */
public abstract class AbstractRenderManager extends Actor {
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

    /**
     * Generate a {@link Container} wrapping the given {@link Actor}.
     * 
     * @param actor The Actor that will be wrapped
     * @return A {@link Container} containing the given {@link Actor}
     */
    protected Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    /**
     * Abstract method to enforce all RenderManagers to have a setLayout method.
     */
    public abstract Table getLayout();
}
