package shader;

import utils.math.Matrix4f;

import static utils.Constants.WATER_FRAGMENT_SHADER_FILE;
import static utils.Constants.WATER_VERTEX_SHADER_FILE;

public class WaterShader extends Shader {

    private int locationTransformationMatrix;
    private int locationViewMatrix;
    private int locationProjectionMatrix;
    private int locationReflectionTexture;
    private int locationRefractionTexture;
    private int locationDuDvMap;
    private int locationMoveFactor;

    public WaterShader() {
        super(WATER_VERTEX_SHADER_FILE, WATER_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationReflectionTexture = super.getUniformLocation("reflectionTexture");
        locationRefractionTexture = super.getUniformLocation("refractionTexture");
        locationDuDvMap = super.getUniformLocation("dudvMap");
        locationMoveFactor = super.getUniformLocation("moveFactor");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void connectTextures() {
        super.loadInt(locationReflectionTexture, 0);
        super.loadInt(locationRefractionTexture, 1);
        super.loadInt(locationDuDvMap, 2);
    }

    public void loadMoveFactor(float factor) {
        super.loadFloat(locationMoveFactor, factor);
    }

}
