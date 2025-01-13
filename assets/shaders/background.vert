#ifdef GL_ES
precision mediump float;
#endif

// Attributes
attribute vec4 a_position;
attribute vec2 a_texCoord0;

uniform mat4 u_projTrans;

varying vec2 v_texCoord;

void main() {
    // Just pass a_texCoord0 to the fragment shader
    v_texCoord = a_texCoord0;

    gl_Position = u_projTrans * a_position;
}