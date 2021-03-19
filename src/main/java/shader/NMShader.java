package shader;

import model.Light;
import utils.math.Matrix4f;
import utils.math.Vector2f;
import utils.math.Vector3f;
import utils.math.Vector4f;

import java.util.List;

import static utils.Constants.*;

public class NMShader extends Shader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int[] locationLightPositionEyeSpace;
    private int[] locationLightColor;
    private int[] locationAttenuation;
    private int locationShineDamper;
    private int locationReflectivity;
    private int locationSkyColor;
    private int locationTextureRows;
    private int locationTextureOffset;
    private int locationClippingPlane;
    private int locationModelTexture;
    private int locationNormalMap;

    public NMShader() {
        super(NM_VERTEX_SHADER_FILE, NM_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationLightPositionEyeSpace = new int[MAX_LIGHT];
        locationLightColor = new int[MAX_LIGHT];
        locationAttenuation = new int[MAX_LIGHT];
        for (int i=0; i<MAX_LIGHT; i++) {
            locationLightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
            locationLightColor[i] = super.getUniformLocation("lightColour[" + i + "]");
            locationAttenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
        locationShineDamper = super.getUniformLocation("shineDamper");
        locationReflectivity = super.getUniformLocation("reflectivity");
        locationSkyColor = super.getUniformLocation("skyColor");
        locationTextureRows = super.getUniformLocation("numberOfRows");
        locationTextureOffset = super.getUniformLocation("offset");
        locationClippingPlane = super.getUniformLocation("clippingPlane");
        locationModelTexture = super.getUniformLocation("modelTexture");
        locationNormalMap = super.getUniformLocation("normalMap");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
    }

    public void connectTextureUnits(){
        super.loadInt(locationModelTexture, 0);
        super.loadInt(locationNormalMap, 1);
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

    public void loadLights(List<Light> lights, Matrix4f viewMatrix) {
        for (int i=0; i<lights.size() && i<MAX_LIGHT; i++) {
            super.loadVector(locationLightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
            super.loadVector(locationLightColor[i], lights.get(i).getColor());
            super.loadVector(locationAttenuation[i], lights.get(i).getAttenuation());
        }
    }

    public void loadShineValues(float shineDamper, float reflectivity) {
        super.loadFloat(locationShineDamper, shineDamper);
        super.loadFloat(locationReflectivity, reflectivity);
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

    public void loadClippingPlane(Vector4f clippingPlane) {
        super.loadVector(locationClippingPlane, clippingPlane);
    }

    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix){
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
        Matrix4f.transform(viewMatrix, eyeSpacePos, eyeSpacePos);
        return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
    }

}
