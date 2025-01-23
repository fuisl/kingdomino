package dev.kingdomino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import dev.kingdomino.game.GameTimer;

/**
 * TODO: Javadoc
 * 
 * @author fuisl
 */
public class BackgroundShader {
    private ShaderProgram backgroundShader;
    private Mesh screenQuad;
    private OrthographicCamera camera;
    private GameTimer gameTimer;

    public BackgroundShader(OrthographicCamera camera) {
        // These assets need its own specific loader if I want to delegate it to
        // AssetManager. Too much work! - @LunaciaDev
        String vertexShader = Gdx.files.internal("shaders/background.vert").readString();
        String fragmentShader = Gdx.files.internal("shaders/background.frag").readString();

        backgroundShader = new ShaderProgram(vertexShader, fragmentShader);
        this.screenQuad = new Mesh(true, 4, 6,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        if (!backgroundShader.isCompiled()) {
            System.out.println(backgroundShader.getLog());
        }

        this.camera = camera;
        this.gameTimer = GameTimer.getInstance();
    }

    public void render() {
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
        camera.position.set(w / 2f, h / 2f, 0);
        camera.update();

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
        backgroundShader.setUniformMatrix("u_projTrans", camera.combined);
        screenQuad.render(backgroundShader, GL20.GL_TRIANGLES);
    }

    public Mesh getScreenQuad() {
        return screenQuad;
    }

    public void dispose() {
        screenQuad.dispose();
        backgroundShader.dispose();
    }
}
