package dev.kingdomino.screen;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

/**
 * A RenderManager specialize in handling the Leaderboard to the left of the
 * Game Screen, automatically updating that based on the current
 * {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class LeaderboardRenderManager extends AbstractRenderManager {
    private Label[] pointLabels;
    private Label.LabelStyle headerStyle;
    private Label.LabelStyle bodyStyle;
    private NinePatchDrawable bezel;
    private NinePatchDrawable bezelBackground;

    public LeaderboardRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle headerStyle,
            Label.LabelStyle bodyStyle, NinePatchDrawable bezel, NinePatchDrawable bezelBackground) {
        super(gameManager, kingAvatar);
        pointLabels = new Label[kingCount];

        this.headerStyle = headerStyle;
        this.bodyStyle = bodyStyle;
        this.bezel = bezel;
        this.bezelBackground = bezelBackground;

        for (int i = 0; i < kingCount; i++) {
            pointLabels[i] = new Label("0", bodyStyle);
        }
    }

    @Override
    public Table getLayout() {
        Table layout = new Table();
        layout.background(bezelBackground);

        String[] label = { "1st", "2nd", "3rd", "4th" };

        layout.add(new Label("Leaderboard", headerStyle))
                .pad(0, 0, 5, 0)
                .colspan(2);

        layout.row();

        for (int i = 0; i < kingCount; i++) {
            Table entry = new Table();
            entry.background(bezel);

            entry.add(new Label(label[i], bodyStyle))
                    .expand();

            entry.add(generateContainer(playerIconActors[i]))
                    .height(Value.percentHeight(0.7f, entry))
                    .width(Value.percentWidth(0.33f, entry))
                    .fill();

            entry.add(pointLabels[i])
                    .expand();

            layout.add(entry)
                    .fill()
                    .expandX();
          
            layout.row();  
        }

        return layout;
    }

    @Override
    public void act(float delta) {
        Map<King, int[]> scores = gameManager.getScores();

        if (scores == null)
            return;

        int position = 0;
        for (Map.Entry<King, int[]> entry : scores.entrySet()) {
            playerIconActors[position].setKingID(entry.getKey().getId());
            pointLabels[position].setText(entry.getValue()[2]);
            position++;
        }
    }
}
