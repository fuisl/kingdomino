package dev.kingdomino.screen;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;

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

    public LeaderboardRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle headerStyle, Label.LabelStyle bodyStyle) {
        super(gameManager, kingAvatar);
        pointLabels = new Label[kingCount];

        this.headerStyle = headerStyle;
        this.bodyStyle = bodyStyle;

        for (int i = 0; i < kingCount; i++) {
            pointLabels[i] = new Label("0", bodyStyle);
        }
    }

    @Override
    public Table getLayout() {
        Table layout = new Table();

        String[] label = { "1st", "2nd", "3rd", "4th" };

        layout.add(new Label("Leaderboard", headerStyle)).colspan(3);
        layout.row();

        for (int i = 0; i < kingCount; i++) {
            layout.add(new Label(label[i], bodyStyle))
                    .expand()
                    .center();
            layout.add(generateContainer(playerIconActors[i]))
                    .width(Value.percentWidth(1f / 3, layout))
                    .expandY()
                    .fill();
            layout.add(pointLabels[i])
                    .expand()
                    .center();
            layout.row();
        }

        return layout;
    }

    @Override
    public void act(float delta) {
        Map<King, int[]> scores = gameManager.getScores();

        if (scores == null) return;

        int position = 0;
        for (Map.Entry<King, int[]> entry : scores.entrySet()) {
            playerIconActors[position].setKingID(entry.getKey().getId());
            pointLabels[position].setText(entry.getValue()[2]);
            position++;
        }
    }
}
