package shader;

import static utils.Constants.BLUR_FRAGMENT_SHADER_FILE;
import static utils.Constants.VERTICAL_BLUR_VERTEX_SHADER_FILE;

public class VerticalBlurShader extends Shader {

    private int locationTargetHeight;

    public VerticalBlurShader() {
        super(VERTICAL_BLUR_VERTEX_SHADER_FILE, BLUR_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTargetHeight = super.getUniformLocation("targetHeight");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadTargetHeight(float targetHeight) {
        super.loadFloat(locationTargetHeight, targetHeight);
    }

}
