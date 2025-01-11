package dev.kingdomino.screen;

import com.badlogic.gdx.utils.viewport.Viewport;

import dev.kingdomino.game.Position;

public class BoardRenderHelper {
    private Position[] boardPositions;
    private Boolean threeKing = false;
    private int kingIndex = 0;

    public BoardRenderHelper(int kingCount) {
        if (kingCount == 3) {
            threeKing = true;
            boardPositions = new Position[2];
            boardPositions[0] = new Position(1600, 450);
            boardPositions[1] = new Position(1600, 0);
            return;
        }

        boardPositions = new Position[3];
        boardPositions[0] = new Position(1600, 600);
        boardPositions[1] = new Position(1600, 300);
        boardPositions[2] = new Position(1600, 000);
    }

    public void setViewportPosition(Viewport tableView) {
        tableView.setScreenPosition(boardPositions[kingIndex].x(), boardPositions[kingIndex].y());
        kingIndex++;

        if (threeKing) {
            if (kingIndex == 2) kingIndex = 0;
            return;
        }

        if (kingIndex == 3) kingIndex = 0;
    }

    public void updateViewport(Viewport tableView) {
        if (threeKing) {
            tableView.update(450, 450);
            return;
        }

        tableView.update(300, 300);
    }

    public void resize(float width, float height) {
        // TODO move the board according to screen
    }
}
