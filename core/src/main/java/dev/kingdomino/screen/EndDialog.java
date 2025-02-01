package dev.kingdomino.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import dev.kingdomino.game.GameManager;

public class EndDialog {
    private LeaderboardRenderManager leaderboardRenderManager;
    private Dialog endGameDialog;

    public EndDialog(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle headerStyle,
            Label.LabelStyle bodyStyle, NinePatchDrawable bezel, NinePatchDrawable bezelBackground,
            NinePatchDrawable whiteBezel, TextureRegion darkBackground) {
        this.leaderboardRenderManager = new LeaderboardRenderManager(gameManager, kingAvatar, bodyStyle, bodyStyle,
                bezel, bezelBackground);

        Window.WindowStyle dialogStyle = new Window.WindowStyle(headerStyle.font, Color.WHITE, bezelBackground);
        dialogStyle.stageBackground = new TextureRegionDrawable(darkBackground);
    
        endGameDialog = new Dialog("", dialogStyle);

        endGameDialog.getContentTable().add(new Label("Game Over", headerStyle));
        endGameDialog.getContentTable().row();

        endGameDialog.getContentTable().add(leaderboardRenderManager.getLayout())
                .height(400)
                .width(300)
                .expand()
                .fill();

        endGameDialog.getContentTable().row();

        TextButton temp = new TextButton("Exit", new TextButtonStyle(whiteBezel, null, null, bodyStyle.font));
        temp.getLabel().setColor(Color.BLACK);

        temp.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.exit();
            }
        });

        endGameDialog.getContentTable().add(temp);
    }

    public Dialog getDialog() {
        leaderboardRenderManager.act(0);
        return endGameDialog;
    }
}
