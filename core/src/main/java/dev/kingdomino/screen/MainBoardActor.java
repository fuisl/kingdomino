package dev.kingdomino.screen;

import static java.lang.Math.round;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.Position;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

/**
 * An {@link Actor} specialize in drawing the Game Board. Unlike other Actors, this has its own {@link FitViewport}
 * to draw with and thus having finer control on the drawing process. This actor does not implement sizing preferences,
 * and thus must be wrapped in a {@link Container} to be used. Failure to do so will cause its height/width to be 0,
 * making it not drawing anything.
 * 
 * @author LunaciaDev
 */
public class MainBoardActor extends Actor {
    private Tile[][] boardTiles;
    private TextureRegion[] crownOverlay;
    private FitViewport tableViewport;
    private ScreenViewport gameViewport;
    private Domino currentDomino;
    private GameManager gameManager;
    
    public MainBoardActor(TextureRegion[] crownOverlay, ScreenViewport screenViewport, GameManager gameManager) {
        this.gameManager = gameManager;
        this.crownOverlay = crownOverlay;
        this.gameViewport = screenViewport;
        tableViewport = new FitViewport(9, 9);
        tableViewport.getCamera().position.set(4.5f, 4.5f, 0);
        tableViewport.getCamera().update();
    }

    @Override
    public void act(float delta) {
        this.boardTiles = gameManager.getBoard().getLand();
        this.currentDomino = gameManager.getCurrentDomino();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        startCustomRender(batch);

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

        // Blink the hovering domino if the placement is invalid
        if (tileA.getTerrain() == TerrainType.INVALID || tileB.getTerrain() == TerrainType.INVALID)  {
            endCustomRender(batch);
            return;
        }

        batch.setColor(1f, 1f, 1f, 0.5f);

        batch.draw(tileA.getTerrain().getTexture(), tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileA.getCrown()], tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        batch.draw(tileB.getTerrain().getTexture(), tileBPosition.x(), 8-tileBPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileB.getCrown()], tileBPosition.x(), 8-tileBPosition.y(), 1, 1);

        batch.setColor(1f, 1f, 1f, 1f);
        endCustomRender(batch);
    }

    private void endCustomRender(Batch batch) {
        batch.end();

        // return the batch state to its original state.
        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        batch.begin();
    }

    private void startCustomRender(Batch batch) {
        batch.end();
        
        // update the viewport positions in case if the screen size has changed
        tableViewport.update(round(getWidth()), round(getHeight()));
        tableViewport.setScreenPosition(
            round(getX()) + tableViewport.getLeftGutterWidth(),
            round(getY()) + tableViewport.getBottomGutterHeight()
        );
        tableViewport.apply();
        batch.setProjectionMatrix(tableViewport.getCamera().combined);
        batch.begin();
    }

    /**
     * Set the Game Board to be drawn. Must be called before drawing.
     * 
     * @param boardTiles The Game Board to be drawn
     */
    public void setBoard(Tile[][] boardTiles) {
        this.boardTiles = boardTiles;
    }

    /**
     * Set the {@link Domino} that is being placed. Must be called before drawing.
     * 
     * @param currentDomino The Domino that is currently being placed
     */
    public void setCurrentDomino(Domino currentDomino) {
        this.currentDomino = currentDomino;
    }
}