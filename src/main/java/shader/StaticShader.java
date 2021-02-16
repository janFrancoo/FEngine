package shader;

import utils.Constants;
import utils.math.Matrix4f;

public class StaticShader extends ShaderLoader {

    private int locationTransformationMatrix;

    public StaticShader(String vertexShaderFile, String fragmentShaderFile) {
        super(Constants.VERTEX_SHADER_FOLDER + "/" + vertexShaderFile + ".txt",
                Constants.FRAGMENT_SHADER_FOLDER + "/" + fragmentShaderFile + ".txt");
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

}
