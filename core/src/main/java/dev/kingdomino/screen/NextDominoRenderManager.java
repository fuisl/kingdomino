package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.DraftInputHandler;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.game.King;
import dev.kingdomino.game.Turn;

/**
 * A RenderManager specialize in showing next round's {@link Domino} on the left
 * side, automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class NextDominoRenderManager extends AbstractRenderManager {
    private DominoActor[] dominoActors;
    private DraftInputHandler draftInputHandler;
    private Label.LabelStyle headerStyle;
    private NinePatchDrawable bezel;
    private NinePatchDrawable whiteBezel;
    private NinePatchDrawable bezelBackground;
    private Table[] rows;

    public NextDominoRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle headerStyle,
            TextureRegion[] crownOverlay, NinePatchDrawable bezel, NinePatchDrawable whiteBezel,
            NinePatchDrawable bezelBackground) {

        super(gameManager, kingAvatar);

        dominoActors = new DominoActor[kingCount];

        for (int i = 0; i < kingCount; i++) {
            dominoActors[i] = new DominoActor(crownOverlay);
        }

        draftInputHandler = gameManager.getDraftInputHandler();

        this.headerStyle = headerStyle;
        this.rows = new Table[kingCount];
        this.bezel = bezel;
        this.whiteBezel = whiteBezel;
        this.bezelBackground = bezelBackground;
    }

    @Override
    public Table getLayout() {
        Table layout = new Table();
        layout.setBackground(bezelBackground);

        layout.add(new Label("Next Dominoes", headerStyle)).pad(5);
        layout.row();

        for (int i = 0; i < kingCount; i++) {
            Table row = new Table();
            row.setBackground(bezel);

            row.add(generateContainer(dominoActors[i]))
                    .height(Value.percentWidth(0.2f, row))
                    .expandX()
                    .fill();

            row.add(generateContainer(playerIconActors[i]))
                    .height(Value.percentWidth(0.2f, row))
                    .expandX()
                    .fill();

            rows[i] = row;

            layout.add(row)
                    .fill()
                    .expandX()
                    .pad(5);

            layout.row();
        }

        layout.invalidate();
        layout.layout();
        return layout;
    }

    @Override
    public void act(float delta) {
        Turn nextTurn = gameManager.getNextTurn();

        if (nextTurn == null) {
            // TODO implement end of turn view
            // right now it break at this point
            return;
        }

        Domino[] nextTurnDomino = nextTurn.getDraft();
        King[] nextTurnPick = nextTurn.getKings();

        for (int i = 0; i < kingCount; i++) {
            dominoActors[i].setDomino(nextTurnDomino[i]);

            if (nextTurnPick[i] == null) {
                playerIconActors[i].setKingID(-1);
                continue;
            }

            playerIconActors[i].setKingID(nextTurnPick[i].getId());
        }

        hideAllHighlighter();

        if (gameManager.getCurrentState() != GameState.TURN_CHOOSING)
            return;

        int currentSelection = draftInputHandler.getSelectionIndex();

        rows[currentSelection].setBackground(whiteBezel);
    }

    /**
     * Hide all highlight actors.
     */
    private void hideAllHighlighter() {
        for (int i = 0; i < kingCount; i++) {
            rows[i].setBackground(bezel);
            ;
        }
    }
}
