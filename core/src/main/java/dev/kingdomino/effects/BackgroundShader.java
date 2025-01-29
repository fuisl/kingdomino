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
 * TODO: Javadoc
 * 
 * @author fuisl
 */
public class BackgroundShader {
    private ShaderProgram backgroundShader;
    private Mesh screenQuad;
    private OrthographicCamera camera;
    private GameTimer gameTimer;

    public static HashMap<String, Float> refTable;

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
        refTable.put("u_time", 0f); // control overall color bleeding. CURRENTLY UNUSED
        refTable.put("u_spinTime", 0.0f); // control spining. [-1, 1] should go with the spinAmount
        refTable.put("u_spinAmount", 0.18f); // control the shape of the spin. 0.2f is the sweet spot
        refTable.put("u_contrast", 1.5f); // control contrast
    }

    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        backgroundShader.bind();

        // spin
        backgroundShader.setUniformf("u_resolution", Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        backgroundShader.setUniformf("u_time", gameTimer.backgroundTime);  // always color bleeding
        backgroundShader.setUniformf("u_spinTime", gameTimer.backgroundTime);
        backgroundShader.setUniformf("u_spinAmount", refTable.get("u_spinAmount")); 
        backgroundShader.setUniformf("u_contrast", refTable.get("u_contrast")); 
        
        // color
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

    public void dispose() {
        screenQuad.dispose();
        backgroundShader.dispose();
    }

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
