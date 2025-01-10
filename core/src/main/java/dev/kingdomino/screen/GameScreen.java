package dev.kingdomino.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;

import dev.kingdomino.game.Board;
import dev.kingdomino.game.Domino;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.Position;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;

public class GameScreen extends AbstractScreen {
    private GameManager gameManager;
    private TextureRegion[] crownOverlay;
    private FitViewport tableView;
    private OrthographicCamera tableCamera;

    protected GameScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch, assetManager);
    }

    @Override
    public void initScreen() {
        gameManager = new GameManager();

        TextureAtlas atlas = assetManager.get("tileTextures.atlas");

        for (TerrainType name : TerrainType.values()) {
            name.setTexture(atlas.findRegion(name.name().toLowerCase()));
        }

        crownOverlay = new TextureRegion[4];
        crownOverlay[0] = atlas.findRegion("nocrown");
        crownOverlay[1] = atlas.findRegion("onecrown");
        crownOverlay[2] = atlas.findRegion("twocrown");
        crownOverlay[3] = atlas.findRegion("threecrown");

        tableCamera = new OrthographicCamera();
        tableView = new FitViewport(10, 10, tableCamera);
        tableView.getCamera().position.set(4, 4, 0);
    };

    @Override
    public void render(float delta) {
        gameManager.update(delta);
        // TODO remove this as we are now rendering from the UI
        // keep for debugging purpose only.
        gameManager.render(spriteBatch);

        // TODO remove this hack once we got the other screens
        // this right now immediately crash the game due to the board... not existing.
        Board gameBoard = gameManager.getBoard();
        if (gameBoard == null) return;

        // What monstrosity am I creating here

        // Drag this entire thing into a helper function/class?
        ScreenUtils.clear(Color.WHITE);
        // pass in the size of the viewport here, in pixels
        tableView.update(400, 400);
        tableView.setScreenPosition(100, 100);
        tableView.apply();
        spriteBatch.setProjectionMatrix(tableView.getCamera().combined);
        spriteBatch.begin();
        drawFocusedGameBoard(gameManager.getBoard().getLand(), spriteBatch);
        spriteBatch.setColor(1f, 1f, 1f, 0.5f);
        drawDominoHover(gameManager.getCurrentDomino(), spriteBatch);
        spriteBatch.setColor(1f, 1f, 1f, 1f);
        spriteBatch.end();
    }

    private void drawDominoHover(Domino currentDomino, SpriteBatch spriteBatch) {
        // TODO hover have blinking effect instead of 50% opacity
        Tile tileA = currentDomino.getTileA();

        // TODO render a failed placement attempt instead
        // without this game crash on placing tile at invalid position.
        if (tileA == null) return;

        Tile tileB = currentDomino.getTileB();
        Position tileAPosition = currentDomino.getPosTileA();
        Position tileBPosition = currentDomino.getPosTileB();

        spriteBatch.draw(tileA.getTerrain().getTexture(), tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        spriteBatch.draw(crownOverlay[tileA.getCrown()], tileAPosition.x(), 8-tileAPosition.y(), 1, 1);
        spriteBatch.draw(tileB.getTerrain().getTexture(), tileBPosition.x(), 8-tileBPosition.y(), 1, 1);
        spriteBatch.draw(crownOverlay[tileB.getCrown()], tileBPosition.x(), 8-tileBPosition.y(), 1, 1);
    }

    private void drawFocusedGameBoard(Tile[][] boardTiles, SpriteBatch spriteBatch) {
        for (int i = 0; i < 9; i++) {
            for (int j = boardTiles[0].length - 1; j >= 0; j--) {
                if (boardTiles[i][j] != null) {
                    // the coordinate system of the screen has origin at bottom left instead of top left
                    spriteBatch.draw(boardTiles[i][j].getTerrain().getTexture(), j, boardTiles[0].length-i-1, 1, 1);
                    spriteBatch.draw(crownOverlay[boardTiles[i][j].getCrown()], j, boardTiles[0].length-i-1, 1, 1);
                }
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // TODO add resizing logic
    }

    @Override
    public void pause() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'pause'");
    }

    @Override
    public void resume() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resume'");
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'hide'");
    }
}