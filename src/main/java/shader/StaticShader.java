package shader;

import utils.Constants;

public class StaticShader extends ShaderLoader {

    public StaticShader(String vertexShaderFile, String fragmentShaderFile) {
        super(Constants.VERTEX_SHADER_FOLDER + "/" + vertexShaderFile + ".txt",
                Constants.FRAGMENT_SHADER_FOLDER + "/" + fragmentShaderFile + ".txt");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }

}
