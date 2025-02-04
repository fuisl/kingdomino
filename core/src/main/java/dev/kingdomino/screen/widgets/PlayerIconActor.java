package dev.kingdomino.screen.widgets;

import static java.lang.Math.min;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;

/**
 * An {@link Actor} specialize in drawing Player Icons. This actor does not
 * implement sizing preferences, and thus must be wrapped in a {@link Container}
 * to be used. Failure to do so will cause its height/width to be 0, making it
 * not drawing anything.
 * 
 * @see com.badlogic.gdx.scenes.scene2d.ui.Container
 * 
 * @author LunaciaDev
 */
public class PlayerIconActor extends Actor {
    private TextureRegion[] kingAvatar;
    private int kingID;

    public PlayerIconActor(TextureRegion[] kingAvatar) {
        this.kingAvatar = kingAvatar;
    }

    public void draw(Batch batch, float parentAlpha) {
        if (kingID == -1)
            return;
        float textureSize = min(getWidth(), getHeight());
        float offsetX = getWidth() / 2 - textureSize / 2;

        batch.draw(kingAvatar[kingID], getX() + offsetX, getY(), textureSize, textureSize);
    }

    @Override
    public void setBounds(float x, float y, float width, float height) {
        // catch setBounds from the parent to do resizing properly
        // this Actor does not implement preferred size system and.. that break the
        // entire layout system. Hooray! This workaround should be fine until it break
        // hard
        // enough that I have to implement Layout interface without any other choice.
        super.setBounds(x, y, this.getParent().getWidth(), this.getParent().getHeight());
    }

    /**
     * Set which King icon will be drawn based on the King's internal ID. Must be
     * called before drawing.
     * 
     * @param kingID Internal ID of the King that will be drawn
     */
    public void setKingID(int kingID) {
        this.kingID = kingID;
    }
}
