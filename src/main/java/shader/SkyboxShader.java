package shader;

import utils.math.Matrix4f;
import utils.math.Vector3f;

import static utils.Constants.SKYBOX_FRAGMENT_SHADER_FILE;
import static utils.Constants.SKYBOX_VERTEX_SHADER_FILE;

public class SkyboxShader extends Shader {

    private int locationProjectionMatrix;
    private int locationViewMatrix;
    private int locationFogColor;
    private int locationBlendFactor;
    private int locationCubeMap;
    private int locationCubeMap2;

    public SkyboxShader() {
        super(SKYBOX_VERTEX_SHADER_FILE, SKYBOX_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
        locationFogColor = super.getUniformLocation("fogColor");
        locationBlendFactor = super.getUniformLocation("blendFactor");
        locationCubeMap = super.getUniformLocation("cubeMap");
        locationCubeMap2 = super.getUniformLocation("cubeMap2");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadProjectionMatrix(Matrix4f matrix){
        super.loadMatrix(locationProjectionMatrix, matrix);
    }

    public void loadViewMatrix(Matrix4f matrix) {
        super.loadMatrix(locationViewMatrix, matrix);
    }

    public void loadFogColor(Vector3f fogColor) {
        super.loadVector(locationFogColor, fogColor);
    }

    public void loadBlendFactor(float blendFactor) {
        super.loadFloat(locationBlendFactor, blendFactor);
    }

    public void connectTextureUnits() {
        super.loadInt(locationCubeMap, 0);
        super.loadInt(locationCubeMap2, 1);
    }

}
