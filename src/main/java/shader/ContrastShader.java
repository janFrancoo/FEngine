package shader;


import static utils.Constants.CONTRAST_FRAGMENT_SHADER_FILE;
import static utils.Constants.CONTRAST_VERTEX_SHADER_FILE;

public class ContrastShader extends Shader {

    public ContrastShader() {
        super(CONTRAST_VERTEX_SHADER_FILE, CONTRAST_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {

    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

}
