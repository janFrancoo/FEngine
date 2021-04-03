package utils.post_processing;

import model.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import render_engine.ModelLoader;

public class PostProcessing {

    private static final float[] POSITIONS = {-1, 1, -1, -1, 1, 1, 1, -1};
    private static RawModel quad;
    private static ContrastChanger contrastChanger;

    public static void init(ModelLoader loader) {
        quad = loader.loadToVao(POSITIONS, 2);
        contrastChanger = new ContrastChanger();
    }

    public static void doPostProcessing(int colorTexture) {
        GL30.glBindVertexArray(quad.getVaoId());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        contrastChanger.render(colorTexture);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    public static void clean() {
        contrastChanger.clean();
    }

}
