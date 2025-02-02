package dev.kingdomino.screen;

import static java.lang.Math.round;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

/**
 * An {@link Actor} specialize in drawing inactive Game Boards. Unlike other
 * Actors, these share their own {@link FitViewport} to draw with and thus
 * having finer control on the drawing process. This actor does not implement
 * sizing preferences, and thus must be wrapped in a {@link Container} to be
 * used. Failure to do so will cause its height/width to be 0, making it not
 * drawing anything.
 * 
 * @author LunaciaDev
 */
public class SideBoardActor extends Actor {
    private Tile[][] boardTiles;
    private TextureRegion[] crownOverlay;
    private TextureRegion[] kingAvatar;
    private FitViewport tableViewport;
    private ScreenViewport gameViewport;
    private int kingID;

    /**
     * Initialize the Actor. The same tableView should be supplied for all instances
     * of this.
     */
    public SideBoardActor(TextureRegion[] crownOverlay, ScreenViewport screenViewport, FitViewport tableView,
            TextureRegion[] kingAvatar) {
        this.crownOverlay = crownOverlay;
        this.gameViewport = screenViewport;
        this.tableViewport = tableView;
        this.kingAvatar = kingAvatar;
    }

    public void draw(Batch batch, float parentAlpha) {
        batch.draw(kingAvatar[kingID], getX(), getY() + getHeight() - 48, 48, 48);

        batch.end();

        tableViewport.update(round(getWidth()), round(getHeight()), true);
        tableViewport.setScreenPosition(
                round(getX()) + tableViewport.getLeftGutterWidth(),
                round(getY()) + tableViewport.getBottomGutterHeight());
        tableViewport.apply();
        batch.setProjectionMatrix(tableViewport.getCamera().combined);
        batch.begin();

        for (int i = 0; i < 9; i++) {
            for (int j = boardTiles[0].length - 1; j >= 0; j--) {
                if (boardTiles[i][j] != null) {
                    // the coordinate system of the screen has origin at bottom left instead of top
                    // left

                    if (boardTiles[i][j].getTerrain() == TerrainType.CASTLE) {
                        batch.draw(boardTiles[i][j].getTerrain().getCastleTexture(kingID), j, boardTiles[0].length - i - 1, 1, 1);
                        continue;
                    }

                    batch.draw(boardTiles[i][j].getTerrain().getTexture(), j, boardTiles[0].length - i - 1, 1, 1);
                    batch.draw(crownOverlay[boardTiles[i][j].getCrown()], j, boardTiles[0].length - i - 1, 1, 1);
                }
            }
        }
        batch.end();

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

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
     * Set which King this board belong to. Must be called before drawing.
     * 
     * @param kingID Internal ID of the King the board belong to.
     */
    public void setKingID(int kingID) {
        this.kingID = kingID;
    }
}