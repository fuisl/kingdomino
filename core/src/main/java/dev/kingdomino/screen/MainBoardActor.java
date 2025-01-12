package dev.kingdomino.screen;

import static java.lang.Math.round;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.Position;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

public class MainBoardActor extends Actor {
    private Tile[][] boardTiles;
    private TextureRegion[] crownOverlay;
    private FitViewport tableViewport;
    private ScreenViewport gameViewport;
    private Domino currentDomino;
    
    public MainBoardActor(TextureRegion[] crownOverlay, ScreenViewport screenViewport) {
        this.crownOverlay = crownOverlay;
        this.gameViewport = screenViewport;
        tableViewport = new FitViewport(10, 10);
        tableViewport.getCamera().position.set(4, 4, 0);
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.end();

        tableViewport.setScreenPosition(round(getX()), round(getY()));
        tableViewport.setScreenSize(round(getWidth()), round(getHeight()));
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

        Tile tileA = currentDomino.getTileA();
        Tile tileB = currentDomino.getTileB();
        Position tileAPosition = currentDomino.getPosTileA();
        Position tileBPosition = currentDomino.getPosTileB();

        if (tileA.getTerrain() == TerrainType.INVALID || tileB.getTerrain() == TerrainType.INVALID) return;

        batch.setColor(1f, 1f, 1f, 0.5f);

        batch.draw(tileA.getTerrain().getTexture(), tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileA.getCrown()], tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        batch.draw(tileB.getTerrain().getTexture(), tileBPosition.x(), 8-tileBPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileB.getCrown()], tileBPosition.x(), 8-tileBPosition.y(), 1, 1);

        batch.setColor(1f, 1f, 1f, 1f);
        batch.end();

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
    }

    public void setBoard(Tile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }

    public void setCurrentDomino(Domino currentDomino) {
        this.currentDomino = currentDomino;
    }
}