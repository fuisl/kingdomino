package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.Domino;
import dev.kingdomino.game.DraftInputProcessor;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;
import dev.kingdomino.game.Turn;
import dev.kingdomino.game.GameManager.GameState;

/**
 * A RenderManager specialize in showing next round's {@link Domino} on the left side,
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class NextDominoRenderManager extends AbstractRenderManager {
    private DominoActor[] dominoActors;
    private HighlightActor[] highlightActors;
    private DraftInputProcessor draftInputProcessor;

    public NextDominoRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin,
                                   TextureRegion[] crownOverlay, TextureRegion selectionOverlay) {

        super(gameManager, kingAvatar, skin);

        dominoActors = new DominoActor[kingCount];
        highlightActors = new HighlightActor[kingCount];

        for (int i = 0; i < kingCount; i++) {
            dominoActors[i] = new DominoActor(crownOverlay);
            highlightActors[i] = new HighlightActor(selectionOverlay);
        }

        draftInputProcessor = gameManager.getDraftInputProcessor();
    }

    @Override 
    public void setLayout(Table layout) {
        layout.add(new Label("Next Dominoes", skin));
        layout.row();

        for (int i = 0; i < kingCount; i++) {
            Stack stack = new Stack();
            stack.add(generateContainer(highlightActors[i]));
            
            Table row = new Table();
            row.pad(5);
            row.add(generateContainer(dominoActors[i])).expand().fill();
            row.add(generateContainer(playerIconActors[i])).expand().fill();

            stack.add(row);

            layout.add(stack).expand().fill();
            layout.row();
        }
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

        if (gameManager.getCurrentState() != GameState.TURN_CHOOSING) return;

        int currentSelection = draftInputProcessor.getSelectionIndex();

        highlightActors[currentSelection].setVisible(true);
    }

    /**
     * Hide all highlight actors.
     */
    private void hideAllHighlighter() {
        for (int i = 0; i < kingCount; i++) {
            highlightActors[i].setVisible(false);
        }
    }
}
