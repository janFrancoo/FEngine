package shader;

import utils.math.Matrix4f;

import static utils.Constants.SHADOW_FRAGMENT_SHADER_FILE;
import static utils.Constants.SHADOW_VERTEX_SHADER_FILE;

public class ShadowShader extends Shader {

    private int locationMVPMatrix;

    public ShadowShader() {
        super(SHADOW_VERTEX_SHADER_FILE, SHADOW_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationMVPMatrix = super.getUniformLocation("mvpMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }

    public void loadMVPMatrix(Matrix4f mvpMatrix) {
        super.loadMatrix(locationMVPMatrix, mvpMatrix);
    }

}
