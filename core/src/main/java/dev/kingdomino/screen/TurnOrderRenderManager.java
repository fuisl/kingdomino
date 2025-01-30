package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameManager.GameState;
import dev.kingdomino.game.King;

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
        layout.background(bezelBackground);

        Label header = new Label("Current Player", headerStyle);
        header.setAlignment(Align.center);

        layout.add(header)
                .pad(10);

        layout.row();

        Table currentPlayer = new Table();
        currentPlayer.background(bezel);

        currentPlayer.add(generateContainer(playerIconActors[0]))
                .height(Value.percentHeight(0.33f, layout))
                .width(Value.percentWidth(0.4f, layout))
                .align(Align.center)
                .fill()
                .pad(5);

        Table playerScore = new Table();

        playerScore.add(new Label("Score", bodyStyle))
                .pad(5);

        playerScore.row();

        Table scoreWrapper = new Table();
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

        return layout;
    }

    protected Container<Actor> generateContainer(Actor actor) {
        Container<Actor> containerizedActor = new Container<>(actor);
        containerizedActor.fill();
        return containerizedActor;
    }

    @Override
    public void act(float delta) {
        if (gameManager.getNextTurn() == null)
            return;

        King[] currentTurnOrder = gameManager.getCurrentTurn().getKings();
        int currentTurnIndex = gameManager.getCurrentTurn().getCurrentIndex();
        King[] nextTurnOrder = gameManager.getNextTurn().getKings();
        int nextTurnIndex = 0;

        score.setText(gameManager.getCurrentKing().getBoard().getScoringSystem().getBoardTotal());

        // TODO figure out how to animate this mess
        for (int i = 0; i < kingCount; i++) {
            if (currentTurnIndex < kingCount) {
                playerIconActors[i].setKingID(currentTurnOrder[currentTurnIndex].getId());
                currentTurnIndex++;
                continue;
            }

            while (nextTurnOrder[nextTurnIndex] == null) {
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

            playerIconActors[i].setKingID(nextTurnOrder[nextTurnIndex].getId());
            nextTurnIndex++;
        }
    }
}
