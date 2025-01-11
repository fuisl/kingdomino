package dev.kingdomino.screen;

import static java.lang.Math.round;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.Tile;

public class SideBoardActor extends Actor {
    private Tile[][] boardTiles;
    private TextureRegion[] crownOverlay;
    private FitViewport tableViewport;
    private ScreenViewport gameViewport;
    
    public SideBoardActor(TextureRegion[] crownOverlay, ScreenViewport screenViewport, FitViewport tableViewport) {
        this.crownOverlay = crownOverlay;
        this.gameViewport = screenViewport;
        this.tableViewport = tableViewport;
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        tableViewport.setScreenPosition(round(getX()), round(getY()));
        tableViewport.setScreenSize(300, 300);
        tableViewport.apply();
        batch.setProjectionMatrix(tableViewport.getCamera().combined);
        batch.begin();

        for (int i = 0; i < 9; i++) {
            for (int j = boardTiles[0].length - 1; j >= 0; j--) {
                if (boardTiles[i][j] != null) {
                    // the coordinate system of the screen has origin at bottom left instead of top left
                    batch.draw(boardTiles[i][j].getTerrain().getTexture(), j, boardTiles[0].length-i-1, 1, 1);
                    batch.draw(crownOverlay[boardTiles[i][j].getCrown()], j, boardTiles[0].length-i-1, 1, 1);
                }
            }
        }
        batch.end();

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
    }

    public void setBoard(Tile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }
}