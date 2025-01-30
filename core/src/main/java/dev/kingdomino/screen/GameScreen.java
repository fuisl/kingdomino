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
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.effects.AudioManager;
import dev.kingdomino.effects.BackgroundManager;
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
    private Table rootTable;

    private MainBoardActor mainBoardActor;
    private SidePanelManager sidePanelManager;
    private TurnOrderRenderManager turnOrderRenderManager;
    private LeaderboardRenderManager leaderboardRenderManager;
    private NextDominoRenderManager nextDominoRenderManager;
    private ControlHintManager controlHintManager;
    private AudioManager audioManager;

    private CRTShader crtShader;
    private BackgroundShader backgroundShader;
    private OrthographicCamera shaderSharedCamera = new OrthographicCamera();

    private NinePatchDrawable leftInfoBackground;
    private NinePatchDrawable rightInfoBackground;
    private NinePatchDrawable bezel;
    private NinePatchDrawable bezelBackground;
    private NinePatchDrawable whiteBezel;

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

        // logging GPU info before shader init.
        // TODO move to loading screen, if I managed to get there in time
        // UPDATE: init before update GameManager to start all timers.
        Gdx.app.log("GPU Info", "Vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
        Gdx.app.log("GPU Info", "Renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
        Gdx.app.log("GPU Info", "Version: " + Gdx.gl.glGetString(GL20.GL_VERSION));
        Gdx.app.log("GPU Info", "GLSL Version: " + Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

        this.backgroundShader = new BackgroundShader(shaderSharedCamera);
        this.crtShader = new CRTShader(shaderSharedCamera, 25f);

        // important for screenshake
        BackgroundManager.setCamera(shaderSharedCamera);

        // load audio
        audioManager = AudioManager.getInstance();
        audioManager.load();
        audioManager.playMusic();

        // TODO remove later, just pinging to get it to be alive... I assume
        // why do you need to be pinged twice...?
        gameManager.update(0f);
        gameManager.update(0f);

        TextureAtlas atlas = assetManager.get("gameTextures.atlas");

        for (TerrainType name : TerrainType.values()) {
            if (name == TerrainType.CASTLE) {
                TextureRegion[] castleTextures = new TextureRegion[4];
                castleTextures[0] = atlas.findRegion("blueCastle");
                castleTextures[1] = atlas.findRegion("greenCastle");
                castleTextures[2] = atlas.findRegion("pinkCastle");
                castleTextures[3] = atlas.findRegion("yellowCastle");

                name.setCastleTexture(castleTextures);
                continue;
            }

            name.setTexture(atlas.findRegion(name.name().toLowerCase()));
        }

        Label.LabelStyle headerStyle = new Label.LabelStyle();
        headerStyle.font = assetManager.get("PixelifySansHeader.fnt");
        headerStyle.fontColor = Color.WHITE;

        Label.LabelStyle bodyStyle = new Label.LabelStyle();
        bodyStyle.font = assetManager.get("PixelifySansBody.fnt");
        bodyStyle.fontColor = Color.WHITE;

        TextureRegion[] crownOverlay;
        TextureRegion[] kingAvatar;

        crownOverlay = new TextureRegion[4];
        crownOverlay[0] = atlas.findRegion("noCrown");
        crownOverlay[1] = atlas.findRegion("oneCrown");
        crownOverlay[2] = atlas.findRegion("twoCrown");
        crownOverlay[3] = atlas.findRegion("threeCrown");

        kingAvatar = new TextureRegion[4];
        kingAvatar[0] = atlas.findRegion("blueKing");
        kingAvatar[1] = atlas.findRegion("greenKing");
        kingAvatar[2] = atlas.findRegion("pinkKing");
        kingAvatar[3] = atlas.findRegion("yellowKing");

        leftInfoBackground = new NinePatchDrawable(atlas.createPatch("leftTable"));
        rightInfoBackground = new NinePatchDrawable(atlas.createPatch("rightTable"));
        bezel = new NinePatchDrawable(atlas.createPatch("bezel"));
        whiteBezel = new NinePatchDrawable(atlas.createPatch("whiteBezel"));
        bezelBackground = new NinePatchDrawable(atlas.createPatch("bezelBackground"));

        turnOrderRenderManager = new TurnOrderRenderManager(gameManager, kingAvatar, headerStyle, bodyStyle, bezel,
                whiteBezel, bezelBackground);
        leaderboardRenderManager = new LeaderboardRenderManager(gameManager, kingAvatar, headerStyle, bodyStyle, bezel,
                bezelBackground);
        nextDominoRenderManager = new NextDominoRenderManager(gameManager, kingAvatar, headerStyle, crownOverlay,
                bezel, whiteBezel, bezelBackground);
        sidePanelManager = new SidePanelManager(gameManager, crownOverlay, screenViewport, kingAvatar);
        controlHintManager = new ControlHintManager(gameManager, bodyStyle);
        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport, gameManager);
    }

    @Override
    public void initScreen() {
        // welcome to the world of Layout-by-Code. Please make yourself feel at home.
        rootTable = new Table();
        rootTable.setFillParent(true);

        Table leftInfoLayout = new Table();
        Table mainGameLayout = new Table();

        /**
         * Add non-drawing Actors to the system
         */

        stage.addActor(rootTable);
        stage.addActor(turnOrderRenderManager);
        stage.addActor(leaderboardRenderManager);
        stage.addActor(nextDominoRenderManager);
        stage.addActor(sidePanelManager);
        stage.addActor(controlHintManager);

        /**
         * Left Information Layout
         */

        leftInfoLayout.setBackground(leftInfoBackground);

        leftInfoLayout.add(turnOrderRenderManager.getLayout())
                .fill()
                .pad(10);

        leftInfoLayout.row();

        leftInfoLayout.add(nextDominoRenderManager.getLayout())
                .fill()
                .pad(10);

        leftInfoLayout.row();

        leftInfoLayout.add(leaderboardRenderManager.getLayout())
                .fill()
                .pad(10);

        /**
         * Central Game Layout
         */

        Container<Actor> container = new Container<>(mainBoardActor);
        container.fill();

        mainGameLayout.add(container)
                .maxHeight(Value.percentHeight(0.92f, mainGameLayout))
                .minHeight(Value.percentHeight(0.5f, mainGameLayout))
                .prefHeight(Value.percentHeight(0.88f, mainGameLayout))
                .expand()
                .fill();

        mainGameLayout.row();

        controlHintManager.setLayout(mainGameLayout);

        /**
         * Right Information Layout
         */

        Table rightInfoLayout = sidePanelManager.getLayout();
        rightInfoLayout.setBackground(rightInfoBackground);

        /**
         * Packing Everything into rootTable
         */

        rootTable.add(leftInfoLayout)
                .expandY()
                .fill();

        rootTable.add(mainGameLayout)
                .expand()
                .fill()
                .minHeight(Value.percentHeight(1f, rootTable))
                .maxHeight(Value.percentHeight(1f, rootTable));

        rootTable.add(rightInfoLayout)
                .width(Value.percentWidth(0.18f, rootTable))
                .expandY()
                .fill();
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

        // update the shaders
        crtShader.replaceBuffer(width, height);
        backgroundShader.changeVertices(width, height);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundShader.dispose();
        crtShader.dispose();
    }
}
