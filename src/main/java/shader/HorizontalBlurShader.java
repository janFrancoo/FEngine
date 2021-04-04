package shader;

import static utils.Constants.BLUR_FRAGMENT_SHADER_FILE;
import static utils.Constants.HORIZONTAL_BLUR_VERTEX_SHADER_FILE;

public class HorizontalBlurShader extends Shader {

    private int locationTargetWidth;

    public HorizontalBlurShader() {
        super(HORIZONTAL_BLUR_VERTEX_SHADER_FILE, BLUR_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTargetWidth = super.getUniformLocation("targetWidth");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadTargetWidth(float targetWidth) {
        super.loadFloat(locationTargetWidth, targetWidth);
    }

}
