package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * An {@link Actor} specialize in drawing Highlight. This actor does not
 * implement sizing preferences, and thus must be wrapped in a {@link Container}
 * to be used. Failure to do so will cause its height/width to be 0, making it
 * not drawing anything.
 * 
 * @see com.badlogic.gdx.scenes.scene2d.ui.Container
 * 
 * @author LunaciaDev
 */
public class HighlightActor extends Actor {
    private TextureRegion texture;

    public HighlightActor(TextureRegion texture) {
        this.texture = texture;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
    }
}
