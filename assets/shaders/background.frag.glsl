#ifdef GL_ES
precision mediump float;
#endif

// Uniforms
uniform float u_time;
uniform float u_spinTime;
uniform vec4  u_colour1;
uniform vec4  u_colour2;
uniform vec4  u_colour3;
uniform float u_contrast;
uniform float u_spinAmount;
uniform vec2  u_resolution; // (width, height)

// (optional for texturing)
varying vec2 v_texCoord;

void main() {
    // [0..width, 0..height].
    vec2 screenCoords = gl_FragCoord.xy;

    // Pixelation factor based on screen diagonal / 700
    float pixel_size = length(u_resolution) / 700.0;

    // 1) floor the screenCoords for a blocky effect
    // 2) shift by -0.5 * u_resolution so the center is at (0,0)
    // 3) normalize by length(u_resolution)
    vec2 uv = floor(screenCoords / pixel_size) * pixel_size;
         uv = (uv - 0.5 * u_resolution) / length(u_resolution);
    // offset (optional)
    // uv -= vec2(0.12, 0.0);

    float uv_len = length(uv);

    // Swirl part
    float speed           = (u_spinTime * 0.5 * 0.2) + 302.2;
    float swirlAngle      = atan(uv.y, uv.x);
    float swirlStrength   = 0.5 * 20.0 * (u_spinAmount * uv_len + (1.0 - u_spinAmount));
    float new_pixel_angle = swirlAngle + speed - swirlStrength;

    // "mid" is the center in normalized coordinates
    vec2 mid = (u_resolution / length(u_resolution)) * 0.5;
    uv = vec2(
        uv_len * cos(new_pixel_angle) + mid.x,
        uv_len * sin(new_pixel_angle) + mid.y
    ) - mid;

    // The paint effect
    uv *= 30.0;
    speed   = u_time * 2.0;
    vec2 uv2 = uv.x + uv.y; // a combined offset

    for(int i = 0; i < 5; i++) {
        uv2 += sin(max(uv.x, uv.y)) + uv;
        uv  += 0.5 * vec2(
                   cos(5.1123314 + 0.353 * uv2.y + speed * 0.131121),
                   sin(uv2.x - 0.113 * speed)
               );
        uv  -= cos(uv.x + uv.y) - sin(uv.x * 0.711 - uv.y);
    }

    // combine with contrast/spin to get paint amount
    float contrast_mod = 0.25 * u_contrast + 0.5 * u_spinAmount + 1.2;
    float paint_res    = min(2.0, max(0.0, length(uv) * 0.035 * contrast_mod));
    float c1p = max(0.0, 1.0 - contrast_mod * abs(1.0 - paint_res));
    float c2p = max(0.0, 1.0 - contrast_mod * abs(paint_res));
    float c3p = 1.0 - min(1.0, c1p + c2p);

    // Final color blend
    // A portion is just (0.3 / u_contrast) * u_colour1,
    // plus a blend of colour1, colour2, and colour3
    vec4 ret_col = (0.3 / u_contrast) * u_colour1 +
                   (1.0 - 0.3 / u_contrast) * (
                        u_colour1 * c1p +
                        u_colour2 * c2p +
                        vec4(c3p * u_colour3.rgb, c3p * u_colour1.a)
                   );

    gl_FragColor = ret_col;
}
