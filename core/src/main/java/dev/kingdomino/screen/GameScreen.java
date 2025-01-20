package dev.kingdomino.screen;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.TerrainType;

/**
 * The main game screen of Kingdomino.
 * 
 * @author LunaciaDev
 */
public class GameScreen extends AbstractScreen {
    private Stage stage;
    private GameManager gameManager;
    private ScreenViewport screenViewport;
    private TextureRegion[] crownOverlay;
    private TextureRegion[] kingAvatar;
    private MainBoardActor mainBoardActor;
    private SidePanelManager sidePanelManager;
    private TurnOrderRenderManager turnOrderRenderManager;
    private LeaderboardRenderManager leaderboardRenderManager;
    private NextDominoRenderManager nextDominoRenderManager;
    private MainBoardHUDManager mainBoardHUDManager;
    private ControlHintManager controlHintManager;
    private Table rootTable;
    private Skin skin;

    /**
     * Create an instance of GameScreen with an instance of {@link SpriteBatch} and {@link AssetManager}.
     * The class does not generate nor manage the instances of those.
     */
    public GameScreen(SpriteBatch spriteBatch, AssetManager assetManager) {
        super(spriteBatch, assetManager);
        screenViewport = new ScreenViewport();
        stage = new Stage(screenViewport);
        gameManager = new GameManager();

        // TODO remove later, just pinging to get it to be alive... I assume
        // why do you need to be pinged twice...?
        gameManager.update(0f);
        gameManager.update(0f);

        TextureAtlas atlas = assetManager.get("gameTextures.atlas");
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
        nextDominoRenderManager = new NextDominoRenderManager(gameManager, kingAvatar, skin, crownOverlay,
                atlas.findRegion("highlight"));
        sidePanelManager = new SidePanelManager(gameManager, crownOverlay, screenViewport, kingAvatar);
        mainBoardHUDManager = new MainBoardHUDManager(gameManager, kingAvatar, skin);
        controlHintManager = new ControlHintManager(gameManager, skin);
    }
    
    @Override
    public void initScreen() {
        // welcome to the world of Layout-by-Code. Please make yourself at home.
        rootTable = new Table();

        // TODO convert to getLayout instead of the current setLayout.
        Table leftInfoLayout = new Table();
        Table rightInfoLayout = new Table();
        Table mainGameLayout = new Table();
        Table turnOrderLayout = new Table();
        Table leaderboardLayout = new Table();
        Table nextDominoLayout = new Table();
        Table mainBoardHUDLayout = new Table();

        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        stage.addActor(turnOrderRenderManager);
        stage.addActor(leaderboardRenderManager);
        stage.addActor(nextDominoRenderManager);
        stage.addActor(sidePanelManager);
        stage.addActor(mainBoardHUDManager);
        stage.addActor(controlHintManager);

        turnOrderRenderManager.setLayout(turnOrderLayout);

        leftInfoLayout.add(turnOrderLayout)
                .height(Value.percentHeight(0.37f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        leftInfoLayout.row();

        nextDominoRenderManager.setLayout(nextDominoLayout);

        leftInfoLayout.add(nextDominoLayout)
                .height(Value.percentHeight(0.27f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        leaderboardRenderManager.setLayout(leaderboardLayout);

        leftInfoLayout.row();

        leftInfoLayout.add(leaderboardLayout)
                .height(Value.percentHeight(0.27f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        mainBoardHUDManager.setLayout(mainBoardHUDLayout);
        mainGameLayout.add(mainBoardHUDLayout).height(Value.percentHeight(0.08f, mainGameLayout)).expandX().fill();
        mainGameLayout.row();
        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport);
        Container<Actor> container = new Container<>(mainBoardActor);
        container.fill();
        mainGameLayout.add(container).expand().fill();
        mainGameLayout.row();
        controlHintManager.setLayout(mainGameLayout);

        sidePanelManager.setLayout(rightInfoLayout);

        rootTable.add(leftInfoLayout).width(Value.percentWidth(0.14f, rootTable)).expandY().fill();
        rootTable.add(mainGameLayout).expand().fill();
        rootTable.add(rightInfoLayout).width(Value.percentWidth(0.2f, rootTable)).expandY().fill();

        // TODO remove this line once we are done with layout
        stage.setDebugAll(true);
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
        if (gameManager.getCurrentKing() == null)
            return;

        // TODO move to mainBoardActor.act()
        mainBoardActor.setBoard(gameManager.getBoard().getLand());
        mainBoardActor.setCurrentDomino(gameManager.getCurrentDomino());

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
