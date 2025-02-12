package dev.kingdomino.effects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.VertexAttribute;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import dev.kingdomino.game.GameTimer;

/**
 * Applies a CRT (Cathode Ray Tube) shader effect to the game screen.
 * This effect simulates the look of an old CRT monitor.
 * 
 * @author @fuisl
 * @version 1.0
 * 
 * refactored by @LunaciaDev
 * 
 * @see ShaderProgram
 * @see FrameBuffer
 * @see Mesh
 * @see OrthographicCamera
 * @see GameTimer
 * @see Gdx
 */
public class CRTShader {
    private final ShaderProgram crtShader;
    private FrameBuffer crtFbo;
    private final Mesh crtQuad;
    private final OrthographicCamera camera;
    private final GameTimer gameTimer;
    private float crtValue;

    /**
     * Gets the current CRT effect value.
     * 
     * @return the current CRT effect value.
     */
    public float getCrtValue() {
        return crtValue;
    }

    /**
     * Sets the CRT effect value.
     * 
     * @param crtValue the new CRT effect value.
     */
    public void setCrtValue(float crtValue) {
        this.crtValue = crtValue;
    }

    /**
     * Constructs a new CRTShader.
     * 
     * @param camera the camera used for rendering.
     * @param crtValue the initial CRT effect value.
     */
    public CRTShader(OrthographicCamera camera, float crtValue) {
        int w = Gdx.graphics.getWidth();
        int h = Gdx.graphics.getHeight();

        crtFbo = new FrameBuffer(Pixmap.Format.RGBA8888, w, h, false);

        crtShader = new ShaderProgram(Gdx.files.internal("shaders/crt.vert"),
                Gdx.files.internal("shaders/crt.frag"));

        if (!crtShader.isCompiled()) {
            System.out.println(crtShader.getLog());
        }

        crtQuad = new Mesh(true, 4, 6,
                new VertexAttribute(Usage.Position, 3, "a_position"),
                new VertexAttribute(Usage.TextureCoordinates, 2, "a_texCoord0"));

        crtQuad.setVertices(new float[] {
                0, 0, 0, 0, 0,
                w, 0, 0, 1, 0,
                w, h, 0, 1, 1,
                0, h, 0, 0, 1
        });

        crtQuad.setIndices(new short[] { 0, 1, 2, 2, 3, 0 });

        this.camera = camera;
        this.gameTimer = GameTimer.getInstance();
        this.crtValue = crtValue;
    }

    /**
     * Starts capturing the frame buffer.
     */
    public void startBufferCapture() {
        crtFbo.begin();
    }

    /**
     * Stops capturing the frame buffer.
     */
    public void stopBufferCapture() {
        crtFbo.end();
    }

    /**
     * Applies the CRT effect to the captured frame buffer.
     */
    public void applyCRTEffect() {
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
        crtShader.setUniformf("glitch_intensity", 0.001f); // or a higher value

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

        // update camera before supplying the uniform
        BackgroundManager.updateCameraShakePosition();
        crtShader.setUniformMatrix("u_projTrans", camera.combined);

        // bind the frame buffer
        crtFbo.getColorBufferTexture().bind();

        crtQuad.render(crtShader, GL20.GL_TRIANGLES);
    }

    /**
     * Replaces the frame buffer with a new one of the specified width and height.
     * 
     * @param width the new width of the frame buffer.
     * @param height the new height of the frame buffer.
     */
    public void replaceBuffer(int width, int height) {
        if (crtFbo != null) {
            try {
                FrameBuffer tempFbo = new FrameBuffer(Pixmap.Format.RGBA8888, width, height, false);
                crtFbo.dispose();
                crtFbo = tempFbo;
            } catch (IllegalStateException e) {
                Gdx.app.log("CRT Shader Exception", e.getMessage());
            }
        }

        float[] newVertices = new float[] {
                0, 0, 0, 0, 0,
                width, 0, 0, 1, 0,
                width, height, 0, 1, 1,
                0, height, 0, 0, 1
        };
        crtQuad.setVertices(newVertices);

        // update camera
        camera.setToOrtho(false, width, height);
        camera.update();
    }

    /**
     * Disposes of the resources used by this shader.
     */
    public void dispose() {
        crtQuad.dispose();
        crtShader.dispose();
        crtFbo.dispose();
    }
}
