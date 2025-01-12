package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

public class SideBoardManager {
    private SideBoardActor[] sideBoardActors;
    private FitViewport tableViewport;
    private GameManager gameManager;
    private int kingCount;

    public SideBoardManager(GameManager gameManager, TextureRegion[] crownOverlay, ScreenViewport screenViewport) {
        this.gameManager = gameManager;
        kingCount = gameManager.getKingCount();
        
        // clamping as 2 king game has 4 boards
        // 4 king game has 4 board, 3 king game has 3 board.
        if (kingCount == 2) kingCount = 4;
        kingCount -= 1;

        sideBoardActors = new SideBoardActor[kingCount];
        tableViewport = new FitViewport(10, 10);
        tableViewport.getCamera().position.set(4, 4, 0);

        for (int i = 0; i < kingCount; i++) {
            sideBoardActors[i] = new SideBoardActor(crownOverlay, screenViewport, tableViewport);
        }

        if (kingCount == 2) {
            sideBoardActors[0].setPosition(1600, 450);
            sideBoardActors[1].setPosition(1600, 0);
        }
        else {
            sideBoardActors[0].setPosition(1600, 600);
            sideBoardActors[1].setPosition(1600, 300);
            sideBoardActors[2].setPosition(1600, 0);
        }
    }

    public void updateSideBoard() {
        int index = 0;

        for (King king : gameManager.getAllKing()) {
            if (king == gameManager.getCurrentKing()) continue;
            sideBoardActors[index].setBoard(king.getBoard().getLand());
            index++;
        }
    }

    public SideBoardActor[] getSideBoardActors() {
        return sideBoardActors;
    }
}
