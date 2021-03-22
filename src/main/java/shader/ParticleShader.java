package shader;

import utils.math.Matrix4f;

import static utils.Constants.PARTICLE_FRAGMENT_SHADER_FILE;
import static utils.Constants.PARTICLE_VERTEX_SHADER_FILE;

public class ParticleShader extends Shader {

    private int locationProjectionMatrix;
    private int locationModelViewMatrix;

    public ParticleShader() {
        super(PARTICLE_VERTEX_SHADER_FILE, PARTICLE_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationModelViewMatrix = super.getUniformLocation("modelViewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadProjectionMatrix(Matrix4f projectionMatrix) {
        super.loadMatrix(locationProjectionMatrix, projectionMatrix);
    }

    public void loadModelViewMatrix(Matrix4f modelViewMatrix) {
        super.loadMatrix(locationModelViewMatrix, modelViewMatrix);
    }

}
