package dev.kingdomino.screen;

import static java.lang.Math.min;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dev.kingdomino.game.Domino;

public class DominoActor extends Actor {
    private Domino domino;
    private TextureRegion[] crownOverlay;

    public DominoActor(TextureRegion[] crownOverlay) {
        this.crownOverlay = crownOverlay;
    }

    public void draw(Batch batch, float parentAlpha) {
        float textureSize = min(getWidth()/2, getHeight());
        float padding = (getWidth() - textureSize*2)/2;

        batch.draw(domino.getTileA().getTerrain().getTexture(), padding + getX(), getY(), textureSize, textureSize);
        batch.draw(crownOverlay[domino.getTileA().getCrown()], padding + getX(), getY(), textureSize, textureSize);
        batch.draw(domino.getTileB().getTerrain().getTexture(), padding + getX() + textureSize, getY(), textureSize, textureSize);
        batch.draw(crownOverlay[domino.getTileB().getCrown()], padding + getX() + textureSize, getY(), textureSize, textureSize);
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }
}
