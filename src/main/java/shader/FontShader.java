package shader;

import utils.math.Vector2f;
import utils.math.Vector3f;

import static utils.Constants.FONT_FRAGMENT_SHADER_FILE;
import static utils.Constants.FONT_VERTEX_SHADER_FILE;

public class FontShader extends Shader {

    private int locationTranslation;
    private int locationColor;

    public FontShader() {
        super(FONT_VERTEX_SHADER_FILE, FONT_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTranslation = super.getUniformLocation("translation");
        locationColor = super.getUniformLocation("color");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoord");
    }

    public void loadTranslation(Vector2f translation) {
        super.loadVector(locationTranslation, translation);
    }

    public void loadColor(Vector3f color) {
        super.loadVector(locationColor, color);
    }

}
