package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

/**
 * Manage the Actors of the right-side panel of the screen.
 * 
 * @author LunaciaDev
 */
public class SidePanelManager extends Actor {
    private SideBoardActor[] sideBoardActors;
    private FitViewport tableViewport;
    private GameManager gameManager;
    private int kingCount;

    public SidePanelManager(GameManager gameManager, TextureRegion[] crownOverlay, ScreenViewport screenViewport,
            TextureRegion[] kingAvatar) {
        this.gameManager = gameManager;
        kingCount = gameManager.getKingCount();

        // clamping as 2 king game has 4 boards
        // 4 king game has 4 board, 3 king game has 3 board.
        if (kingCount == 2)
            kingCount = 4;
        kingCount -= 1;

        sideBoardActors = new SideBoardActor[kingCount];
        tableViewport = new FitViewport(9, 9);
        tableViewport.getCamera().position.set(4.5f, 4.5f, 0);
        tableViewport.getCamera().update();

        for (int i = 0; i < kingCount; i++) {
            sideBoardActors[i] = new SideBoardActor(crownOverlay, screenViewport, tableViewport, kingAvatar);
        }
    }

    @Override
    public void act(float delta) {
        int index = 0;

        for (King king : gameManager.getAllKing()) {
            if (king == gameManager.getCurrentKing())
                continue;
            sideBoardActors[index].setBoard(king.getBoard().getLand());
            sideBoardActors[index].setKingID(king.getId());
            index++;
        }
    }

    public Table getLayout() {
        Table layout = new Table();

        for (int i = 0; i < kingCount; i++) {
            layout.add(generateContainer(sideBoardActors[i]))
                    .expand()
                    .fill()
                    .pad(0, 10, 0, 0);
            layout.row();
        }

        return layout;
    }

    /**
     * Generate a {@link Container} wrapping the given {@link Actor}.
     * 
     * @param actor The Actor that will be wrapped
     * @return A {@link Container} containing the given {@link Actor}
     */
    private Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }
}
