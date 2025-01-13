package dev.kingdomino.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.TerrainType;

public class GameScreen extends AbstractScreen {
    private Stage stage;
    private GameManager gameManager;
    private ScreenViewport screenViewport;
    private TextureRegion[] crownOverlay;
    private TextureRegion[] kingAvatar;
    private MainBoardActor mainBoardActor;
    private SideBoardManager sideBoardManager;
    private TurnOrderRenderManager turnOrderRenderManager;
    private LeaderboardRenderManager leaderboardRenderManager;
    private NextDominoRenderManager nextDominoRenderManager;
    private Table rootTable;
    private Skin skin;

    public GameScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch, assetManager);
        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport);
        gameManager = new GameManager();

        // TODO remove later, just pinging to get it to be alive... I assume
        // why do you need to be pinged twice...?
        gameManager.update(0f);
        gameManager.update(0f);
        
        TextureAtlas atlas = assetManager.get("tileTextures.atlas");
        skin = assetManager.get("skin/uiskin.json");

        for (TerrainType name : TerrainType.values()) {
            name.setTexture(atlas.findRegion(name.name().toLowerCase()));
        }

        crownOverlay = new TextureRegion[4];
        crownOverlay[0] = atlas.findRegion("nocrown");
        crownOverlay[1] = atlas.findRegion("onecrown");
        crownOverlay[2] = atlas.findRegion("twocrown");
        crownOverlay[3] = atlas.findRegion("threecrown");

        kingAvatar = new TextureRegion[4];
        kingAvatar[0] = atlas.findRegion("kingOne");
        kingAvatar[1] = atlas.findRegion("kingTwo");
        kingAvatar[2] = atlas.findRegion("kingThree");
        kingAvatar[3] = atlas.findRegion("kingFour");

        turnOrderRenderManager = new TurnOrderRenderManager(gameManager, kingAvatar, skin);
        leaderboardRenderManager = new LeaderboardRenderManager(gameManager, kingAvatar, skin);
        nextDominoRenderManager = new NextDominoRenderManager(gameManager, kingAvatar, skin);
        sideBoardManager = new SideBoardManager(gameManager, crownOverlay, screenViewport);
    }

    @Override
    public void initScreen() {
        rootTable = new Table();
        Table leftInfoLayout = new Table();
        Table rightInfoLayout = new Table();
        Table mainGameLayout = new Table();
        Table turnOrderLayout = new Table();
        Table leaderboardLayout = new Table();
        Table nextDominoLayout = new Table();

        rootTable.setFillParent(true);
        // TODO remove this line once we are done with layout
        rootTable.setDebug(true);
        leftInfoLayout.setDebug(true);
        rightInfoLayout.setDebug(true);
        mainGameLayout.setDebug(true);
        stage.addActor(rootTable);

        leftInfoLayout.add(new Label("Turn Order", skin));
        leftInfoLayout.row();
        turnOrderRenderManager.setLayout(turnOrderLayout);
        leftInfoLayout.add(turnOrderLayout).expand().fill();
        leftInfoLayout.row();
        leftInfoLayout.add(new Label("Leaderboard", skin));
        leftInfoLayout.row();
        leaderboardRenderManager.setLayout(leaderboardLayout);
        leftInfoLayout.add(leaderboardLayout).expand().fill();
        leftInfoLayout.row();
        leftInfoLayout.add(new Label("Next Dominoes", skin));
        leftInfoLayout.row();
        nextDominoRenderManager.setLayout(nextDominoLayout);
        leftInfoLayout.add(nextDominoLayout).expand().fill();

        // TODO replace with actual player render
        mainGameLayout.add(new Label("Main Game Area", skin));
        mainGameLayout.row();
        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport);
        Container<Actor> container = new Container<>(mainBoardActor);
        container.fill();
        mainGameLayout.add(container).expand().fill();
        mainGameLayout.row();
        mainGameLayout.add(new Label("Control Hints", skin));

        // TODO replace with other player renders
        rightInfoLayout.add(new Label("Right Info", skin));
        rightInfoLayout.row();
        
        for (SideBoardActor actor : sideBoardManager.getSideBoardActors()) {
            container = new Container<>(actor);
            container.fill();
            rightInfoLayout.add(container).expand().fill();
            rightInfoLayout.row();
        }

        rootTable.add(leftInfoLayout).width(Value.percentWidth(0.15f, rootTable)).expandY().fill();
        rootTable.add(mainGameLayout).expand().fill();
        rootTable.add(rightInfoLayout).width(Value.percentWidth(0.21f, rootTable)).expandY().fill();
    }

    @Override
    public void render(float delta) {
        gameManager.update(delta);

        // TODO remove this as we are now rendering from the UI
        // keep for debugging purpose only.
        gameManager.render(spriteBatch);

        // update the actors with new informations
        // TODO remove this once we have a proper screen covering this part
        // in theory we can supplement a default board to deal with
        if (gameManager.getCurrentKing() == null) return;

        sideBoardManager.updateSideBoard();
        mainBoardActor.setBoard(gameManager.getBoard().getLand());
        mainBoardActor.setCurrentDomino(gameManager.getCurrentDomino());
        turnOrderRenderManager.informActors();
        leaderboardRenderManager.informActors();
        nextDominoRenderManager.informActors();

        ScreenUtils.clear(Color.DARK_GRAY);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}
