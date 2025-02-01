package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import dev.kingdomino.game.GameManager;

public class EndDialog {
    private LeaderboardRenderManager leaderboardRenderManager;
    private Dialog endGameDialog;

    public EndDialog(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle headerStyle,
            Label.LabelStyle bodyStyle, NinePatchDrawable bezel, NinePatchDrawable bezelBackground) {
        this.leaderboardRenderManager = new LeaderboardRenderManager(gameManager, kingAvatar, bodyStyle, bodyStyle,
                bezel, bezelBackground);

        endGameDialog = new Dialog("", new Window.WindowStyle(headerStyle.font, Color.WHITE, bezelBackground));

        endGameDialog.add(leaderboardRenderManager.getLayout())
                .height(500)
                .width(480);
    }

    public Dialog getDialog() {
        return endGameDialog;
    }
}
