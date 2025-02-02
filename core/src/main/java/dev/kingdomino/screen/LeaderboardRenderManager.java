package dev.kingdomino.screen;

import java.util.Map;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.effects.ShakeAction;
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

    private Table firstPosition;

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
        layout.setBackground(bezelBackground);

        String[] label = { "1st", "2nd", "3rd", "4th" };

        WavyLabel header = new WavyLabel("Leaderboard", headerStyle);
        layout.add(header)
                .pad(5);

        layout.row();

        for (int i = 0; i < kingCount; i++) {
            Table entry = new Table();
            entry.setBackground(bezel);

            entry.add(new Label(label[i], bodyStyle))
                    .expand();

            entry.add(generateContainer(playerIconActors[i]))
                    .height(Value.percentHeight(0.1f, layout))
                    .width(Value.percentWidth(0.33f, entry))
                    .fill();

            entry.add(pointLabels[i])
                    .expand();

            if (i == 0)
                firstPosition = entry; // save first position for effects.

            layout.add(entry)
                    .fill()
                    .expand()
                    .pad(5);

            layout.row();
        }

        layout.invalidate();
        layout.layout();
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

    public void animateFirstPosition() {
        firstPosition.setTransform(true);

        // Set the origin to the center of the actor
        firstPosition.setOrigin(Align.center);

        final float originalX = firstPosition.getX();
        final float originalY = firstPosition.getY();

        firstPosition.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(1.25f, 1.25f, 0.05f, Interpolation.linear),
                        new ShakeAction(0.15f, 10)),

                // reset the scale and position
                Actions.parallel(
                        Actions.scaleTo(1f, 1f, 0.05f, Interpolation.linear),
                        Actions.moveTo(originalX, originalY, 0.05f, Interpolation.linear))));
    }
}
