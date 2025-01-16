package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.King;

public class LeaderboardRenderManager extends AbstractRenderManager {
    private Label[] pointLabels;

    public LeaderboardRenderManager(GameManager gameManager, TextureRegion[] kingAvatar, Skin skin) {
        super(gameManager, kingAvatar, skin);
        pointLabels = new Label[kingCount];
        
        for (int i = 0; i < kingCount; i++) {
            pointLabels[i] = new Label("0pt", skin);
        }
    }

    @Override
    public void setLayout(Table turnOrderLayout) {
        String[] label = {"1st", "2nd", "3rd", "4th"};

        for (int i = 0; i < kingCount; i++) {
            turnOrderLayout.add(new Label(label[i], skin)).expand().fill();
            turnOrderLayout.add(generateContainer(playerIconActors[i])).expand().fill();
            turnOrderLayout.add(pointLabels[i]).expand().fill();
            turnOrderLayout.row();
        }
    }

    @Override
    public void informActors() {
        // TODO nab a sorted version of this.
        King[] kings = gameManager.getAllKing();

        int position = 0;
        for (King king : kings) {
            playerIconActors[position].setKingID(king.getId());
            pointLabels[position].setText(king.getBoard().getScoringSystem().getBoardTotal() + "pt");
            position++;
        }
    }
}
