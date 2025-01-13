#ifdef GL_ES
precision mediump float;
#endif

// Attributes coming from your mesh or SpriteBatch
attribute vec4 a_position;
attribute vec2 a_texCoord0;

// libgdx transform
uniform mat4 u_projTrans;

uniform float hovering;          // 0 or 1 (or something in between) to enable/disable the "hover" effect
uniform float screen_scale;      // scale factor for distance-based offset
uniform vec2  mouse_screen_pos;  // (mouseX, mouseY) in screen coords
uniform vec2  u_resolution;      // (screenWidth, screenHeight)

varying vec2 v_texCoord;

void main()
{
    // Forward the texture coordinate
    v_texCoord = a_texCoord0;

    // First transform to clip-space
    vec4 worldPos = u_projTrans * a_position;

    // Optionally apply "hovering" parallax / bulge effect
    if (hovering > 0.0) {
        // Distance from screen center (or from a reference).
        // Here, we approximate logic from your snippet that references love_ScreenSize.
        float mid_dist = screen_scale
            * length((worldPos.xy / screen_scale) - 0.5 * u_resolution)
            / length(u_resolution);

        // Mouse offset for local distortion
        vec2 mouse_offset = (worldPos.xy - mouse_screen_pos) / screen_scale;

        // A small vertical offset in clip-space Z (or W) to simulate a "pop-out" effect
        float scale = 0.002
            * (-0.03 - 0.3 * max(0.0, 0.3 - mid_dist))
            * hovering
            * (length(mouse_offset) * length(mouse_offset))
            / (2.0 - mid_dist);

        // Add to the z-position to move the vertex slightly forward/back
        worldPos.z += scale;
    }

    gl_Position = worldPos;
}
