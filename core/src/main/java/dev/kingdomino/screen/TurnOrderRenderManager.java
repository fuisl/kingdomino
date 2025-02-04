package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.effects.actions.ShakeAnimation;
import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.screen.widgets.WavyLabel;
import dev.kingdomino.game.King;
import dev.kingdomino.game.Turn;

/**
 * A RenderManager specialize in showing the turn order on the left side,
 * automatically updating that based on the current {@link GameState}.
 * 
 * @author LunaciaDev
 */
public class TurnOrderRenderManager extends AbstractRenderManager {
    private Label score;
    private Label.LabelStyle headerStyle;
    private Label.LabelStyle bodyStyle;
    private NinePatchDrawable bezel;
    private NinePatchDrawable whiteBezel;
    private NinePatchDrawable bezelBackground;

    private Table scoreWrapper; // background for the score
    private Table playerScore; // the actual score text

    public TurnOrderRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Label.LabelStyle header,
            Label.LabelStyle body, NinePatchDrawable bezel, NinePatchDrawable whiteBezel,
            NinePatchDrawable bezelBackground) {
        super(gameManager, kingAvatar);

        Label.LabelStyle blackBody = new Label.LabelStyle(body);
        blackBody.fontColor = Color.BLACK;

        score = new Label("[PH]", blackBody);

        this.headerStyle = header;
        this.bodyStyle = body;
        this.bezel = bezel;
        this.whiteBezel = whiteBezel;
        this.bezelBackground = bezelBackground;
    }

    public Table getLayout() {
        Table layout = new Table();
        layout.setBackground(bezelBackground);

        WavyLabel header = new WavyLabel("Turn Order", headerStyle);
        header.setAlignment(Align.center);

        layout.add(header)
                .pad(5);

        layout.row();

        Table currentPlayer = new Table();
        currentPlayer.setBackground(bezel);

        currentPlayer.add(generateContainer(playerIconActors[0]))
                .height(Value.percentHeight(0.33f, layout))
                .width(Value.percentWidth(0.4f, layout))
                .align(Align.center)
                .fill()
                .pad(5);

        playerScore = new Table();

        playerScore.add(new Label("Score", bodyStyle))
                .pad(5);

        playerScore.row();

        scoreWrapper = new Table();
        scoreWrapper.setBackground(whiteBezel);

        scoreWrapper.add(score);

        playerScore.add(scoreWrapper)
                .pad(0, 0, 10, 0);

        currentPlayer.add(playerScore)
                .expandX();

        layout.add(currentPlayer)
                .fill()
                .pad(5);

        layout.row();

        Table upcomingPlayers = new Table();

        upcomingPlayers.add(new Label("Next:", bodyStyle));

        for (int i = 1; i < kingCount; i++) {
            upcomingPlayers.add(generateContainer(playerIconActors[i]))
                    .height(Value.percentHeight(0.15f, layout))
                    .align(Align.center)
                    .expand()
                    .fill();
        }

        layout.add(upcomingPlayers)
                .fill()
                .pad(10, 5, 10, 0);

        layout.invalidate();
        layout.layout();
        return layout;
    }

    protected Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    @Override
    public void act(float delta) {
        King[] currentTurnOrder = gameManager.getCurrentTurn().getKings();
        int currentTurnIndex = gameManager.getCurrentTurn().getCurrentIndex();
        Turn nextTurn = gameManager.getNextTurn();
        int nextTurnIndex = 0;

        score.setText(gameManager.getCurrentKing().getBoard().getScoringSystem().getBoardTotal());

        // TODO figure out how to animate this mess
        for (int i = 0; i < kingCount; i++) {
            if (currentTurnIndex < kingCount) {
                playerIconActors[i].setKingID(currentTurnOrder[currentTurnIndex].getId());
                currentTurnIndex++;
                continue;
            }

            if (nextTurn == null) {
                playerIconActors[i].setKingID(-1);
                continue;
            }

            while (nextTurn.getKings()[nextTurnIndex] == null) {
                nextTurnIndex++;

                // failsafe in case somehow there are less than kingCount in the order queue,
                // which should be impossible.
                if (nextTurnIndex == kingCount) {
                    for (; i < kingCount; i++) {
                        playerIconActors[i].setKingID(-1);
                    }

                    return;
                }
            }

            playerIconActors[i].setKingID(nextTurn.getKings()[nextTurnIndex].getId());
            nextTurnIndex++;
        }
    }

    public void animateScoreWrapper() {
        scoreWrapper.setTransform(true);

        // Set the origin to the center of the actor
        scoreWrapper.setOrigin(Align.center);

        final float originalX = scoreWrapper.getX();
        final float originalY = scoreWrapper.getY();

        scoreWrapper.addAction(Actions.sequence(
                Actions.parallel(
                        Actions.scaleTo(1.25f, 1.25f, 0.05f, Interpolation.linear),
                        new ShakeAnimation(0.15f, 10)),

                // reset the scale and position
                Actions.parallel(
                        Actions.scaleTo(1f, 1f, 0.05f, Interpolation.linear),
                        Actions.moveTo(originalX, originalY, 0.05f, Interpolation.linear))));
    }

}
