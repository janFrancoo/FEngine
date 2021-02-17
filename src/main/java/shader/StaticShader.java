package shader;

import utils.Constants;
import utils.math.Matrix4f;

public class StaticShader extends ShaderLoader {

    private int locationTransformationMatrix;
    private int locationProjectionMatrix;
    private int locationViewMatrix;

    public StaticShader(String vertexShaderFile, String fragmentShaderFile) {
        super(Constants.SHADER_FOLDER + "/" + vertexShaderFile + ".txt",
                Constants.SHADER_FOLDER + "/" + fragmentShaderFile + ".txt");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
        locationProjectionMatrix = super.getUniformLocation("projectionMatrix");
        locationViewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
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

}
