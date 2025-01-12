package dev.kingdomino.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
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
    private OrthographicCamera cam = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    // Shader stuff
    private Mesh screenQuad;
    private ShaderProgram shader;

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

        String vertexShader = Gdx.files.internal("assets/shaders/vortex.vert.glsl").readString();
        String fragmentShader = Gdx.files.internal("assets/shaders/background.frag.glsl").readString();

        shader = new ShaderProgram(vertexShader, fragmentShader);
        this.screenQuad = new Mesh(true, 4, 6, new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        if (!shader.isCompiled()) {
            System.out.println(shader.getLog());
        }
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

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        shader.bind();
        // define the uniform
        shader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        shader.setUniformf("u_time", GameTimer.getInstance().totalTime);
        shader.setUniformf("u_spinTime", GameTimer.getInstance().totalTime);
        shader.setUniformf("u_contrast", 1f);
        shader.setUniformf("u_spinAmount", 1f);
        shader.setUniformf("u_colour1", Color.FOREST);
        shader.setUniformf("u_colour2", Color.GREEN);
        shader.setUniformf("u_colour3", Color.CYAN);
        shader.setUniformf("u_vortexAmt", 0.5f);

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();
        cam.position.set(w / 2f, h / 2f, 0);
        cam.update();

        float[] vertices = new float[] {
                0, 0, 0, 0, 0,
                w, 0, 0, 1, 0,
                w, h, 0, 1, 1,
                0, h, 0, 0, 1
        };

        short[] indices = new short[] { 0, 1, 2, 2, 3, 0 };
        screenQuad.setVertices(vertices);
        screenQuad.setIndices(indices);

        shader.setUniformMatrix("u_projTrans", cam.combined);
        screenQuad.render(shader, GL20.GL_TRIANGLES);

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

        // ScreenUtils.clear(Color.DARK_GRAY);
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
        screenQuad.dispose();
    }
}
