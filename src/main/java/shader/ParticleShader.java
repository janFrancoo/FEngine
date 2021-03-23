package shader;

import utils.math.Matrix4f;
import utils.math.Vector2f;

import static utils.Constants.PARTICLE_FRAGMENT_SHADER_FILE;
import static utils.Constants.PARTICLE_VERTEX_SHADER_FILE;

public class ParticleShader extends Shader {

    private int locationProjectionMatrix;
    private int locationModelViewMatrix;
    private int locationTextureOffset1;
    private int locationTextureOffset2;
    private int locationTextureCoordInfo;

    public ParticleShader() {
        super(PARTICLE_VERTEX_SHADER_FILE, PARTICLE_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationModelViewMatrix = super.getUniformLocation("modelViewMatrix");
        locationTextureOffset1 = super.getUniformLocation("textureOffset1");
        locationTextureOffset2 = super.getUniformLocation("textureOffset2");
        locationTextureCoordInfo = super.getUniformLocation("textureCoordInfo");
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

    public void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float rows, float blend) {
        super.loadVector(locationTextureOffset1, offset1);
        super.loadVector(locationTextureOffset2, offset2);
        super.loadVector(locationTextureCoordInfo, new Vector2f(rows, blend));
    }

}
