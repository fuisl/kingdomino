package dev.kingdomino.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameTimer;
import dev.kingdomino.game.TerrainType;

/**
 * The main game screen of Kingdomino.
 * 
 * @author LunaciaDev
 */
public class GameScreen extends AbstractScreen {
    private Stage stage;
    private GameManager gameManager;
    private GameTimer gameTimer;
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
    private OrthographicCamera cam;

    private final boolean SHADER_TOGGLE = false;

    // Shader stuff
    private Mesh screenQuad;
    private ShaderProgram backgroundShader;

    // CRT shader: render everything to frame buffer then apply shader
    private ShaderProgram crtShader;
    private FrameBuffer crtFbo;
    private Mesh crtQuad;
    private float crtValue = 30f; // control intensity of the CRT effect

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
        gameTimer = GameTimer.getInstance();

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

        // shader stuff
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // logging GPU info before shader init
        Gdx.app.log("GPU Info", "Vendor: " + Gdx.gl.glGetString(GL20.GL_VENDOR));
        Gdx.app.log("GPU Info", "Renderer: " + Gdx.gl.glGetString(GL20.GL_RENDERER));
        Gdx.app.log("GPU Info", "Version: " + Gdx.gl.glGetString(GL20.GL_VERSION));
        Gdx.app.log("GPU Info", "GLSL Version: " + Gdx.gl.glGetString(GL20.GL_SHADING_LANGUAGE_VERSION));

        // init the shaders
        initBackgroundShader();
        initCRTShader();
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
            crtFbo.begin();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            renderBackground();
            stage.act(delta);
            stage.draw();
            crtFbo.end();
            
            // apply the CRT shader
            renderCRTShader();
        } else {
            ScreenUtils.clear(Color.DARK_GRAY);
            stage.act(delta);
            stage.draw();
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);

        // resize the CRT shader
        if (crtFbo != null)
            crtFbo.dispose();
        crtFbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);

        float[] newVertices = new float[] {
                0, 0, 0, 0, 0,
                width, 0, 0, 1, 0,
                width, height, 0, 1, 1,
                0, height, 0, 0, 1
        };
        crtQuad.setVertices(newVertices);

        // background shader
        float[] bgVertices = new float[] {
                0, 0, 0, 0, 0,
                width, 0, 0, 1, 0,
                width, height, 0, 1, 1,
                0, height, 0, 0, 1
        };
        screenQuad.setVertices(bgVertices);

        // update camera
        cam.setToOrtho(false, width, height);
        cam.update();
    }

    @Override
    public void dispose() {
        stage.dispose();
        screenQuad.dispose();
        backgroundShader.dispose();
        crtQuad.dispose();
        crtShader.dispose();
        crtFbo.dispose();
    }

    public void renderBackground() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundShader.bind();
        // define the uniform
        backgroundShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundShader.setUniformf("u_time", gameTimer.totalTime); // control color bleeding.
        backgroundShader.setUniformf("u_spinTime", gameTimer.totalTime * 0.0f); // control spining. set to
                                                                                // 0f to stop spining. set
                                                                                // negative to spin in
                                                                                // opposite direction
        backgroundShader.setUniformf("u_contrast", 1.5f);
        backgroundShader.setUniformf("u_spinAmount", 0.2f); // control the shape of the spin
        backgroundShader.setUniformf("u_colour1", Color.valueOf("02394A"));
        backgroundShader.setUniformf("u_colour2", Color.valueOf("043565"));
        backgroundShader.setUniformf("u_colour3", Color.valueOf("5158BB"));

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam.position.set(w / 2f, h / 2f, 0);
        cam.update();

        // 2 triangles to form a quad
        float[] vertices = new float[] {
                0, 0, 0, 0, 0,
                w, 0, 0, 1, 0,
                w, h, 0, 1, 1,
                0, h, 0, 0, 1
        };

        short[] indices = new short[] { 0, 1, 2, 2, 3, 0 };
        screenQuad.setVertices(vertices);
        screenQuad.setIndices(indices);

        // this value is required by LibGDX
        backgroundShader.setUniformMatrix("u_projTrans", cam.combined);
        screenQuad.render(backgroundShader, GL20.GL_TRIANGLES);
    }

    public void initBackgroundShader() {
        String vertexShader = Gdx.files.internal("assets/shaders/background.vert").readString();
        String fragmentShader = Gdx.files.internal("assets/shaders/background.frag").readString();

        backgroundShader = new ShaderProgram(vertexShader, fragmentShader);
        this.screenQuad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        if (!backgroundShader.isCompiled()) {
            System.out.println(backgroundShader.getLog());
        }
    }

    public void initCRTShader() {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        crtFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);

        crtShader = new ShaderProgram(Gdx.files.internal("assets/shaders/crt.vert"),
                Gdx.files.internal("assets/shaders/crt.frag"));

        if (!crtShader.isCompiled()) {
            System.out.println(crtShader.getLog());
        }

        crtQuad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        crtQuad.setVertices(new float[] {
                0, 0, 0, 0, 0,
                w, 0, 0, 1, 0,
                w, h, 0, 1, 1,
                0, h, 0, 0, 1
        });

        crtQuad.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });
    }

    public void renderCRTShader() {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        crtShader.bind();
        crtShader.setUniformf("time", gameTimer.totalTime);

        // distortion
        float distX = 1.0f + 0.07f * (crtValue / 100.0f);
        float distY = 1.0f + 0.10f * (crtValue / 100.0f);
        crtShader.setUniformf("distortion_fac", distX, distY);

        // scale
        float scaleX = 1.0f - 0.008f * (crtValue / 100.0f);
        float scaleY = 1.0f - 0.008f * (crtValue / 100.0f);
        crtShader.setUniformf("scale_fac", scaleX, scaleY);

        // feather
        crtShader.setUniformf("feather_fac", 0.01f);

        // noise
        float noiseFactor = 0.001f * (crtValue / 100.0f);
        crtShader.setUniformf("noise_fac", noiseFactor);

        crtShader.setUniformf("bloom_fac", 1.0f);

        // intensity
        float intensity = 0.16f * (crtValue / 100.0f);
        crtShader.setUniformf("crt_intensity", intensity);

        // glitch
        crtShader.setUniformf("glitch_intensity", 0.2f); // or a higher value

        // scanlines
        crtShader.setUniformf("scanlines", Gdx.graphics.getHeight() * 0.75f);

        // resolution
        crtShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY(); // invert Y if needed
        crtShader.setUniformf("hovering", 1.0f); // or 0.0f to disable
        crtShader.setUniformf("screen_scale", 100f);
        crtShader.setUniformf("mouse_screen_pos", mx, my);

        crtQuad.bind(crtShader);
        crtShader.setUniformMatrix("u_projTrans", cam.combined);

        // bind the frame buffer
        crtFbo.getColorBufferTexture().bind();

        crtQuad.render(crtShader, GL20.GL_TRIANGLES);
    }
}
