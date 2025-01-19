package dev.kingdomino.screen;

import static java.lang.Math.min;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerIconActor extends Actor {
    private TextureRegion[] kingAvatar;
    private int kingID;

    public PlayerIconActor(TextureRegion[] kingAvatar) {
        this.kingAvatar = kingAvatar;
    }

    public void draw(Batch batch, float parentAlpha) {
        if (kingID == -1) return;
        float textureSize = min(getWidth(), getHeight());
        float offsetX = getWidth()/2 - textureSize/2;

        batch.draw(kingAvatar[kingID], getX() + offsetX, getY(), textureSize, textureSize);
    }

    public void setKingID(int kingID) {
        this.kingID = kingID;
    }
}
