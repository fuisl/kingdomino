#ifdef GL_ES
precision mediump float;
#endif

// Varying passed from the vertex shader
varying vec2 v_texCoord;

// The main texture sampler
uniform sampler2D u_texture;

// Uniforms matching your original snippet
uniform float time;
uniform vec2  distortion_fac;
uniform vec2  scale_fac;
uniform float feather_fac;
uniform float noise_fac;
uniform float bloom_fac;
uniform float crt_intensity;
uniform float glitch_intensity;
uniform float scanlines;
uniform vec2  u_resolution;

// Define constants for the bloom loop, etc.
#define BUFF       0.01
#define BLOOM_AMT  3

void main()
{
    // Original texture coords
    vec2 tc = v_texCoord;

    //-------------------------------------------------------------
    // 1) Recenter and scale for "barrel" distortion
    //-------------------------------------------------------------
    // Rescale from [0,1] → [-1,1]
    tc = tc * 2.0 - vec2(1.0);
    // Apply scale factor
    tc *= scale_fac;
    // Bulge from the middle
    tc += (tc.yx * tc.yx) * tc * (distortion_fac - 1.0);

    //-------------------------------------------------------------
    // 2) Smoothly transition edges to black
    //-------------------------------------------------------------
    float mask =
          (1.0 - smoothstep(1.0 - feather_fac, 1.0, abs(tc.x) - BUFF))
        * (1.0 - smoothstep(1.0 - feather_fac, 1.0, abs(tc.y) - BUFF));

    //-------------------------------------------------------------
    // 3) Undo the recenter back to [0,1]
    //-------------------------------------------------------------
    tc = (tc + vec2(1.0)) / 2.0;

    //-------------------------------------------------------------
    // 4) Glitch offsets
    //-------------------------------------------------------------
    float offset_l = 0.0;
    float offset_r = 0.0;
    if (glitch_intensity > 0.01) {
        float timefac = 3.0 * time;
        
        // We sample multiple sin() frequencies to get a wiggly, glitchy offset
        offset_l = 50.0 * (
             -3.5 + sin(timefac * 0.512  + tc.y * 40.0)
                   + sin(-timefac * 0.8233 + tc.y * 81.532)
                   + sin(timefac * 0.333  + tc.y * 30.3)
                   + sin(-timefac * 0.1112331 + tc.y * 13.0)
        );
        offset_r = -50.0 * (
             -3.5 + sin(timefac * 0.6924  + tc.y * 29.0)
                   + sin(-timefac * 0.9661  + tc.y * 41.532)
                   + sin(timefac * 0.4423  + tc.y * 40.3)
                   + sin(-timefac * 0.13321312 + tc.y * 11.0)
        );

        // Additional pass if glitch_intensity > 1
        if (glitch_intensity > 1.0) {
            offset_l = 50.0 * (
                 -1.5 + sin(timefac * 0.512   + tc.y * 4.0)
                       + sin(-timefac * 0.8233  + tc.y * 1.532)
                       + sin(timefac * 0.333   + tc.y * 3.3)
                       + sin(-timefac * 0.1112331 + tc.y * 1.0)
            );
            offset_r = -50.0 * (
                 -1.5 + sin(timefac * 0.6924   + tc.y * 19.0)
                       + sin(-timefac * 0.9661   + tc.y * 21.532)
                       + sin(timefac * 0.4423   + tc.y * 20.3)
                       + sin(-timefac * 0.13321312 + tc.y * 5.0)
            );
        }

        // Apply the offset in the X direction
        tc.x += 0.001 * glitch_intensity * clamp(offset_l, clamp(offset_r, -1.0, 0.0), 1.0);
    }

    //-------------------------------------------------------------
    // 5) Sample the texture after distortion
    //-------------------------------------------------------------
    vec4 crt_tex = texture2D(u_texture, tc);

    //-------------------------------------------------------------
    // 6) Artifact amplifier (some lines produce bigger glitch colors)
    //-------------------------------------------------------------
    float artifact_amplifier =
        (abs(clamp(offset_l, clamp(offset_r, -1.0, 0.0), 1.0)) * glitch_intensity > 0.9)
        ? 3.0
        : 1.0;

    //-------------------------------------------------------------
    // 7) Horizontal Chromatic Aberration
    //-------------------------------------------------------------
    float crt_amount_adjusted = max(0.0, (crt_intensity) / (0.16 * 0.3)) * artifact_amplifier;
    if (crt_amount_adjusted > 1e-7) {
        // Adjust color channels horizontally
        float shift = 0.0005 * (1.0 + 10.0 * (artifact_amplifier - 1.0))
                            * (1600.0 / u_resolution.x);
        // Shift red one way
        crt_tex.r = mix(crt_tex.r, texture2D(u_texture, tc + vec2( shift, 0.0)).r, crt_amount_adjusted);
        // Shift green the other way
        crt_tex.g = mix(crt_tex.g, texture2D(u_texture, tc + vec2(-shift, 0.0)).g, crt_amount_adjusted);
    }

    // Factor out a portion of brightness by CRT intensity
    vec3 rgb_result = crt_tex.rgb * (1.0 - (1.0 * crt_intensity * artifact_amplifier));

    //-------------------------------------------------------------
    // 8) Extra red/green glitch lines if sin(...) is large
    //-------------------------------------------------------------
    if (sin(time + tc.y * 200.0) > 0.85) {
        if (offset_l < 0.99 && offset_l > 0.01) {
            // Make red pop
            rgb_result.r = rgb_result.g * 1.5;
        }
        if (offset_r > -0.99 && offset_r < -0.01) {
            // Make green pop
            rgb_result.g = rgb_result.r * 1.5;
        }
    }

    //-------------------------------------------------------------
    // 9) Pixel scanlines overlay
    //-------------------------------------------------------------
    // This does not sample from the main image but adds lines horizontally & vertically
    vec3 rgb_scanline = vec3(
        clamp(
            -0.3 + 2.0 * sin(tc.y * scanlines - 3.14 / 4.0)
                - 0.8 * clamp(sin(tc.x * scanlines * 4.0), 0.4, 1.0),
            -1.0, 2.0
        ),
        clamp(
            -0.3 + 2.0 * cos(tc.y * scanlines)
                - 0.8 * clamp(cos(tc.x * scanlines * 4.0), 0.0, 1.0),
            -1.0, 2.0
        ),
        clamp(
            -0.3 + 2.0 * cos(tc.y * scanlines - 3.14 / 3.0)
                - 0.8 * clamp(cos(tc.x * scanlines * 4.0 - 3.14 / 4.0), 0.0, 1.0),
            -1.0, 2.0
        )
    );
    // Accumulate that overlay
    rgb_result += crt_tex.rgb * rgb_scanline * crt_intensity * artifact_amplifier;

    //-------------------------------------------------------------
    // 10) Noise overlay
    //-------------------------------------------------------------
    float x = (tc.x - mod(tc.x, 0.002)) * (tc.y - mod(tc.y, 0.0013)) * time * 1000.0;
    x = mod(x, 13.0) * mod(x, 123.0);
    float dx = mod(x, 0.11) / 0.11;
    float noise_amount = clamp(noise_fac * artifact_amplifier, 0.0, 1.0);
    rgb_result = (1.0 - noise_amount) * rgb_result + dx * noise_amount * vec3(1.0, 1.0, 1.0);

    //-------------------------------------------------------------
    // 11) Contrast/Brightness correction
    //-------------------------------------------------------------
    // Also tweak brightness for bloom
    rgb_result -= vec3(
        0.55
        - 0.02 * (artifact_amplifier - 1.0 - crt_amount_adjusted * bloom_fac * 0.7)
    );
    rgb_result *=
        (1.0 + 0.14 + crt_amount_adjusted * (0.012 - bloom_fac * 0.12));
    rgb_result += vec3(0.5);

    // This is our main color so far
    vec4 final_col = vec4(rgb_result, 1.0);

    //-------------------------------------------------------------
    // 12) Bloom pass
    //-------------------------------------------------------------
    vec4 col = vec4(0.0);
    float bloom = 0.0;

    if (bloom_fac > 0.00001 && crt_intensity > 0.000001) {
        bloom = 0.03 * max(0.0, (crt_intensity) / (0.16 * 0.3));
        float bloom_dist = 0.0015 * float(BLOOM_AMT);
        float cutoff = 0.6;

        // Simple gather around the pixel
        for (int i = -BLOOM_AMT; i <= BLOOM_AMT; ++i) {
            for (int j = -BLOOM_AMT; j <= BLOOM_AMT; ++j) {
                vec2 sampleCoord = tc + (bloom_dist / float(BLOOM_AMT)) * vec2(float(i), float(j));
                vec4 samp = texture2D(u_texture, sampleCoord);

                // Overbright each channel
                samp.r = max((1.0 / (1.0 - cutoff)) * samp.r - (1.0 / (1.0 - cutoff)) + 1.0, 0.0);
                samp.g = max((1.0 / (1.0 - cutoff)) * samp.g - (1.0 / (1.0 - cutoff)) + 1.0, 0.0);
                samp.b = max((1.0 / (1.0 - cutoff)) * samp.b - (1.0 / (1.0 - cutoff)) + 1.0, 0.0);

                float mixFactor = (2.0 - float(abs(i + j)) / float(BLOOM_AMT + BLOOM_AMT));
                col += min(min(samp.r, samp.g), samp.b) * mixFactor;
            }
        }

        // Average the bloom
        col /= float(BLOOM_AMT * BLOOM_AMT);
        col.a = final_col.a;
    }

    //-------------------------------------------------------------
    // 13) Combine final color and bloom
    //-------------------------------------------------------------
    // We fade between the final_col and the bloom_col based on bloom
    gl_FragColor = (final_col * (1.0 - bloom) + bloom * col) * mask;
}
