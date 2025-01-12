attribute vec4 a_position;     
attribute vec2 a_texCoord0;    

uniform mat4 u_projTrans;      
uniform vec2 u_resolution;     
uniform float u_vortexAmt;     

varying vec2 v_texCoord;       

void main() {
    // Start with the original vertex position
    vec4 pos = a_position;

    // Screen size, e.g. (width, height)
    vec2 screenSize = u_resolution;

    // Convert from [0..width, 0..height] to a space centered at (0,0),
    // scaled by the diagonal length of the screen:
    vec2 uv = (pos.xy - 0.5 * screenSize) / length(screenSize);

    // Vortex parameters (swirl strength, radius, etc.)
    float effectRadius = 1.6 - 0.05 * u_vortexAmt;
    float effectAngle  = 0.5 + 0.15 * u_vortexAmt;

    // Aspect ratio correction and swirl computations
    float len   = length(uv * vec2(screenSize.x / screenSize.y, 1.0));
    float angle = atan(uv.y, uv.x)
                  + effectAngle * smoothstep(effectRadius, 0.0, len);
    float radius = length(uv);

    // Recompute final X,Y in original pixel space
    // The 'center' is halfway in normalized space, i.e. 0.5*(w,h)/diag
    vec2 center    = 0.5 * screenSize / length(screenSize);
    float screenLen = length(screenSize);

    float finalX = (radius * cos(angle) + center.x) * screenLen;
    float finalY = (radius * sin(angle) + center.y) * screenLen;

    pos.x = finalX;
    pos.y = finalY;

    // Apply LibGDX's standard projection transform
    gl_Position = u_projTrans * pos;

    // Pass the texture coordinate to the fragment shader
    v_texCoord = a_texCoord0;
}
