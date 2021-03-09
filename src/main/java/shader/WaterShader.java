package shader;

import model.Camera;
import model.Light;
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
    private int locationNormalMap;
    private int locationMoveFactor;
    private int locationCameraPosition;
    private int locationLightPosition;
    private int locationLightColor;

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
        locationNormalMap = super.getUniformLocation("normalMap");
        locationMoveFactor = super.getUniformLocation("moveFactor");
        locationCameraPosition = super.getUniformLocation("cameraPosition");
        locationLightPosition = super.getUniformLocation("lightPosition");
        locationLightColor = super.getUniformLocation("lightColor");
    }

    @Override
    protected void bindAttributes() {
        bindAttribute(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadViewMatrix(Camera camera, Matrix4f matrix) {
        super.loadVector(locationCameraPosition, camera.getPosition());
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void connectTextures() {
        super.loadInt(locationReflectionTexture, 0);
        super.loadInt(locationRefractionTexture, 1);
        super.loadInt(locationDuDvMap, 2);
        super.loadInt(locationNormalMap, 3);
    }

    public void loadMoveFactor(float factor) {
        super.loadFloat(locationMoveFactor, factor);
    }

    public void loadLight(Light light) {
        super.loadVector(locationLightPosition, light.getPosition());
        super.loadVector(locationLightColor, light.getColor());
    }

}
