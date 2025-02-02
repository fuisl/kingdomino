package dev.kingdomino.screen;

import static java.lang.Math.round;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.Position;
import dev.kingdomino.game.TerrainType;
import dev.kingdomino.game.Tile;
import dev.kingdomino.game.GameManager.GameState;

/**
 * An {@link Actor} specialize in drawing the Game Board. Unlike other Actors,
 * this has its own {@link FitViewport} to draw with and thus having finer
 * control on the drawing process. This actor does not implement sizing
 * preferences, and thus must be wrapped in a {@link Container} to be used.
 * Failure to do so will cause its height/width to be 0, making it not drawing
 * anything.
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
    private TransparencyTween transparencyTween;

    public MainBoardActor(TextureRegion[] crownOverlay, ScreenViewport screenViewport, GameManager gameManager) {
        this.gameManager = gameManager;
        this.crownOverlay = crownOverlay;
        this.gameViewport = screenViewport;
        tableViewport = new FitViewport(9, 9);
        tableViewport.getCamera().position.set(4.5f, 4.5f, 0);
        tableViewport.getCamera().update();
        this.transparencyTween = new TransparencyTween(0.4f, 1f, 0.5f);
        this.addAction(transparencyTween);
    }

    @Override
    public void act(float delta) {
        // advance the Action system
        super.act(delta);

        this.boardTiles = gameManager.getBoard().getLand();
        this.currentDomino = gameManager.getCurrentDomino();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        startCustomRender(batch);

        for (int i = 0; i < 9; i++) {
            for (int j = boardTiles[0].length - 1; j >= 0; j--) {
                if (boardTiles[i][j] != null) {
                    // the coordinate system of the screen has origin at bottom left instead of top
                    // left
                    if (boardTiles[i][j].getTerrain() == TerrainType.CASTLE) {
                        batch.draw(boardTiles[i][j].getTerrain().getCastleTexture(gameManager.getCurrentKing().getId()),
                                j, boardTiles[0].length - i - 1, 1, 1);
                        continue;
                    }

                    batch.draw(boardTiles[i][j].getTerrain().getTexture(), j, boardTiles[0].length - i - 1, 1, 1);
                    batch.draw(crownOverlay[boardTiles[i][j].getCrown()], j, boardTiles[0].length - i - 1, 1, 1);
                }
            }
        }

        if (gameManager.getCurrentState() != GameState.TURN_PLACING) {
            endCustomRender(batch);
            return;
        }

        Tile tileA = currentDomino.getTileA();
        Tile tileB = currentDomino.getTileB();
        Position tileAPosition = currentDomino.getPosTileA();
        Position tileBPosition = currentDomino.getPosTileB();

        // Blink the hovering domino if the placement is invalid
        if (tileA.getTerrain() == TerrainType.INVALID || tileB.getTerrain() == TerrainType.INVALID) {
            endCustomRender(batch);
            return;
        }

        batch.setColor(1f, 1f, 1f, transparencyTween.getValue());

        batch.draw(tileA.getTerrain().getTexture(), tileAPosition.x(), 8 - tileAPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileA.getCrown()], tileAPosition.x(), 8 - tileAPosition.y(), 1, 1);
        batch.draw(tileB.getTerrain().getTexture(), tileBPosition.x(), 8 - tileBPosition.y(), 1, 1);
        batch.draw(crownOverlay[tileB.getCrown()], tileBPosition.x(), 8 - tileBPosition.y(), 1, 1);

        batch.setColor(1f, 1f, 1f, 1f);
        endCustomRender(batch);
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
                round(getY()) + tableViewport.getBottomGutterHeight());
        tableViewport.apply();
        batch.setProjectionMatrix(tableViewport.getCamera().combined);
        batch.begin();
    }

    private class TransparencyTween extends Action {
        private float startValue;
        private float endValue;
        private float duration;
        private float elapsedTime;
        private boolean reverse;

        private float dist;

        private float value;

        /**
         * Instantiate an Action that tween linearly between 2 value over a certain
         * amount of time. Throw {@link IllegalArgumentException} if startValue is
         * bigger than or equal to endValue.
         * 
         * @param startValue The minimum value of the tween
         * @param endValue The maximum value of the tween
         * @param duration How long the tween will take
         */
        public TransparencyTween(float startValue, float endValue, float duration) {
            if (startValue >= endValue) {
                throw new IllegalArgumentException("startValue must be smaller than endValue");
            }

            this.startValue = startValue;
            this.endValue = endValue;
            this.duration = duration;
            this.reverse = false;
            this.dist = endValue - startValue;
        }

        @Override
        public boolean act(float delta) {
            elapsedTime += delta;

            if (elapsedTime > duration) {
                elapsedTime = 0;
                reverse = !reverse;
            }

            float interpolateFactor = elapsedTime / duration;

            if (reverse) {
                value = endValue - (dist * interpolateFactor);
            }
            else {
                value = startValue + (dist * interpolateFactor);
            }

            // this action never stop. Good idea?
            return false;
        }

        public float getValue() {
            return value;
        }
    }
}