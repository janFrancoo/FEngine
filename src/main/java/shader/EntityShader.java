package shader;

import model.Light;
import utils.math.Matrix4f;
import utils.math.Vector2f;
import utils.math.Vector3f;

import java.util.List;

import static utils.Constants.*;

public class EntityShader extends Shader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int[] locationLightPosition;
    private int[] locationLightColor;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationFakeLight;
    private int locationFogDensity;
    private int locationFogGradient;
    private int locationSkyColor;
    private int locationTextureRows;
    private int locationTextureOffset;

    public EntityShader() {
        super(VERTEX_SHADER_FILE, FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationLightPosition = new int[MAX_LIGHT];
        locationLightColor = new int[MAX_LIGHT];
        for (int i=0; i<MAX_LIGHT; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
        }
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationFakeLight = super.getUniformLocation("fakeLight");
        locationFogDensity = super.getUniformLocation("density");
        locationFogGradient = super.getUniformLocation("gradient");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationTextureRows = super.getUniformLocation("textureRows");
        locationTextureOffset = super.getUniformLocation("textureOffset");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadLights(List<Light> lights) {
        for (int i=0; i<lights.size() && i<MAX_LIGHT; i++) {
            super.loadVector(locationLightPosition[i], lights.get(i).getPosition());
            super.loadVector(locationLightColor[i], lights.get(i).getColor());
        }
    }

    public void loadShineValues(float shineDamper, float reflectivity) {
        super.loadFloat(locationShineDamper, shineDamper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    public void loadFakeLightValue(boolean fakeLight) {
        super.loadBoolean(locationFakeLight, fakeLight);
    }

    public void loadFogValues(float fogDensity, float fogGradient) {
        super.loadFloat(locationFogDensity, fogDensity);
        super.loadFloat(locationFogGradient, fogGradient);
    }

    public void loadSkyColor(Vector3f color) {
        super.loadVector(locationSkyColor, color);
    }

    public void loadTextureRows(int rows) {
        super.loadFloat(locationTextureRows, rows);
    }

    public void loadTextureOffset(Vector2f textureOffset) {
        super.loadVector(locationTextureOffset, textureOffset);
    }

}
