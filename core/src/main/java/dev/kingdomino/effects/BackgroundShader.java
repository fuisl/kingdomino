package dev.kingdomino.effects;

import java.util.HashMap;

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
 * Applies a background shader effect to the game screen.
 * This effect handles the background rendering with various visual effects.
 * 
 * @author @fuisl
 * @version 1.0
 * 
 * refactored by @LunaciaDev
 * 
 * @see ShaderProgram
 * @see Mesh
 * @see OrthographicCamera
 * @see GameTimer
 * @see Gdx
 */
public class BackgroundShader {
    private final ShaderProgram backgroundShader;
    private final Mesh screenQuad;
    private final OrthographicCamera camera;
    private final GameTimer gameTimer;

    public static HashMap<String, Float> refTable;
    public static HashMap<String, Color> colorTable;

    /**
     * Constructs a new BackgroundShader.
     * 
     * @param camera the camera used for rendering.
     */
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

        // init refTable
        refTable = new HashMap<>();
        colorTable = new HashMap<>();
    }

    /**
     * Renders the background with the shader effect.
     */
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundShader.bind();

        // spin
        backgroundShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundShader.setUniformf("u_time", gameTimer.backgroundTime); // always color bleeding
        backgroundShader.setUniformf("u_spinTime", gameTimer.backgroundTime);
        backgroundShader.setUniformf("u_spinAmount", refTable.get("u_spinAmount"));
        backgroundShader.setUniformf("u_contrast", refTable.get("u_contrast"));

        // color
        BackgroundManager.updateColors();
        backgroundShader.setUniformf("u_colour1", colorTable.get("u_color1"));
        backgroundShader.setUniformf("u_colour2", colorTable.get("u_color2"));
        backgroundShader.setUniformf("u_colour3", colorTable.get("u_color3"));

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

    /**
     * Disposes of the resources used by this shader.
     */
    public void dispose() {
        screenQuad.dispose();
        backgroundShader.dispose();
    }

    /**
     * Changes the vertices of the background quad to match the new width and
     * height.
     * 
     * @param width  the new width of the background.
     * @param height the new height of the background.
     */
    public void changeVertices(int width, int height) {
        // background shader
        float[] bgVertices = new float[] {
                0, 0, 0, 0, 0,
                width, 0, 0, 1, 0,
                width, height, 0, 1, 1,
                0, height, 0, 0, 1
        };
        screenQuad.setVertices(bgVertices);
    }
}
