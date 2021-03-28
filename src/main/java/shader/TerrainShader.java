package shader;

import model.Light;
import utils.math.Matrix4f;
import utils.math.Vector3f;
import utils.math.Vector4f;

import java.util.List;

import static utils.Constants.*;

public class TerrainShader extends Shader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int[] locationLightPosition;
    private int[] locationLightColor;
    private int[] locationAttenuation;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationFogDensity;
    private int locationFogGradient;
    private int locationSkyColor;
    private int locationBackgroundTexture;
    private int locationRTexture;
    private int locationGTexture;
    private int locationBTexture;
    private int locationBlendMap;
    private int locationCelEnable;
    private int locationCelLevel;
    private int locationClippingPlane;
    private int locationToShadowMapSpace;
    private int locationShadowMap;

    public TerrainShader() {
        super(TERRAIN_VERTEX_SHADER_FILE, TERRAIN_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationLightPosition = new int[MAX_LIGHT];
        locationLightColor = new int[MAX_LIGHT];
        locationAttenuation = new int[MAX_LIGHT];
        for (int i=0; i<MAX_LIGHT; i++) {
            locationLightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationFogDensity = super.getUniformLocation("density");
        locationFogGradient = super.getUniformLocation("gradient");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationBackgroundTexture = super.getUniformLocation("backgroundTexture");
        locationRTexture = super.getUniformLocation("rTexture");
        locationGTexture = super.getUniformLocation("gTexture");
        locationBTexture = super.getUniformLocation("bTexture");
        locationBlendMap = super.getUniformLocation("blendMap");
        locationCelEnable = super.getUniformLocation("celEnable");
        locationCelLevel = super.getUniformLocation("celLevel");
        locationClippingPlane = super.getUniformLocation("clippingPlane");
        locationToShadowMapSpace = super.getUniformLocation("toShadowMapSpace");
        locationShadowMap = super.getUniformLocation("shadowMap");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
    }

    public void connectTextureUnits() {
        super.loadInt(locationBackgroundTexture, 0);
        super.loadInt(locationRTexture, 1);
        super.loadInt(locationGTexture, 2);
        super.loadInt(locationBTexture, 3);
        super.loadInt(locationBlendMap, 4);
        super.loadInt(locationShadowMap, 5);
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
            super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
        }
    }

    public void loadShineValues(float shineDamper, float reflectivity) {
        super.loadFloat(locationShineDamper, shineDamper);
        super.loadFloat(locationReflectivity, reflectivity);
    }

    public void loadFogValues(float fogDensity, float fogGradient) {
        super.loadFloat(locationFogDensity, fogDensity);
        super.loadFloat(locationFogGradient, fogGradient);
    }

    public void loadSkyColor(Vector3f color) {
        super.loadVector(locationSkyColor, color);
    }

    public void loadCelLevel() {
        if (SKY_CEL_LEVEL != 0) {
            super.loadBoolean(locationCelEnable, true);
            super.loadFloat(locationCelLevel, CEL_LEVEL);
        }
    }

    public void loadClippingPlane(Vector4f clippingPlane) {
        super.loadVector(locationClippingPlane, clippingPlane);
    }

    public void loadToShadowMapSpace(Matrix4f matrix) {
        super.loadMatrix(locationToShadowMapSpace, matrix);
    }

}
