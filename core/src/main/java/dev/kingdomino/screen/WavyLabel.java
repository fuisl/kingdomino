package dev.kingdomino.screen;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class WavyLabel extends Label {
    private float elapsedTime = 0f;
    private float amplitude; // Maximum vertical offset in pixels.
    private float frequency; // Oscillation frequency in radians per second.
    private float phaseOffset; // Phase difference between characters (radians).

    /**
     * @param text        The text of the label.
     * @param style       The label style.
     * @param amplitude   Maximum vertical offset (in pixels).
     * @param frequency   Oscillation frequency (radians per second).
     * @param phaseOffset Phase difference between consecutive characters (radians).
     */
    public WavyLabel(CharSequence text, LabelStyle style, float amplitude, float frequency, float phaseOffset) {
        super(text, style);
        this.amplitude = amplitude;
        this.frequency = frequency;
        this.phaseOffset = phaseOffset;
    }

    public WavyLabel(CharSequence text, LabelStyle style) {
        this(text, style, 1.5f, 1.5f, 0.75f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        elapsedTime += delta;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        BitmapFont font = getStyle().font;
        Color color = getColor();
        font.setColor(color.r, color.g, color.b, color.a * parentAlpha);

        // Starting position (y is the baseline)
        float x = getX();
        float y = getY();

        // Correct for the baseline: add the cap height so that the text appears higher.
        float baseY = y + font.getData().capHeight * 1.5f;

        float currentX = x;
        CharSequence text = getText();

        // Draw each character individually using the glyph's xadvance for spacing.
        for (int i = 0; i < text.length(); i++) {
            char ch = text.charAt(i);
            BitmapFont.Glyph glyph = font.getData().getGlyph(ch);
            if (glyph == null)
                continue;

            // Compute a vertical offset for this character.
            float offsetY = amplitude * MathUtils.sin(frequency * elapsedTime + i * phaseOffset);
            // Draw the character at currentX; baseY ensures the text is drawn higher.
            font.draw(batch, String.valueOf(ch), currentX, baseY + offsetY);
            // Advance currentX by the glyph's xadvance.
            currentX += glyph.xadvance;
        }
    }

}
