package utils.post_processing;

import model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import render_engine.ModelLoader;

import static utils.Constants.HEIGHT;
import static utils.Constants.WIDTH;

public class PostProcessing {

    private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
    private static RawModel quad;
    private static ContrastChanger contrastChanger;
    private static HorizontalBlur horizontalBlur;
    private static VerticalBlur verticalBlur;

    public static void init(ModelLoader loader) {
        quad = loader.loadToVao(POSITIONS, 2);
        contrastChanger = new ContrastChanger();
        horizontalBlur = new HorizontalBlur(WIDTH, HEIGHT);
        verticalBlur = new VerticalBlur(WIDTH, HEIGHT);
    }

    public static void doPostProcessing(int colorTexture) {
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        // horizontalBlur.render(colorTexture);
        // verticalBlur.render(horizontalBlur.getOutputTexture());
        contrastChanger.render(colorTexture);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public static void clean() {
        contrastChanger.clean();
        horizontalBlur.clean();
        verticalBlur.clean();
    }

}
