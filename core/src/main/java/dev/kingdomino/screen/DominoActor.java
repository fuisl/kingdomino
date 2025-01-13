package dev.kingdomino.screen;

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
        batch.draw(domino.getTileA().getTerrain().getTexture(), getX(), getY(), getWidth()/2, getHeight());
        batch.draw(crownOverlay[domino.getTileA().getCrown()], getX(), getY(), getWidth()/2, getHeight());
        batch.draw(domino.getTileB().getTerrain().getTexture(), getX() + getWidth()/2, getY(), getWidth()/2, getHeight());
        batch.draw(crownOverlay[domino.getTileB().getCrown()], getX() + getWidth()/2, getY(), getWidth()/2, getHeight());
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }
}