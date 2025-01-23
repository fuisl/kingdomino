package dev.kingdomino.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

import dev.kingdomino.effects.BackgroundShader;
import dev.kingdomino.effects.CRTShader;
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

    private CRTShader crtShader;
    private BackgroundShader backgroundShader;

    // TODO: Allow this value to be changed, if I can get there...
    private final boolean SHADER_TOGGLE = true;

    /**
     * Create an instance of GameScreen with an instance of {@link SpriteBatch} and
     * {@link AssetManager}. The class does not generate nor manage the instances of
     * those.
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
        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport, gameManager);

        // logging GPU info before shader init
        // TODO move to loading screen, if I managed to get there in time
        Gdx.app.log("GPU Info", "Vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
        Gdx.app.log("GPU Info", "Renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
        Gdx.app.log("GPU Info", "Version: " + Gdx.gl.glGetString(GL20.GL_VERSION));
        Gdx.app.log("GPU Info", "GLSL Version: " + Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

        OrthographicCamera shaderSharedCamera = new OrthographicCamera();

        this.backgroundShader = new BackgroundShader(shaderSharedCamera);
        this.crtShader = new CRTShader(shaderSharedCamera);
    }

    @Override
    public void initScreen() {
        // welcome to the world of Layout-by-Code. Please make yourself at home.
        rootTable = new Table();

        Table leftInfoLayout = new Table();
        Table mainGameLayout = new Table();

        rootTable.setFillParent(true);
        stage.addActor(rootTable);
        stage.addActor(turnOrderRenderManager);
        stage.addActor(leaderboardRenderManager);
        stage.addActor(nextDominoRenderManager);
        stage.addActor(sidePanelManager);
        stage.addActor(mainBoardHUDManager);
        stage.addActor(controlHintManager);

        leftInfoLayout.add(turnOrderRenderManager.getLayout())
                .height(Value.percentHeight(0.37f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        leftInfoLayout.row();

        leftInfoLayout.add(nextDominoRenderManager.getLayout())
                .height(Value.percentHeight(0.27f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        leftInfoLayout.row();

        leftInfoLayout.add(leaderboardRenderManager.getLayout())
                .height(Value.percentHeight(0.27f, leftInfoLayout))
                .expandX()
                .fill()
                .pad(15);

        mainGameLayout.add(mainBoardHUDManager.getLayout())
                .height(Value.percentHeight(0.08f, mainGameLayout))
                .expandX()
                .fill();

        mainGameLayout.row();

        Container<Actor> container = new Container<>(mainBoardActor);
        container.fill();
        mainGameLayout.add(container).expand().fill();
        mainGameLayout.row();

        controlHintManager.setLayout(mainGameLayout);

        rootTable.add(leftInfoLayout)
                .width(Value.percentWidth(0.14f, rootTable))
                .expandY()
                .fill();

        rootTable.add(mainGameLayout)
                .expand()
                .fill();

        rootTable.add(sidePanelManager.getLayout())
                .width(Value.percentWidth(0.2f, rootTable))
                .expandY()
                .fill();

        // TODO remove this line once we are done with layout
        stage.setDebugAll(true);
    }

    @Override
    public void render(float delta) {
        gameManager.update(delta);

        // update the actors with new informations
        // TODO remove this once we have a proper screen covering this part
        // in theory we can supplement a default board to deal with
        if (gameManager.getCurrentKing() == null)
            return;

        // renderBackground();
        if (SHADER_TOGGLE) {
            // wrap everything in a buffer
            crtShader.startBufferCapture();
            backgroundShader.render();
            stage.act(delta);
            stage.draw();
            crtShader.stopBufferCapture();
            
            // apply the CRT shader
            crtShader.applyCRTEffect();
        } else {
            ScreenUtils.clear(Color.DARK_GRAY);
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        crtShader.replaceBuffer(width, height, backgroundShader.getScreenQuad());
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundShader.dispose();
        crtShader.dispose();
    }
}
