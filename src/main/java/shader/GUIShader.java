package shader;

import utils.math.Matrix4f;

import static utils.Constants.GUI_FRAGMENT_SHADER_FILE;
import static utils.Constants.GUI_VERTEX_SHADER_FILE;

public class GUIShader extends Shader {

    private int locationTransformationMatrix;

    public GUIShader() {
        super(GUI_VERTEX_SHADER_FILE, GUI_FRAGMENT_SHADER_FILE);
    }

    @Override
    protected void getAllUniformLocations() {
        locationTransformationMatrix = super.getUniformLocation("transformationMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(locationTransformationMatrix, matrix);
    }

}
