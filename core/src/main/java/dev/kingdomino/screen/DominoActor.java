package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import dev.kingdomino.game.Domino;

public class DominoActor extends Actor {
    private Domino domino;

    public DominoActor() {}

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(domino.getTileA().getTerrain().getTexture(), getX(), getY(), getWidth()/2, getHeight());
        batch.draw(domino.getTileB().getTerrain().getTexture(), getX() + getWidth()/2, getY(), getWidth()/2, getHeight());
    }

    public void setDomino(Domino domino) {
        this.domino = domino;
    }
}
