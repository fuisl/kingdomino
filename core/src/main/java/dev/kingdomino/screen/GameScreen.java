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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import dev.kingdomino.game.GameManager;
import dev.kingdomino.game.GameTimer;
import dev.kingdomino.game.TerrainType;

public class GameScreen extends AbstractScreen {
    private Stage stage;
    private GameManager gameManager;
    private ScreenViewport screenViewport;
    private TextureRegion[] crownOverlay;
    private MainBoardActor mainBoardActor;
    private SideBoardManager sideBoardManager;
    private Table rootTable;
    private Skin skin;
    private OrthographicCamera cam;

    // Shader stuff
    private Mesh screenQuad;
    private ShaderProgram backgroundShader;
    private ShaderProgram crtShader;
    private FrameBuffer crtFbo;
    private Mesh crtQuad;

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

        // shader stuff
        initBackgroundShader();
        initCRTShader();
        cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void initScreen() {
        rootTable = new Table();
        Table leftInfoLayout = new Table();
        Table rightInfoLayout = new Table();
        Table mainGameLayout = new Table();

        rootTable.setFillParent(true);
        // TODO remove this line once we are done with layout
        rootTable.setDebug(true);
        leftInfoLayout.setDebug(true);
        rightInfoLayout.setDebug(true);
        mainGameLayout.setDebug(true);
        stage.addActor(rootTable);

        leftInfoLayout.add(new Label("Left Info", skin));
        leftInfoLayout.row();
        mainGameLayout.add(new Label("Main Game Area", skin));
        mainGameLayout.row();
        rightInfoLayout.add(new Label("Right Info", skin));
        rightInfoLayout.row();

        mainBoardActor = new MainBoardActor(crownOverlay, screenViewport);
        sideBoardManager = new SideBoardManager(gameManager, crownOverlay, screenViewport);

        // Better than implementing Layout interface... that is one massive interface.
        Container<Actor> container = new Container<>(mainBoardActor);
        container.fill();
        mainGameLayout.add(container).expand().fill();

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
        if (gameManager.getCurrentKing() == null)
            return;

        sideBoardManager.updateSideBoard();
        mainBoardActor.setBoard(gameManager.getBoard().getLand());
        mainBoardActor.setCurrentDomino(gameManager.getCurrentDomino());

        // renderBackground();
        // // ScreenUtils.clear(Color.DARK_GRAY);
        // stage.act(delta);
        // stage.draw();

        renderCRTShader(delta);
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

        cam.setToOrtho(false, width, height);
        cam.update();

        // background shader
        float[] bgVertices = new float[] {
                0, 0, 0, 0, 0,
                width, 0, 0, 1, 0,
                width, height, 0, 1, 1,
                0, height, 0, 0, 1
        };
        screenQuad.setVertices(bgVertices);
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
        backgroundShader.setUniformf("u_time", GameTimer.getInstance().totalTime); // control color bleeding.
        backgroundShader.setUniformf("u_spinTime", GameTimer.getInstance().totalTime * 0.0f); // control spining. set to
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

        // this is where bug begin, maybe another matrix would do the work but for now,
        // it's good enough.
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
        String vertexShader = Gdx.files.internal("assets/shaders/vortex.vert.glsl").readString();
        String fragmentShader = Gdx.files.internal("assets/shaders/background.frag.glsl").readString();

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

    public void renderCRTShader(float delta) {
        crtFbo.begin();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderBackground();
        stage.act(delta);
        stage.draw();

        crtFbo.end();

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        crtShader.bind();
        crtShader.setUniformf("time", GameTimer.getInstance().totalTime);
        crtShader.setUniformf("distortion_fac", 1.07f, 1.10f);
        crtShader.setUniformf("scale_fac", 0.992f, 0.992f);
        crtShader.setUniformf("feather_fac", 0.01f);
        crtShader.setUniformf("noise_fac", 0.001f);
        crtShader.setUniformf("bloom_fac", 1.0f); // tweak as needed
        crtShader.setUniformf("crt_intensity", 0.16f);
        crtShader.setUniformf("glitch_intensity", 0.0f); // or a higher value
        crtShader.setUniformf("scanlines", Gdx.graphics.getHeight() * 0.75f);

        crtShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        float mx = Gdx.input.getX();
        float my = Gdx.graphics.getHeight() - Gdx.input.getY(); // invert Y if needed
        crtShader.setUniformf("hovering", 0.0f); // or 0.0f to disable
        crtShader.setUniformf("screen_scale", 100f);
        crtShader.setUniformf("mouse_screen_pos", mx, my);

        crtQuad.bind(crtShader);
        crtShader.setUniformMatrix("u_projTrans", cam.combined);

        crtFbo.getColorBufferTexture().bind();

        crtQuad.render(crtShader, GL20.GL_TRIANGLES);
    }
}
