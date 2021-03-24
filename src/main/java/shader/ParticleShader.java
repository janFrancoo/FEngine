package shader;

import utils.math.Matrix4f;
import utils.math.Vector2f;

import static utils.Constants.PARTICLE_FRAGMENT_SHADER_FILE;
import static utils.Constants.PARTICLE_VERTEX_SHADER_FILE;

public class ParticleShader extends Shader {

    private int locationProjectionMatrix;
    private int locationRows;

    public ParticleShader() {
        super(PARTICLE_VERTEX_SHADER_FILE, PARTICLE_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationRows = super.getUniformLocation("rows");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "modelViewMatrix");
        super.bindAttribute(5, "textureOffsets");
        super.bindAttribute(6, "blendFactor");
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(locationProjectionMatrix, projectionMatrix);
    }

    public void loadRows(float rows) {
        super.loadFloat(locationRows, rows);
    }

}
