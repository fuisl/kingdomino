package dev.kingdomino.screen;

import java.util.Random;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class WavyLabel extends Label {   
    private WaveAnimation waveAnimation;

    /**
     * Create a label that will wave indefinitely.
     * 
     * @param text Label text
     * @param style Label style
     * @param maxHeight Maximum y offset of label
     * @param duration How long it take for a single character to wave
     * @param characterTimeOffset Time offset between character next to each other
     * 
     * @author fuisl
     */
    public WavyLabel(CharSequence text, LabelStyle style, float maxHeight, float duration, float characterTimeOffset) {
        super(text, style);
        this.waveAnimation = new WaveAnimation(maxHeight, duration, characterTimeOffset);
        this.addAction(waveAnimation);
    }

    public WavyLabel(CharSequence text, LabelStyle style) {
        this(text, style, 1.5f, 4f, 0.5f);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
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
            float offsetY = waveAnimation.getHeight(i);
            // Draw the character at currentX; baseY ensures the text is drawn higher.
            font.draw(batch, String.valueOf(ch), currentX, baseY + offsetY);
            // Advance currentX by the glyph's xadvance.
            currentX += glyph.xadvance;
        }
    }

    private class WaveAnimation extends Action {
        private float maxHeight;
        private float duration;
        private float timeOffset;
        
        private float elapsedTime;

        public WaveAnimation(float maxHeight, float duration, float timeOffset) {
            this.maxHeight = maxHeight;
            this.duration = duration;
            this.timeOffset = timeOffset;

            this.elapsedTime = new Random().nextFloat(duration);
        }

        @Override
        public boolean act(float dt) {
            // basically a clock, as we need to lookup the sine wave when getting height
            elapsedTime += dt;

            if (elapsedTime > duration) {
                elapsedTime -= duration;
            }

            // always ticking
            return false;
        }

        public float getHeight(int offsetScale) {
            return maxHeight * MathUtils.sinDeg((elapsedTime + offsetScale * timeOffset) / duration * 360);
        }
    }
}
